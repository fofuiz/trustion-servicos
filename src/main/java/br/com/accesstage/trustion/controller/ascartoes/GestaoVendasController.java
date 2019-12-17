package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.ascartoes.model.FatoTransacao;
import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoCloCA;
import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoElvCA;
import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoRdcCA;
import br.com.accesstage.trustion.ascartoes.model.RemessaConciliacaoDetalhe;
import br.com.accesstage.trustion.ascartoes.model.StatusConciliacaoCA;
import br.com.accesstage.trustion.ascartoes.model.VisaoGestaoVendasCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.EdicaoConciliacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.RemessaConciliacaoDetalheDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IEmpresaCaService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoCloService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoElvService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoRdcService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IGestaoVendasService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IRemessaConciliacaoDetalheService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IStatusConciliacaoService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IVisaoGestaoVendasService;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;

@RestController
@RequestMapping("/gestaovendas")
public class GestaoVendasController {

    // Flags
    private static final Long FLG_CONCILIACAO = 2L;
    private static final Long FLG_EXCLUSAO = 0L;
    private static final Short FLG_LIQUIDADO = 0;

    // Operadoras
    private static final int OPERADORA_ELAVON = 22;
    private static final int OPERADORA_CIELO = 2;
    private static final int OPERADORA_REDECARD = 6;

    @Log
    private static Logger LOGGER;

    @Autowired
    private IEmpresaCaService empresaCaService;

    @Autowired
    private IGestaoVendasService gestaoVendasService;

    @Autowired
    private IStatusConciliacaoService statusConciliacaoService;

    @Autowired
    private IVisaoGestaoVendasService visaoGestaoVendaService;

    @Autowired
    private IFatoTransacaoService fatoTransacaoService;

    @Autowired
    private IFatoTransacaoCloService fatoTransacaoCloService;

    @Autowired
    private IFatoTransacaoRdcService fatoTransacaoRdcService;

    @Autowired
    private IFatoTransacaoElvService fatoTransacaoElvService;

    @Autowired
    private IRemessaConciliacaoDetalheService remessaConciliacaoService;

    private List<RemessaConciliacaoDetalheDTO> listarTransacoesASerConciliada;
    private List<RemessaConciliacaoDetalheDTO> listarPossiveisTransacoes;
    private List<RemessaConciliacaoDetalheDTO> listaTransacaoRemessa;
    private List<RemessaConciliacaoDetalheDTO> listaTransacaoFato;

    /**
     * Pesquisa as divergências com base nos filtros informados.
     *
     * @param filtro
     * @return
     */
    @GetMapping(value = "/criterios", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<GestaoVendasDTO>> pesquisa(FiltroGestaoVendaDTO filtro) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        try {
            
            filtro.setEmpresa(getEmpresaCA(filtro.getListaEmpresas()));
            List<GestaoVendasDTO> gestaoVendasDTOs = gestaoVendasService.pesquisar(filtro);

            LOGGER.info(Utils.getFimMetodo());
            
            return ResponseEntity.ok(gestaoVendasDTOs);

        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de " + UTF8.creditos}));
        }

    }

    private EmpresaCA getEmpresaCA(List<String> empresas) {
        String empresa = empresas.get(0);
        if (empresa != null) {
            long id = Long.parseLong(empresa);
            return empresaCaService.find(id);
        }
        return null;
    }

    /**
     * Metodo para montar tabela de semaforo
     *
     * @param filtro
     * @return
     */
    @GetMapping(value = "/semaforo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<SemaforoDTO>> atualizaSemaforo(FiltroGestaoVendaDTO filtro) {
        
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);
        
        int contadorQtdConciliados = 0;
        int contadorQtdNConciliados = 0;
        int contadorQtdPendente = 0;

        List<SemaforoDTO> listaSemaforo = new ArrayList<>();
        BigDecimal somaNCon = BigDecimal.ZERO;
        BigDecimal somaCon = BigDecimal.ZERO;
        BigDecimal somaPendente = BigDecimal.ZERO;

        try {
            
            filtro.setEmpresa(getEmpresaCA(filtro.getListaEmpresas()));
            List<GestaoVendasDTO> gestaoVendasDTOs = gestaoVendasService.pesquisarSemaforo(filtro);

            if (gestaoVendasDTOs != null) {
                for (GestaoVendasDTO dto : gestaoVendasDTOs) {

                    if (null != dto.getStatusConciliacao()) {
                        switch (dto.getStatusConciliacao()) {
                            case 1:
                            case 2:
                                contadorQtdConciliados = contadorQtdConciliados + 1;
                                BigDecimal valorCon = dto.getValor();
                                somaCon = somaCon.add(valorCon);
                                break;
                            case 3:
                                contadorQtdNConciliados = contadorQtdNConciliados + 1;
                                BigDecimal valorNCon = dto.getValor();
                                somaNCon = somaNCon.add(valorNCon);
                                break;
                            case 0:
                                contadorQtdPendente = contadorQtdPendente + 1;
                                BigDecimal valorPendente = dto.getValor();
                                somaPendente = somaPendente.add(valorPendente);
                                break;
                            default:
                                break;
                        }
                    }
                }

                SemaforoDTO dto = new SemaforoDTO();
                SemaforoDTO dto1 = new SemaforoDTO();
                SemaforoDTO dto2 = new SemaforoDTO();

                dto.setQuantidade(contadorQtdConciliados);
                dto.setValor(somaCon);
                dto.setStatus(1);

                dto1.setQuantidade(contadorQtdNConciliados);
                dto1.setValor(somaNCon);
                dto1.setStatus(3);

                dto2.setQuantidade(contadorQtdPendente);
                dto2.setValor(somaPendente);
                dto2.setStatus(0);

                listaSemaforo.add(dto);
                listaSemaforo.add(dto2);
                listaSemaforo.add(dto1);

                /**
                 * SOMA OS VALORES PARA A ÚLTIMA LINHA
                 */
                SemaforoDTO totais = new SemaforoDTO();
                int soma;

                if (!listaSemaforo.isEmpty()) {
                    for (int i = 0; i < listaSemaforo.size(); i++) {
                        BigDecimal vSoma = BigDecimal.ZERO;

                        if (totais.getValor() != null) {
                            vSoma = totais.getValor();
                        }
                        soma = contadorQtdConciliados + contadorQtdNConciliados + contadorQtdPendente;
                        totais.setQuantidade(soma);

                        if (listaSemaforo.get(i).getValor() != null) {
                            totais.setValor(listaSemaforo.get(i).getValor().add(vSoma));
                        }
                    }
                    listaSemaforo.add(totais);

                } else {
                    listaSemaforo = gestaoVendasService.carregarSemaforoZerado();
                }
            } else {
                listaSemaforo = gestaoVendasService.carregarSemaforoZerado();
            }
            
            LOGGER.info(Utils.getFimMetodo());
         
            return ResponseEntity.ok(listaSemaforo);

        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de " + UTF8.creditos}));
        }
    }

    /**
     * Concilia a partir do RemessaConciliacaoDetalheDTO
     *
     * @param linhaSelecionada
     * @param camposInvalidos
     * @return
     */
    @PostMapping(value = "/conciliar", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> conciliar(@Valid @RequestBody RemessaConciliacaoDetalheDTO linhaSelecionada, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + "linhaSelecionada: " + linhaSelecionada);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get("divergenciaview.dialog.aviso"));
        }

        try {

            List<RemessaConciliacaoDetalheDTO> remessaList = buscarTransacoesRefAgrupamento(linhaSelecionada);

            if (!Utils.isNotEmpty(remessaList)) {
                throw new BadRequestResponseException(Mensagem.get("divergenciaview.dialog.aviso"));
            }

            Long codLoja = 0L;
            for (RemessaConciliacaoDetalheDTO remessa : remessaList) {

                if (remessa.getCodLoja() != null) {
                    codLoja = remessa.getCodLoja();
                }
                final String dscAreaCliente = remessaList.get(0).getDscAreaCliente();

                switch (remessa.getCodOperadora().intValue()) {
                    case OPERADORA_CIELO:
                        FatoTransacaoCloCA fatoTransacaoClo = fatoTransacaoCloService.listarHashClo(remessa.getHashValue());

                        fatoTransacaoClo.setFlgConciliacao(BigInteger.valueOf(FLG_CONCILIACAO));
                        fatoTransacaoClo.setFlgLiquidado(FLG_LIQUIDADO);
                        fatoTransacaoClo.setDtaConciliacao(new Date());
                        fatoTransacaoClo.setDscAreaCliente(dscAreaCliente);
                        fatoTransacaoCloService.merge(fatoTransacaoClo);

                        remessaConciliacaoService.merge(getRemessa(remessa.getHashValue(), fatoTransacaoClo.getEmpid(), linhaSelecionada.getCodDetalhe()));
                        break;

                    case OPERADORA_REDECARD:
                        FatoTransacaoRdcCA fatoTransacaoRdc = fatoTransacaoRdcService.listarHashRdc(remessa.getHashValue());
                        fatoTransacaoRdc.setFlgConciliacao(BigInteger.valueOf(FLG_CONCILIACAO));
                        fatoTransacaoRdc.setFlgLiquidado(FLG_LIQUIDADO);
                        fatoTransacaoRdc.setDtaConciliacao(new Date());
                        fatoTransacaoRdc.setDscAreaCliente(dscAreaCliente);
                        fatoTransacaoRdcService.merge(fatoTransacaoRdc);

                        remessaConciliacaoService.merge(getRemessa(remessa.getHashValue(), fatoTransacaoRdc.getEmpid(), linhaSelecionada.getCodDetalhe()));
                        break;

                    case OPERADORA_ELAVON:
                        FatoTransacaoElvCA fatoTransacaoElv = fatoTransacaoElvService.listarHashElv(remessa.getHashValue());
                        fatoTransacaoElv.setFlgConciliacao(BigInteger.valueOf(FLG_CONCILIACAO));
                        fatoTransacaoElv.setFlgLiquidado(FLG_LIQUIDADO);
                        fatoTransacaoElv.setDtaConciliacao(new Date());
                        fatoTransacaoElv.setDscAreaCliente(dscAreaCliente);
                        fatoTransacaoElvService.merge(fatoTransacaoElv);

                        remessaConciliacaoService.merge(getRemessa(remessa.getHashValue(), fatoTransacaoElv.getEmpid(), linhaSelecionada.getCodDetalhe()));
                        break;

                    default:
                        FatoTransacao fatoTransacao = fatoTransacaoService.listarHash(remessa.getHashValue());
                        fatoTransacao.setFlgConciliacao(FLG_CONCILIACAO);
                        fatoTransacao.setFlgLiquidado(FLG_LIQUIDADO);
                        fatoTransacao.setDtaConciliacao(new Date());
                        fatoTransacao.setDscAreaCliente(dscAreaCliente);
                        fatoTransacaoService.merge(fatoTransacao);

                        remessaConciliacaoService.merge(getRemessa(remessa.getHashValue(), fatoTransacao.getEmpid(), linhaSelecionada.getCodDetalhe()));
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("divergenciaview.dialog.erro"));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Desconcilia a partir do hash value de um RemessaConciliacaoDetalheDTO.
     *
     * @param hashValue
     * @return
     */
    @GetMapping(value = "/desconciliar")
    public ResponseEntity<Void> desconciliar(@RequestParam String hashValue) {

        LOGGER.info(Utils.getInicioMetodo() + "hashValue: " + hashValue);

        if ("".equals(hashValue)) {
            throw new BadRequestResponseException(Mensagem.get("divergenciaview.dialog.aviso"));
        }

        try {
            remessaConciliacaoService.desconciliarTransacoes(hashValue);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("divergenciaview.dialog.erro"));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private RemessaConciliacaoDetalhe getRemessa(String hashValue, Number empId, Long idDetalhe) {
        RemessaConciliacaoDetalhe remessa = remessaConciliacaoService.findById(idDetalhe);
        remessa.setFlgConciliacao(FLG_CONCILIACAO);
        remessa.setFlgExclusao(FLG_EXCLUSAO);
        remessa.setDtaConciliacao(new Date());
        remessa.setHashValue(hashValue);
        remessa.setEmpid(empId.longValue());

        return remessa;
    }

    private List<RemessaConciliacaoDetalheDTO> buscarTransacoesRefAgrupamento(RemessaConciliacaoDetalheDTO remessa) {
        List<RemessaConciliacaoDetalheDTO> listTransacoes = remessaConciliacaoService.desagrupaTransacoes(
                remessa.getDscAutorizacao(), remessa.getCodNsu(), remessa.getEmpId(),
                remessa.getDtaVenda(), remessa.getCodOperadora(), remessa.getNroPlano(), remessa.getNomeProduto());
        return listTransacoes;
    }

    /**
     * Botão editar da tela gestaoVendas.xhtml
     *
     * @param divergenciaSelecionado
     * @param camposInvalidos
     * @return
     */
    @PostMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<EdicaoConciliacaoDTO> editar(@Valid @RequestBody GestaoVendasDTO divergenciaSelecionado, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + divergenciaSelecionado);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }
        
        EdicaoConciliacaoDTO dto = new EdicaoConciliacaoDTO();

        List<RemessaConciliacaoDetalheDTO> dtos = new ArrayList<>();

        if (divergenciaSelecionado.getStatusConciliacao() == 1) {
            carregaListaDesconciliar(divergenciaSelecionado);
            
            dto.setListaTransacaoRemessa(listaTransacaoRemessa);
            dto.setListaTransacaoFato(listaTransacaoFato);
        } else {
            carregaListas(divergenciaSelecionado);
            
            dto.setListarTransacoesASerConciliada(listarTransacoesASerConciliada);
            dto.setListarPossiveisTransacoes(listarPossiveisTransacoes);
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * *
     * Consultar as transaçoes da operadora e remessa para desconciliar.
     *
     * @param divergencia
     */
    public void carregaListaDesconciliar(GestaoVendasDTO divergencia) {

        listaTransacaoFato = remessaConciliacaoService.fatoTransacaoDesconciliar(divergencia);

        listaTransacaoRemessa = remessaConciliacaoService.remessaDesconciliar(divergencia.getHashValue());

    }

    /**
     * Carrega as listas das tabelas a serem exibidas em tela.
     *
     * @param gestaoVendasDTO
     */
    private void carregaListas(GestaoVendasDTO gestaoVendasDTO) {

        Long empId = Long.parseLong(gestaoVendasDTO.getListaEmpresas().get(0));

        listarTransacoesASerConciliada = remessaConciliacaoService.listaTransacoesASeremConciliadas(
                gestaoVendasDTO.getIdDetalhe(), empId, gestaoVendasDTO.getData());

        listarPossiveisTransacoes = remessaConciliacaoService.listaPossiveisTransacoes(
                gestaoVendasDTO.getCodAutorizacao(), Long.parseLong(gestaoVendasDTO.getNsu()),
                empId, gestaoVendasDTO.getData(), gestaoVendasDTO.getCodProduto(),
                gestaoVendasDTO.getCodOperadora());
    }

    /**
     * Carrega as listas status de conciliacao
     *
     * @param idVisao
     * @return
     */
    @GetMapping(value = "/listarStatusConciliacao", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<StatusConciliacaoCA>> listarStatusConciliacao(@RequestParam int idVisao) {
        List<StatusConciliacaoCA> listaStatusConciliacao;
        if (idVisao == 1) {
            listaStatusConciliacao = statusConciliacaoService.listarTodos();
        } else {
            listaStatusConciliacao = statusConciliacaoService.listarTodos();
            listaStatusConciliacao.remove(1);
        }
        return new ResponseEntity<>(listaStatusConciliacao, HttpStatus.OK);
    }

    /**
     * Carrega as listas de visao
     *
     * @return
     */
    @GetMapping(value = "/listarVisao", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<VisaoGestaoVendasCA>> listarVisao() {
        List<VisaoGestaoVendasCA> listaVisaoGestaoVendasCA = visaoGestaoVendaService.listarTodos();
        return new ResponseEntity<>(listaVisaoGestaoVendasCA, HttpStatus.OK);
    }

    /**
     * Metodo para excluir itens selecionados
     *
     * @param gestaoVendasDTOs
     * @return
     */
    @PostMapping(value = "/excluir", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GestaoVendasDTO> excluir(@RequestBody List<GestaoVendasDTO> gestaoVendasDTOs) {

        LOGGER.info(Utils.getInicioMetodo());
        long detalhe = 0;

        try {

            for (GestaoVendasDTO dto : gestaoVendasDTOs) {
                detalhe = dto.getIdDetalhe();
                LOGGER.info("Excluindo: " + detalhe);
                RemessaConciliacaoDetalhe remessa = remessaConciliacaoService.findById(dto.getIdDetalhe());
                remessa.setFlgExclusao(Long.valueOf(1));
                remessa.setUserExclusao(UsuarioAutenticado.getLogin());
                remessaConciliacaoService.merge(remessa);
                LOGGER.info("Usuario " + remessa.getUserExclusao() + " excluiu detalhe: " + detalhe);
            }

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException("Detalhe " + detalhe + " não foi encontrado.");
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
