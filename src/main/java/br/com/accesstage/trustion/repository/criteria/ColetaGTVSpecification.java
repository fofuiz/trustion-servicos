package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.ColetaGTV;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import static br.com.accesstage.trustion.util.SpecificationUtils.*;

public class ColetaGTVSpecification {

    public static Specification<ColetaGTV> byCriterio(ColetaGTV dto) {
        return (Root<ColetaGTV> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            like(builder, root, predicates, "numSerie", dto.getNumSerie());
            equal(builder, root, predicates, "idEquipamento", dto.getIdEquipamento());
            greaterThanOrEqualTo(builder, root, predicates, "periodoInicialDt", dto.getPeriodoInicialDt());
            lessThanOrEqualTo(builder, root, predicates, "coletaDt", dto.getColetaDt());

            return and(builder, predicates);
        };

    }
}
