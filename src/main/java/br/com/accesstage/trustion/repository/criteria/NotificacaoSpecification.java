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

import br.com.accesstage.trustion.dto.NotificacaoDTO;
import br.com.accesstage.trustion.model.Notificacao;
import br.com.accesstage.trustion.model.Usuario;

public class NotificacaoSpecification {
	public static Specification<Notificacao> byCriterio(NotificacaoDTO notificacao,List<Long> idsGrupo){
		return new Specification<Notificacao>() {

			@Override
			public Predicate toPredicate(Root<Notificacao> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				
				if(notificacao.getIdGrupoEconomico() != null && notificacao.getIdGrupoEconomico() > 0){
					predicates.add(builder.equal(root.<String>get("idGrupoEconomico"), notificacao.getIdGrupoEconomico()));
				}
				
				if(notificacao.getIdEmpresa() != null && notificacao.getIdEmpresa() > 0){
					predicates.add(builder.equal(root.<String>get("idEmpresa"), notificacao.getIdEmpresa()));
				}
				
				if(notificacao.getIdTipoNotificacao() != null && notificacao.getIdTipoNotificacao() > 0){
					predicates.add(builder.equal(root.<String>get("idTipoNotificacao"), notificacao.getIdTipoNotificacao()));
				}

				if(notificacao.getStatus() != null && !notificacao.getStatus().isEmpty()){
					predicates.add(builder.equal(root.get("status"), notificacao.getStatus()));
				}
				
				if(idsGrupo != null && !idsGrupo.isEmpty()){
					Path<Notificacao> expr = root.<Notificacao>get("idGrupoEconomico");
					predicates.add(expr.in(idsGrupo));
				}
						
				
				Join<Notificacao,Usuario> usuarios = root.join("usuario");
				
				if(notificacao.getLogin() != null && !notificacao.getLogin().equals("")){
					predicates.add(builder.like(builder.lower(usuarios.<String>get("login")), "%"+ notificacao.getLogin().toLowerCase() +"%"));
				}
				
				if(notificacao.getNome() != null && !notificacao.getNome().equals("")){
					predicates.add(builder.like(builder.lower(usuarios.<String>get("nome")), "%"+ notificacao.getNome().toLowerCase() +"%"));
				}
				
				if(notificacao.getEmail() != null && !notificacao.getEmail().equals("")) {
					predicates.add(builder.like(builder.lower(usuarios.<String>get("email")), "%"+ notificacao.getEmail().toLowerCase() +"%"));
				}
				
		        
				
		        
				return builder.and(predicates.toArray(new Predicate[]{}));
			}		
		};
	}
}
