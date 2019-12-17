package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.accesstage.trustion.model.ConciliacaoVendasPac;
import br.com.accesstage.trustion.service.interfaces.IRelatorioConcilNumerarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RelatorioConcilNumerarioService implements IRelatorioConcilNumerarioService {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<ConciliacaoVendasPac> findAllWithFilters(Date startDate, Date endDate, Long gtv, Pageable paginacao) {
		
		//Criteria Builder e Query
		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
	    CriteriaQuery<ConciliacaoVendasPac> cQuery = cBuilder.createQuery(ConciliacaoVendasPac.class);
	    
	    //Root - From
	    Root<ConciliacaoVendasPac> root = cQuery.from(ConciliacaoVendasPac.class);
	    
	    // Lista de Predicate's e Order's
	    List<Predicate> predicates = new ArrayList<Predicate>();
	    List<Order> orderList = new ArrayList<Order>();
	    
	    //Path's
	    Path<String> gtvPath = root.<String>get("numeroGtv");
	    Path<Date> dataGtvPath = root.<Date>get("dataConciliacao");
	    
	    //Filtro -> Datas -> Obrigatórias
	    if (startDate != null && endDate != null) {
	    	Predicate dateIni = cBuilder.greaterThanOrEqualTo(dataGtvPath, startDate);
	    	Predicate dateFim = cBuilder.lessThanOrEqualTo(dataGtvPath, endDate);
	    	predicates.add(dateIni);
	    	predicates.add(dateFim);
	    }

	    //Filtro -> GTV
	    if (gtv != null) {
	        Predicate gtvIgual = cBuilder.equal(gtvPath, gtv);
	        predicates.add(gtvIgual);
	    }
		
	    //Select - step1
	    cQuery.select(root);
	    
	    //Where - step2
		cQuery.where((Predicate[]) predicates.toArray(new Predicate[0]));

	    //Order By - step3 (optional)
	    orderList.add(cBuilder.desc(root.<Date>get("dataConciliacao")));
	    cQuery.orderBy(orderList);
	    
	    //Criação da query para TypedQuery
	    TypedQuery<ConciliacaoVendasPac> typedQuery = em.createQuery(cQuery);
	    
	    //Paginação
	    int totalRows = typedQuery.getResultList().size();
	    typedQuery.setFirstResult(paginacao.getPageNumber() * paginacao.getPageSize());
	    typedQuery.setMaxResults(paginacao.getPageSize());
	    
	    //Retorno
	    Page<ConciliacaoVendasPac> resultQueryPage = new PageImpl<ConciliacaoVendasPac>(typedQuery.getResultList(), paginacao, totalRows);
		
		return resultQueryPage;
	}
	

	
	
}
