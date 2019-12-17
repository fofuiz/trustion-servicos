package br.com.accesstage.trustion.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import br.com.accesstage.trustion.model.BkOfficeDetalhe;
import br.com.accesstage.trustion.model.SkytefDetalhe;
import br.com.accesstage.trustion.repository.ascartoes.impl.TotalAdquirentesRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioConcilCartoesRepository;
import br.com.accesstage.trustion.service.interfaces.IRelatorioConcilCartoesService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelatorioConcilCartoesService implements IRelatorioConcilCartoesService {

	@Autowired
	private IRelatorioConcilCartoesRepository relatorioConcilCartoesRepository;
	
	@Autowired
	private TotalAdquirentesRepository repository;

	@Override
	public List<BkOfficeDetalhe> retornarTotalBandeiras(Date date) {
		
		System.out.println("DATAAAAAAAAA: " + date);
		
		List<BkOfficeDetalhe> bkOfficeList = relatorioConcilCartoesRepository.retornarTotalBandeiras(date);
		
		return bkOfficeList;
	}

	@Override
	public List<AdquirenteDetalhe> retornarTotalBandeirasAdq(Date date) {
		List<AdquirenteDetalhe> vos = null;

		try {
			vos = repository.consultaTotalAdquirentes(date);
		} catch (Exception e) {
			log.error("Erro ao realizar o LOAD GESTAO VENDAS - " + e.getMessage());
		}
		
		return vos;
	}

	@Override
	public List<SkytefDetalhe> retornarTotalBandeirasSky(Date date) {
		List<SkytefDetalhe> vos = null;

		try {
			vos = repository.consultaTotalSkytef(date);
		} catch (Exception e) {
			log.error("Erro ao realizar o LOAD GESTAO VENDAS - " + e.getMessage());
		}
		
		return vos;
	}
	

	

}
