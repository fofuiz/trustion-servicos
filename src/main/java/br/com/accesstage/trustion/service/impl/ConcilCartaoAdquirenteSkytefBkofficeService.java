package br.com.accesstage.trustion.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.model.ConcilCartaoAdquirenteSkytefBkoffice;
import br.com.accesstage.trustion.repository.interfaces.IConcilCartaoAdquirenteSkytefBkofficeRepository;
import br.com.accesstage.trustion.service.interfaces.IConcilCartaoAdquirenteSkytefBkofficeService;

@Service
public class ConcilCartaoAdquirenteSkytefBkofficeService implements IConcilCartaoAdquirenteSkytefBkofficeService {

	@Autowired
	private IConcilCartaoAdquirenteSkytefBkofficeRepository concilCartaoAdquirenteSkytefBkofficeRepository;

	@Override
	public void salvarTodos(List<ConcilCartaoAdquirenteSkytefBkoffice> concil) {
		this.concilCartaoAdquirenteSkytefBkofficeRepository.save(concil);
	}

	@Override
	public void salvar(ConcilCartaoAdquirenteSkytefBkoffice concil) {
		this.concilCartaoAdquirenteSkytefBkofficeRepository.save(concil);
	}

	@Override
	public Page<ConcilCartaoAdquirenteSkytefBkoffice> findAllRangeDate(LocalDate startDate, LocalDate endDate, Pageable paginacao) {
		return this.concilCartaoAdquirenteSkytefBkofficeRepository.findAllRangeDate(startDate, endDate, paginacao);
	}

	@Override
	public boolean validDataProcessada(LocalDate data) {
		try {
			ConcilCartaoAdquirenteSkytefBkoffice concil = this.concilCartaoAdquirenteSkytefBkofficeRepository.findByDataConcil(data);
			return (concil == null) ? false : true;
		} catch (NoSuchElementException exception){
			return false;
		}
	}


}
