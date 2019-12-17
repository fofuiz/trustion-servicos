package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.like;

public class TransportadoraSpecification {

    public static Specification<Transportadora> byCriterio(Transportadora transportadora) {

        return (Root<Transportadora> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            like(builder, root, predicates, "razaoSocial", transportadora.getRazaoSocial());
            like(builder, root, predicates, "cnpj", transportadora.getCnpj());
            like(builder, root, predicates, "status", transportadora.getStatus());

            equal(builder, root, predicates, "idUsuarioCriacao", transportadora.getIdUsuarioCriacao());
            equal(builder, root, predicates, "idTransportadora", transportadora.getIdTransportadora());

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Transportadora> byUsuarioTransp(Long idUsuario) {

        return (Root<Transportadora> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            Join<Usuario,Transportadora> usuarioTransportadoraJoin = root.join("usuarioList");

            Path<Long> expr = usuarioTransportadoraJoin.<Long>get("idUsuario");
            predicates.add(expr.in(idUsuario));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Transportadora> byUsuarioEmpr(Long idUsuario) {

        return (Root<Transportadora> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
             query.distinct(true);
            Join<ModeloNegocio, Transportadora> negocioTransportadoraJoin = root.join("modeloNegocioList");
            Join<EmpresaModeloNegocio, ModeloNegocio> empresaModeloNegocio = negocioTransportadoraJoin.join("empresaModeloNegocioCollection");

            Join<Empresa, ModeloNegocio> empresaModeloNegocioJoin = empresaModeloNegocio.join("empresa");

            Join<Usuario, Transportadora> usuarioTransportadoraJoin = empresaModeloNegocioJoin.join("usuarioList");
            Path<Long> expr = usuarioTransportadoraJoin.<Long>get("idUsuario");
            predicates.add(expr.in(idUsuario));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
