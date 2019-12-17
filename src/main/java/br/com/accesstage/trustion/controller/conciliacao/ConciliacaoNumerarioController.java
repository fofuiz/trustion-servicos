package br.com.accesstage.trustion.controller.conciliacao;

import br.com.accesstage.trustion.model.*;
import br.com.accesstage.trustion.repository.interfaces.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsavel por receber um get que serve como trigger para o start do servico de conciliacao de vendas x pac
 * A conciliacao e realizada da seguinte forma:
 * - Primeiro pegamos apenas as lojas que devem ser conciliadas no dia de hoje
 * - geramos uma lista de dados de pac
 * - geramos uma lista de dados de vendas
 * - realizamos a somatoria e calculamos a diferenca
 * - salvamos em uma tabela de conciliacao
 * - atualizamos a data de ultima conciliacao na tabela de conciliacao de vendas
 */
@Slf4j
@RestController
public class ConciliacaoNumerarioController {

    @Autowired
    private ICalendarioColetaNumerarioRepository iCalendarioColetaNumerarioRepository;

    @Autowired
    private IVendasRepository repVendas;

    @Autowired
    private IPacRepository repPac;

    @Autowired
    private IConciliacaoVendasPac  repConciliacaoVendasPac;

    @Autowired
    private IEmpresaRepository repEmpresa;

    @RequestMapping(value = "/conciliacao/numerario", method = RequestMethod.GET)
    public void conciliaNumerario(){

        //Pega o dia de hoje
        LocalDate hoje = LocalDate.now();

        //Filtra resultado da base e gera lista de objetos para serem conciliados na data atual
        List<CalendarioColetaNumerario> conciliarHoje = geraListaConciliaHoje(hoje.getDayOfWeek().toString().toLowerCase());
        log.info(conciliarHoje.size() + " cnpj para conciliar hoje");

        //para cada cnpj que tem para conciliar no dia
        conciliarHoje.forEach(x -> {

            //pega o id da loja que sera conciliada neste momento e a quantidade de coletas na semana
            int idloja = x.getIdloja();
            int qtdColetasSemana = x.getQtdColetaSemana();

            //Data de inicio da conciliacao = o ultimo dia conciliado mais um
            //Data fim  = o dia de hoje menos um
            LocalDate dtInicio = x.getUltDiaConciliado().plusDays(1);
            LocalDate dtFim = hoje.minusDays(1);

            //LocalDate para Date pois implementamos as classes pac e movimentodiario utilizando date
            Date dtInicioDate = Date.from(dtInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dtFimDate = Date.from(dtFim.atStartOfDay(ZoneId.systemDefault()).toInstant());

            log.info("CNPJ: " + x.getCnpj() + ", ultima data conciliacao: " + x.getUltDiaConciliado() + " data inicio conciliacao: " + dtInicio + " data fim conciliação: " +  dtFim);

            if (!hoje.toString().equalsIgnoreCase(x.getUltDiaConciliado().toString())){
                //gera listas de pac e movimento diario baseado na data de inicio, fim  e id da loja
                List<Pac> allPac = geraListaPac(idloja, dtInicioDate, dtFimDate);
                List<MovimentoDiario> allMovimentoDiario = geralistaMovimentoDiario(idloja, dtInicioDate, dtFimDate);

                assert allPac != null;
                int numeroGtv = allPac.get(0).getGtv();
                LocalDate dataGtv = allPac.get(0).getDataColeta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                double somaMoviemntoDiario = allMovimentoDiario.stream().mapToDouble(MovimentoDiario::getValor).sum();
//                double somaPac = allPac.stream().mapToDouble(Pac::getVlrDeclarado).sum();
                double somaPacVlrDeclarado = allPac.get(0).getVlrDeclaradoGtv();
                double somaPacVlrConferido = allPac.get(0).getVlrConferidoGtv();
                boolean conciliadoComSucesso = somaMoviemntoDiario == somaPacVlrDeclarado;
                double diferenca = (somaMoviemntoDiario > somaPacVlrDeclarado) ? somaMoviemntoDiario - somaPacVlrDeclarado : somaPacVlrDeclarado - somaMoviemntoDiario;


                Empresa empresa = repEmpresa.findBySiglaLoja(allPac.get(0).getSigla());

                if(empresa != null){

                    String razaoSocial = empresa.getRazaoSocial();
                    String cnpj = empresa.getCnpj();
                    String siglaLoja = empresa.getSiglaLoja();


                    repConciliacaoVendasPac.save(
                            ConciliacaoVendasPac.builder()
                                    .somatoriaVendas(somaMoviemntoDiario)
                                    .pacVlrDeclarado(somaPacVlrDeclarado)
                                    .dataConciliacao(hoje)
                                    .idLoja(idloja)
                                    .dataInicioConciliacao(dtInicio)
                                    .dataFimConciliacao(dtFim)
                                    .conciliadoComSucesso(conciliadoComSucesso)
                                    .diferenca(diferenca)
                                    .numeroGtv((long) numeroGtv)
                                    .siglaLoja(siglaLoja)
                                    .datagtv(dataGtv)
                                    .qtdColSemana(qtdColetasSemana)
                                    .cnpj(cnpj)
                                    .razaoSocial(razaoSocial)
                                    .pacVlrConferido(somaPacVlrConferido)
                                    .build()
                    );

                    //Atualiza a ultima data de conciliacao para essa loja
                    x.setUltDiaConciliado(hoje);
                    iCalendarioColetaNumerarioRepository.save(x);
                }else {
                    log.error("Empresa nao encontrada");
                }

            } else {
                log.info("CNPJ: " + x.getCnpj() + " ja foi conciliado hoje");
            }


        });
    }


    /**metodo gera uma lista de objetos do tipo calendarioColetaNumerario que serao utilizados para definir quem deve ser
     * conciliado na data que o job estiver rodando
     * @param diaDaSemana dia da semana
     * @return lista de objetos do tipo calendarioColetaNumerario
     */
    private List<CalendarioColetaNumerario> geraListaConciliaHoje(String diaDaSemana){

        List<CalendarioColetaNumerario> conciliarHoje = new ArrayList<>();

        iCalendarioColetaNumerarioRepository.findAll().forEach(item -> {

            if (item.isMonday() && diaDaSemana.equalsIgnoreCase("monday")){
                conciliarHoje.add(item);
            }
            if (item.isTuesday() && diaDaSemana.equalsIgnoreCase("tuesday")){
                conciliarHoje.add(item);
            }
            if (item.isWednesday() && diaDaSemana.equalsIgnoreCase("wednesday")){
                conciliarHoje.add(item);
            }
            if (item.isThursday() && diaDaSemana.equalsIgnoreCase("thursday")){
                conciliarHoje.add(item);
            }
            if (item.isFriday() && diaDaSemana.equalsIgnoreCase("friday")){
                conciliarHoje.add(item);
            }
            if (item.isSaturday() && diaDaSemana.equalsIgnoreCase("saturday")){
                conciliarHoje.add(item);
            }
            if(item.isSunday() && diaDaSemana.equalsIgnoreCase("sunday")){
                conciliarHoje.add(item);
            }
        });
        return conciliarHoje;
    }


    /**metodo gera uma lista de objetos do tipo pac por data e id de loja
     * @param idloja numero referente ao id da loja
     * @param dtInicioDate data inicio que deve ser considerada na conciliacao
     * @param dtFimDate data fim que deve ser considerada na conciliacao
     * @return lista de objetos do tipo pac
     */
    private List<Pac> geraListaPac(int idloja, Date dtInicioDate, Date dtFimDate){
        try {
            return repPac.findAllByDataColetaGreaterThanEqualAndDataColetaLessThanEqual(dtInicioDate, dtFimDate)
                    .stream()
                    .filter(linha -> linha.getNumero() == idloja)
                    .collect(Collectors.toList());
        }catch(Exception e) {
            log.error("Erro para gerar a lista PAC");
            log.error(String.valueOf(e.getCause()));
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    };

    /**metodo gera uma lista de objetos do tipo MovimentoDiario por data e id de loja
     * @param idloja numero referente ao id da loja
     * @param dtInicioDate data inicio que deve ser considerada na conciliacao
     * @param dtFimDate data fim que deve ser considerada na conciliacao
     * @return lista de objetos do tipo MovimentoDiario
     */
    private List<MovimentoDiario> geralistaMovimentoDiario(int idloja, Date dtInicioDate, Date dtFimDate) {
        //Consulta tablea vendas por range de datas
        try{
            return repVendas.findAllByDataMovimentoDiarioGreaterThanEqualAndDataMovimentoDiarioLessThanEqual(dtInicioDate,dtFimDate)
                    .stream()
                    .filter(linha -> linha.getTipoPagamento().equalsIgnoreCase("Dinheiro"))
                    .filter(linha -> linha.getIdRestaurante() == idloja)
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("Erro para gerar a lista Movimento diario");
            log.error(String.valueOf(e.getCause()));
            log.error(e.getMessage());
            return Collections.emptyList();
        }

    }

}
