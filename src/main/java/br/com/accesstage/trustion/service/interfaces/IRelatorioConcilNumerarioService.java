package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.model.ConciliacaoVendasPac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IRelatorioConcilNumerarioService {

    public Page<ConciliacaoVendasPac> findAllWithFilters(Date startDate, Date endDate, Long gtv, Pageable paginacao);

}
