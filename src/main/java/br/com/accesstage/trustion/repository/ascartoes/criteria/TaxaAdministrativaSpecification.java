package br.com.accesstage.trustion.repository.ascartoes.criteria;

import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TaxaAdministrativaSpecification {

    private TaxaAdministrativaSpecification() {
        throw new IllegalAccessError("Specification class");
    }

    public static Specification<TaxaAdministrativa> byCriterio(TaxaAdministrativaDTO dto) {

        return (Root<TaxaAdministrativa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
