package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.model.Transportadora;

import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.in;
import static br.com.accesstage.trustion.util.SpecificationUtils.like;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

public class UsuarioSpecification {

    public static Specification<Usuario> byCriterio(Usuario usuario, List<Long> listaIdPerfil, List<Long> idsGrupos, List<Long> idsTransportadora) {

        return (Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            like(builder, root, predicates, "nome", usuario.getNome());
            like(builder, root, predicates, "email", usuario.getEmail());
            like(builder, root, predicates, "login", usuario.getLogin());

            equal(builder, root, predicates, "idPerfil", usuario.getIdPerfil());
            in(builder, root, predicates, "idPerfil", listaIdPerfil);

            if (isNotEmpty(idsGrupos)) {
                Join<Empresa, Usuario> empresas = root.join("empresaList");
                Path<Long> expr = empresas.get("idGrupoEconomico");
                predicates.add(expr.in(idsGrupos));
            }

            if (isNotEmpty(usuario.getTransportadoraList())) {
                Join<Transportadora, Usuario> transportadoras = root.join("transportadoraList");
                predicates.add(builder.equal(transportadoras.<String>get("idTransportadora"), usuario.getTransportadoraList().get(0).getIdTransportadora()));
            }

            if (isNotEmpty(idsTransportadora)) {
                Join<Transportadora, Usuario> transportadorasId = root.join("transportadoraList");
                Path<Long> expr = transportadorasId.<Long>get("idTransportadora");
                predicates.add(expr.in(idsTransportadora));
            }

            if (isNotEmpty(usuario.getEmpresaList())) {
                Join<Empresa, Usuario> empresas = root.join("empresaList");
                predicates.add(builder.equal(empresas.<String>get("idEmpresa"), usuario.getEmpresaList().get(0).getIdEmpresa()));
            }

            query.distinct(true);

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
