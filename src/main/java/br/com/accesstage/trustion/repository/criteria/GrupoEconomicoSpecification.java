package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.GrupoEconomico;
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

public class GrupoEconomicoSpecification {

    public static Specification<GrupoEconomico> byCriterio(GrupoEconomico grupo, List<Long> idsGrupos) {
        return (Root<GrupoEconomico> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isNotBlank(grupo.getCnpj())) {
                predicates.add(builder.like(root.<String>get("cnpj"), grupo.getCnpj()));
            }
            if (isNotBlank(grupo.getNome())) {
                predicates.add(builder.like(builder.lower(root.<String>get("nome")), "%" + grupo.getNome().toLowerCase() + "%"));
            }
            if (grupo.getIdGrupoEconomico() != null && grupo.getIdGrupoEconomico() > 0L) {
                predicates.add(builder.equal(root.<String>get("idGrupoEconomico"), grupo.getIdGrupoEconomico()));
            }
            if (isNotEmpty(idsGrupos)) {
                Path<GrupoEconomico> expr = root.<GrupoEconomico>get("idGrupoEconomico");
                predicates.add(expr.in(idsGrupos));
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
