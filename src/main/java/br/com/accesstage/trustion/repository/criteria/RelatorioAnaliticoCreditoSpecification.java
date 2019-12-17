package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import org.springframework.data.jpa.domain.Specification;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.greaterThanOrEqualTo;
import static br.com.accesstage.trustion.util.SpecificationUtils.in;
import static br.com.accesstage.trustion.util.SpecificationUtils.lessThanOrEqualTo;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

public class RelatorioAnaliticoCreditoSpecification {

    private static final String NUM_SERIE = "numSerie";
    private static final String DATA_CORTE = "dataCorte";
    private static final String ID_EMPRESA = "idEmpresa";
    private static final String VALOR_QUESTIONADO = "valorQuestionado";
    private static final String ID_GRUPO_ECONOMICO = "idGrupoEconomico";

    private RelatorioAnaliticoCreditoSpecification() {
        throw new IllegalAccessError("Specification class");
    }

    public static Specification<RelatorioAnaliticoCredito> byCriterio(RelatorioAnaliticoCreditoDTO dto, List<Empresa> empresasPermissao, List<GrupoEconomico> gruposPermissao) {

        return new Specification<RelatorioAnaliticoCredito>() {

            @Override
            public Predicate toPredicate(Root<RelatorioAnaliticoCredito> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = new ArrayList<>();

                if (dto != null) {

                    if (dto.isRegistrosComDiferenca()) {
                        predicates.add(builder.isNotNull(root.get(VALOR_QUESTIONADO)));
                        predicates.add(builder.notEqual(root.get(VALOR_QUESTIONADO), Double.valueOf("0.00")));
                    }

                    if (isNotEmpty(dto.getTransportadoras())) {
                        List<Long> listaIds = new ArrayList<>();
                        dto.getTransportadoras().forEach(transportadora -> listaIds.add(transportadora.getIdTransportadora()));
                        in(builder, root, predicates, "idTransportadora", listaIds);
                    }

                    if (isNotEmpty(dto.getEmpresas())) {
                        List<Long> listaIds = new ArrayList<>();
                        dto.getEmpresas().forEach(empresa -> listaIds.add(empresa.getIdEmpresa()));
                        in(builder, root, predicates, ID_EMPRESA, listaIds);
                    }

                    if (isNotEmpty(dto.getCofres())) {
                        List<String> listaIdsNumSerie = new ArrayList<>();
                        dto.getCofres().forEach(cofre -> listaIdsNumSerie.add(cofre.getNumSerie()));
                        in(builder, root, predicates,  NUM_SERIE, listaIdsNumSerie);
                    }

                    equal(builder, root, predicates, "numSerie", dto.getNumSerie());
                    equal(builder, root, predicates, "cnpjCliente", dto.getCnpj());
                    equal(builder, root, predicates, "idOcorrencia", dto.getIdOcorrencia());
                    equal(builder, root, predicates, "statusOcorrencia", dto.getStatusOcorrencia());
                    equal(builder, root, predicates, "statusConciliacao", dto.getStatusConciliacao());

                    greaterThanOrEqualTo(builder, root, predicates, DATA_CORTE, dto.getDataInicial());
                    lessThanOrEqualTo(builder, root, predicates, DATA_CORTE, dto.getDataFinal());

                }

                if (isNotEmpty(empresasPermissao)) {
                    List<Long> idsEmpresas = new ArrayList<>();
                    empresasPermissao.forEach(empresa -> idsEmpresas.add(empresa.getIdEmpresa()));
                    in(builder, root, predicates, ID_EMPRESA, idsEmpresas);
                }

                if(isNotEmpty(dto.getOutrasEmpresas())){
                    List<Long> listaIds = new ArrayList<>();
                    dto.getOutrasEmpresas().forEach(grupo -> listaIds.add(grupo.getIdGrupoEconomico()));
                    in(builder, root, predicates, ID_GRUPO_ECONOMICO, listaIds);
                } else if (isNotEmpty(gruposPermissao)) {
                    List<Long> idsGrupos = new ArrayList<>();
                    gruposPermissao.forEach(grupoEconomico -> idsGrupos.add(grupoEconomico.getIdGrupoEconomico()));
                    in(builder, root, predicates, ID_GRUPO_ECONOMICO, idsGrupos);
                }

                query = query.orderBy(builder.asc(root.get(DATA_CORTE)));

                return builder.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

}
