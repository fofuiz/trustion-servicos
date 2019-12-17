package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import br.com.accesstage.trustion.dto.CategoriaDTO;
import br.com.accesstage.trustion.model.Categoria;

public class CategoriaSpecification {

    public static Specification<Categoria> byCriterio(CategoriaDTO dto) {
        return (Root<Categoria> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto.getIdTipoCategoria() != null && dto.getIdTipoCategoria().getIdTipoCategoria() > 0L) {
                predicates.add(builder.equal(root.<String>get("idTipoCategoria"), dto.getIdTipoCategoria()));
            }

            if (StringUtils.isNotBlank(dto.getDescricao())) {
                predicates.add(builder.like(builder.lower(root.<String>get("descricao")), "%" + dto.getDescricao().toLowerCase() + "%"));
            }

            if (StringUtils.isNotBlank(dto.getStatus())) {
                predicates.add(builder.equal(root.get("status"), dto.getStatus()));
            }

            return builder.and(predicates.toArray(new Predicate[] {}));
        };
    }

}
