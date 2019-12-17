package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.model.DadosBancarios;
import br.com.accesstage.trustion.model.ExtratoElegivel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ExtratoElegivelSpecification {

    private static final String ID_CONCILIACAO = "idConciliacao";
    
    private ExtratoElegivelSpecification() {
        throw new IllegalAccessError("Utility class");
    }

    public static Specification<ExtratoElegivel> byCriterioComRangeValorD1(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO, ModeloNegocioDTO modeloNegocioDTO, List<DadosBancarios> dadosBancarios) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (relatorioAnaliticoCreditoD1DTO != null) {

                if (relatorioAnaliticoCreditoD1DTO.getCnpj() != null && !relatorioAnaliticoCreditoD1DTO.getCnpj().isEmpty()) {
//                    predicates.add(cb.equal(root.get("cnpj"), relatorioAnaliticoCreditoD1DTO.getCnpj()));
                }

                if(dadosBancarios != null && !dadosBancarios.isEmpty()){
                    List<Integer> listBancos = new LinkedList<>(),listAgencias = new LinkedList<>(), listContas = new LinkedList<>();
                    for(DadosBancarios dadosBanco : dadosBancarios){
                        listBancos.add(dadosBanco.getIdBanco().intValue());
                        listAgencias.add(dadosBanco.getAgencia().intValue());
                        listContas.add(dadosBanco.getConta().intValue());
                    }
                    predicates.add(cb.in(root.get("codigoBanco")).value(listBancos));
                    predicates.add(cb.in(root.get("agencia")).value(listAgencias));
                    predicates.add(cb.in(root.get("conta")).value(listContas));
                }

                if (modeloNegocioDTO.getQuantidadeDiasConfCredito() != null) {
                    Calendar dataCreditoInicial = Calendar.getInstance();
                    dataCreditoInicial.setTime(relatorioAnaliticoCreditoD1DTO.getDataColeta());
                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, modeloNegocioDTO.getQuantidadeDiasConfCredito());

                    dataCreditoInicial.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoInicial.set(Calendar.MINUTE, 59);
                    dataCreditoInicial.set(Calendar.SECOND, 59);

                    Calendar dataCreditoFinal = Calendar.getInstance();
                    dataCreditoFinal.setTime(dataCreditoInicial.getTime());

                    dataCreditoFinal.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoFinal.set(Calendar.MINUTE, 59);
                    dataCreditoFinal.set(Calendar.SECOND, 59);

                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, -1);

                    predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancamento"), dataCreditoInicial.getTime()));

                }

                query = query.orderBy(cb.asc(root.get("dataLancamento")));

            }

            query = query.orderBy(cb.asc(root.get("dataLancamento")));

            predicates.add(cb.isNull(root.get(ID_CONCILIACAO)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<ExtratoElegivel> byCriterioComRangeValorD0(RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (relatorioAnaliticoCreditoDTO != null) {

                if (relatorioAnaliticoCreditoDTO.getCnpj() != null && !relatorioAnaliticoCreditoDTO.getCnpj().isEmpty()) {
//                    predicates.add(cb.equal(root.get("cnpj"), relatorioAnaliticoCreditoDTO.getCnpj()));
                }

                if (relatorioAnaliticoCreditoDTO.getDataCorte() != null) {
                    Calendar dataCreditoInicial = Calendar.getInstance();
                    dataCreditoInicial.setTime(relatorioAnaliticoCreditoDTO.getDataCorte());

                    dataCreditoInicial.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoInicial.set(Calendar.MINUTE, 59);
                    dataCreditoInicial.set(Calendar.SECOND, 59);

                    Calendar dataCreditoFinal = Calendar.getInstance();
                    dataCreditoFinal.setTime(dataCreditoInicial.getTime());

                    dataCreditoFinal.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoFinal.set(Calendar.MINUTE, 59);
                    dataCreditoFinal.set(Calendar.SECOND, 59);

                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, -1);

                    predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancamento"), dataCreditoInicial.getTime()));
                }

            }

            query = query.orderBy(cb.asc(root.get("dataLancamento")));

            predicates.add(cb.isNull(root.get(ID_CONCILIACAO)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<ExtratoElegivel> byCriterioSemRangeValorD1(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO, ModeloNegocioDTO modeloNegocioDTO, List<DadosBancarios> dadosBancarios) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (relatorioAnaliticoCreditoD1DTO != null) {

                if (relatorioAnaliticoCreditoD1DTO.getCnpj() != null && !relatorioAnaliticoCreditoD1DTO.getCnpj().isEmpty()) {
//                    predicates.add(cb.equal(root.get("cnpj"), relatorioAnaliticoCreditoD1DTO.getCnpj()));
                }

                if(dadosBancarios != null && !dadosBancarios.isEmpty()){
                    List<Integer> listBancos = new LinkedList<>(),listAgencias = new LinkedList<>(), listContas = new LinkedList<>();
                    for(DadosBancarios dadosBanco : dadosBancarios){
                        listBancos.add(dadosBanco.getIdBanco().intValue());
                        listAgencias.add(dadosBanco.getAgencia().intValue());
                        listContas.add(dadosBanco.getConta().intValue());
                    }
                    predicates.add(cb.in(root.get("codigoBanco")).value(listBancos));
                    predicates.add(cb.in(root.get("agencia")).value(listAgencias));
                    predicates.add(cb.in(root.get("conta")).value(listContas));
                }

                if (modeloNegocioDTO.getQuantidadeDiasConfCredito() != null) {
                    Calendar dataCreditoInicial = Calendar.getInstance();
                    dataCreditoInicial.setTime(relatorioAnaliticoCreditoD1DTO.getDataColeta());
                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, modeloNegocioDTO.getQuantidadeDiasConfCredito());

                    dataCreditoInicial.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoInicial.set(Calendar.MINUTE, 59);
                    dataCreditoInicial.set(Calendar.SECOND, 59);

                    Calendar dataCreditoFinal = Calendar.getInstance();
                    dataCreditoFinal.setTime(dataCreditoInicial.getTime());

                    dataCreditoFinal.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoFinal.set(Calendar.MINUTE, 59);
                    dataCreditoFinal.set(Calendar.SECOND, 59);

                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, -1);

                    predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancamento"), dataCreditoInicial.getTime()));
                }
            }

            query = query.orderBy(cb.asc(root.get("dataLancamento")));

            predicates.add(cb.isNull(root.get(ID_CONCILIACAO)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<ExtratoElegivel> byCriterioSemRangeValorD0(RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (relatorioAnaliticoCreditoDTO != null) {

                if (relatorioAnaliticoCreditoDTO.getCnpj() != null && !relatorioAnaliticoCreditoDTO.getCnpj().isEmpty()) {
//                    predicates.add(cb.equal(root.get("cnpj"), relatorioAnaliticoCreditoDTO.getCnpj()));
                }

                if (relatorioAnaliticoCreditoDTO.getDataCorte() != null) {
                    Calendar dataCreditoInicial = Calendar.getInstance();
                    dataCreditoInicial.setTime(relatorioAnaliticoCreditoDTO.getDataCorte());

                    dataCreditoInicial.set(Calendar.HOUR_OF_DAY, 23);
                    dataCreditoInicial.set(Calendar.MINUTE, 59);
                    dataCreditoInicial.set(Calendar.SECOND, 59);
                    dataCreditoInicial.add(Calendar.DAY_OF_MONTH, -1);

                    predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancamento"), dataCreditoInicial.getTime()));
                }
            }

            query = query.orderBy(cb.asc(root.get("dataLancamento")));

            predicates.add(cb.isNull(root.get(ID_CONCILIACAO)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
