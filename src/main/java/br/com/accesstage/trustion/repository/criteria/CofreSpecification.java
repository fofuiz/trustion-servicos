package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.Cofre;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import static br.com.accesstage.trustion.util.Utils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CofreSpecification {

    public static Specification<Cofre> byCriterio(Cofre cofre, List<Long> idsGrupo) {
        return (Root<Cofre> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cofre.getIdEquipamento() != null && cofre.getIdEquipamento() > 0L) {
                predicates.add(builder.equal(root.<String>get("idEquipamento"), cofre.getIdEquipamento()));
            }

            if (isNotBlank(cofre.getNumSerie())) {
                predicates.add(builder.like(builder.lower(root.<String>get("numSerie")), "%" + cofre.getNumSerie().toLowerCase() + "%"));
            }

            if (cofre.getIdGrupoEconomico() != null && cofre.getIdGrupoEconomico() > 0L) {
                predicates.add(builder.equal(root.<String>get("idGrupoEconomico"), cofre.getIdGrupoEconomico()));
            }

            if (cofre.getIdEmpresa() != null && cofre.getIdEmpresa() > 0L) {
                predicates.add(builder.equal(root.<String>get("idEmpresa"), cofre.getIdEmpresa()));
            }

            if (cofre.getIdModeloNegocio() != null && cofre.getIdModeloNegocio() > 0L) {
                predicates.add(builder.equal(root.<String>get("idModeloNegocio"), cofre.getIdModeloNegocio()));
            }

            if (isNotBlank(cofre.getStatus())) {
                predicates.add(builder.equal(root.<String>get("status"), cofre.getStatus()));
            }

            if (isNotEmpty(idsGrupo)) {
                Path<Cofre> expr = root.<Cofre>get("idGrupoEconomico");
                predicates.add(expr.in(idsGrupo));
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Cofre> byCriterio(List<Long> listaIdsEmpresa) {

        return (Root<Cofre> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isNotEmpty(listaIdsEmpresa)) {
                Path<Cofre> expr = root.<Cofre>get("idEmpresa");
                predicates.add(expr.in(listaIdsEmpresa));
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
