package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.model.RelatorioOcorrenciaD0;

public class RelatorioOcorrenciaD0Specification {

    public static Specification<RelatorioOcorrenciaD0> byCriterioIds(RelatorioOcorrenciaDTO dto, List<Long> ids) {
        return new Specification<RelatorioOcorrenciaD0>() {

            @Override
            public Predicate toPredicate(Root<RelatorioOcorrenciaD0> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (!ids.isEmpty()) {
                    Path<RelatorioOcorrenciaD0> expr = root.<RelatorioOcorrenciaD0>get("row_number");
                    predicates.add(expr.in(ids));
                } else {

                    if (null != dto.getIdOcorrencia()) {
                        predicates.add(cb.equal(root.get("idOcorrencia"), dto.getIdOcorrencia()));
                        return cb.and(predicates.toArray(new Predicate[]{}));
                    }

                    if (null != dto.getIdsEmpresa() && !dto.getIdsEmpresa().isEmpty()) {
                        Path<RelatorioOcorrenciaD0> expr = root.<RelatorioOcorrenciaD0>get("idEmpresa");
                        predicates.add(expr.in(dto.getIdsEmpresa()));
                    }

                    if (null != dto.getIdGrupoEconomico() && null != dto.getIdGrupoEconomicoOutrasEmpresas()) {
                        predicates.add(cb.equal(root.get("idGrupoEconomico"), dto.getIdGrupoEconomico()));
                    }

                    if (null != dto.getIdGrupoEconomicoOutrasEmpresas()) {
                        predicates.add(cb.equal(root.get("idGrupoEconomicoOutrasEmpresas"), dto.getIdGrupoEconomicoOutrasEmpresas()));
                    }

                    if (null != dto.getIdModeloNegocio()) {
                        predicates.add(cb.equal(root.get("idModeloNegocio"), dto.getIdModeloNegocio()));
                    }

                    if (null != dto.getStatusOcorrencia()) {
                        predicates.add(cb.equal(root.get("statusOcorrencia"), dto.getStatusOcorrencia()));
                    }

                    if (null != dto.getDataInicial()) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("dataCriacao"), dto.getDataInicial()));
                    }

                    if (null != dto.getDataFinal()) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("dataCriacao"), dto.getDataFinal()));
                    }

                    if (null != dto.getResponsavel() && !"".equals(dto.getResponsavel())) {

                        predicates.add(cb.like(cb.lower(root.<String>get("responsavel")),
                                "%" + dto.getResponsavel().toLowerCase() + "%"));
                    }

                    if (null != dto.getCnpjCliente()) {
                        predicates.add(cb.equal(root.get("cnpjCliente"), dto.getCnpjCliente()));
                    }
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
    };

	public static Specification<RelatorioOcorrenciaD0> byCriterio(RelatorioOcorrenciaDTO dto) {
        return new Specification<RelatorioOcorrenciaD0>() {

            @Override
            public Predicate toPredicate(Root<RelatorioOcorrenciaD0> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (null != dto.getIdOcorrencia()) {
                    predicates.add(cb.equal(root.get("idOcorrencia"), dto.getIdOcorrencia()));
                    return cb.and(predicates.toArray(new Predicate[]{}));
                }

                if (null != dto.getIdsEmpresa() && !dto.getIdsEmpresa().isEmpty()) {
                    Path<RelatorioOcorrenciaD0> expr = root.<RelatorioOcorrenciaD0>get("idEmpresa");
                    predicates.add(expr.in(dto.getIdsEmpresa()));
                }

                if (null != dto.getIdGrupoEconomico() && null != dto.getIdGrupoEconomicoOutrasEmpresas()) {
                    predicates.add(cb.equal(root.get("idGrupoEconomico"), dto.getIdGrupoEconomico()));
                }

                //if (null != dto.getIdGrupoEconomicoOutrasEmpresas()) {
                  //  predicates.add(cb.equal(root.get("idGrupoEconomicoOutrasEmpresas"), dto.getIdGrupoEconomicoOutrasEmpresas()));
                //}

                if (null != dto.getIdModeloNegocio()) {
                    predicates.add(cb.equal(root.get("idModeloNegocio"), dto.getIdModeloNegocio()));
                }

                if (null != dto.getStatusOcorrencia()) {
                    predicates.add(cb.equal(root.get("statusOcorrencia"), dto.getStatusOcorrencia()));
                }

                if (null != dto.getDataInicial()) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("dataCriacao"), dto.getDataInicial()));
                }

                if (null != dto.getDataFinal()) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("dataCriacao"), dto.getDataFinal()));
                }

                if (null != dto.getResponsavel() && !"".equals(dto.getResponsavel())) {

                    predicates.add(cb.like(cb.lower(root.<String>get("responsavel")),
                            "%" + dto.getResponsavel().toLowerCase() + "%"));
                }

                if (null != dto.getCnpjCliente()) {
                    predicates.add(cb.equal(root.get("cnpjCliente"), dto.getCnpjCliente()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };

    }

}
