package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.ModeloNegocio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.like;

public class ModeloNegocioSpecification {

    public static Specification<ModeloNegocio> byCriterio(ModeloNegocio modelo) {
        return (Root<ModeloNegocio> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            like(builder, root, predicates, "nome", modelo.getNome());

            equal(builder, root, predicates, "idModeloNegocio", modelo.getIdModeloNegocio());
            equal(builder, root, predicates, "idTipoCredito", modelo.getIdTipoCredito());
            equal(builder, root, predicates, "idTransportadora", modelo.getIdTransportadora());

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
