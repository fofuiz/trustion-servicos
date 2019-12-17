package br.com.accesstage.trustion.repository.ascartoes.criteria;

import br.com.accesstage.trustion.ascartoes.model.Parametros;
import br.com.accesstage.trustion.dto.ascartoes.ParametrosDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author raphael
 */
public class ParametrosSpecification {

    private ParametrosSpecification() {
        throw new IllegalAccessError("Specification class");
    }

    public static Specification<Parametros> byCriterio(ParametrosDTO dto) {

        return (Root<Parametros> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
