package br.com.accesstage.trustion.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import br.com.accesstage.trustion.model.TipoServico;

public class TipoServicoSpecification {

	public static Specification<TipoServico> byCriterio(TipoServico tipoServico,List<Long> idsGrupo){
		return new Specification<TipoServico>() {

			@Override
			public Predicate toPredicate(Root<TipoServico> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				
				if(tipoServico.getIdTipoServico() != null && tipoServico.getIdTipoServico() > 0){
					predicates.add(builder.equal(root.<String>get("idTipoServico"), tipoServico.getIdTipoServico()));
				}
				
				if(tipoServico.getNomeServico() != null && !tipoServico.getNomeServico().equals("")){
					predicates.add(builder.like(builder.lower(root.<String>get("nomeServico")), "%" + tipoServico.getNomeServico().toLowerCase() + "%"));
				}
				
				if(tipoServico.getIdGrupoEconomico() != null && tipoServico.getIdGrupoEconomico() > 0){
					predicates.add(builder.equal(root.<String>get("idGrupoEconomico"), tipoServico.getIdGrupoEconomico()));
				}
				
				if(idsGrupo != null && !idsGrupo.isEmpty()){
					Path<TipoServico> expr = root.<TipoServico>get("idGrupoEconomico");
					predicates.add(expr.in(idsGrupo));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
}
