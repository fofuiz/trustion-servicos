package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.like;
import static br.com.accesstage.trustion.util.SpecificationUtils.in;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

public class EmpresaSpecification {

    public static Specification<Empresa> byCriterio(Empresa empresa, List<Long> idsGrupos, List<Long> idsEmpresas, Optional<Usuario> usuarioOptional) {
        return (Root<Empresa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            like(builder, root, predicates, "razaoSocial", empresa.getRazaoSocial());

            if(usuarioOptional.isPresent() || isNotEmpty(empresa.getEmpresaModeloNegocioList())) {
                Join<EmpresaModeloNegocio, Empresa> empresas = root.join("empresaModeloNegocioList").join("modeloNegocio");
                if(isNotEmpty(empresa.getEmpresaModeloNegocioList())) {
                    predicates.add(builder.equal(empresas.<String>get("idModeloNegocio"), empresa.getEmpresaModeloNegocioList().get(0).getModeloNegocio().getIdModeloNegocio()));
                }
                if(usuarioOptional.isPresent()) {
                    Usuario usuario = usuarioOptional.get();
                    Join<Transportadora, ModeloNegocio> modeloNegocioTransportadoraJoin = empresas.join("transportadora");

                    Join<Usuario, Transportadora> usuarioTransportadoraJoin = modeloNegocioTransportadoraJoin.join("usuarioList");
                    Path<Long> expr = usuarioTransportadoraJoin.<Long>get("idUsuario");
                    predicates.add(expr.in(usuario.getIdUsuario()));
                }

            }

            in(builder, root, predicates, "idEmpresa", idsEmpresas);
            
            equal(builder, root, predicates, "cnpj", empresa.getCnpj());
            equal(builder, root, predicates, "idGrupoEconomico", empresa.getIdGrupoEconomico());
            equal(builder, root, predicates, "status", empresa.getStatus());

            in(builder, root, predicates, "idGrupoEconomico", idsGrupos);

            return builder.and(predicates.toArray(new Predicate[]{}));
        };

    }


    public static Specification<Empresa> byCriterioModeloNegocio(Empresa empresa, List<Long> idsModeloNegocio) {
        return (Root<Empresa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            like(builder, root, predicates, "razaoSocial", empresa.getRazaoSocial());

            equal(builder, root, predicates, "cnpj", empresa.getCnpj());
            equal(builder, root, predicates, "idGrupoEconomico", empresa.getIdGrupoEconomico());
            equal(builder, root, predicates, "status", empresa.getStatus());

            if (isNotEmpty(empresa.getEmpresaModeloNegocioList())) {
                Join<EmpresaModeloNegocio, Empresa> empresas = root.join("empresaModeloNegocioList").join("modeloNegocio");
                predicates.add(builder.equal(empresas.<String>get("idModeloNegocio"), empresa.getEmpresaModeloNegocioList().get(0).getModeloNegocio().getIdModeloNegocio()));
            }

            if (isNotEmpty(idsModeloNegocio)) {
                Join<EmpresaModeloNegocio, Empresa> empresasId = root.join("empresaModeloNegocioList").join("modeloNegocio");
                Path<Long> expr = empresasId.<Long>get("idModeloNegocio");
                predicates.add(expr.in(idsModeloNegocio));
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
