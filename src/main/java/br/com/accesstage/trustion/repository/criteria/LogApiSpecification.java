package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

import br.com.accesstage.trustion.dto.LogApiDTO;
import br.com.accesstage.trustion.model.LogApi;

import static br.com.accesstage.trustion.util.SpecificationUtils.*;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

public class LogApiSpecification {

    public static Specification<LogApi> byCriterio(LogApiDTO dto, List<Long> idsEmpresa) {
        return (Root<LogApi> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            equal(builder, root, predicates, "idGrupoEconomico", dto.getIdGrupoEconomico());
            equal(builder, root, predicates, "idCofre", dto.getIdCofre());
            equal(builder, root, predicates, "statusConsulta", dto.getStatusConsulta());

            if(dto.getIdEmpresa() != null) {
                equal(builder, root, predicates, "idEmpresa", dto.getIdEmpresa());
            }else{
                in(builder, root, predicates, "idEmpresa", idsEmpresa);
            }

            greaterThanOrEqualTo(builder, root, predicates, "dataLog", dto.getDataInicial());
            lessThanOrEqualTo(builder, root, predicates, "dataLog", dto.getDataFinal());


            return builder.and(predicates.toArray(new Predicate[]{}));
            };
        }


}
