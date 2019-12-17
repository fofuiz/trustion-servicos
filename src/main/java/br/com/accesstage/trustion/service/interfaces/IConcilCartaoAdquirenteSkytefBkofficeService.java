package br.com.accesstage.trustion.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.accesstage.trustion.model.ConcilCartaoAdquirenteSkytefBkoffice;

public interface IConcilCartaoAdquirenteSkytefBkofficeService {

	public void salvarTodos(List<ConcilCartaoAdquirenteSkytefBkoffice> concil);

	public void salvar(ConcilCartaoAdquirenteSkytefBkoffice concil);
	
	public Page<ConcilCartaoAdquirenteSkytefBkoffice> findAllRangeDate(LocalDate startDate, LocalDate endDate, Pageable paginacao);

	public boolean validDataProcessada(LocalDate data);
	
}
