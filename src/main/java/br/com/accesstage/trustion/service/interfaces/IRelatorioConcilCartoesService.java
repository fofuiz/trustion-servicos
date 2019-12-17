package br.com.accesstage.trustion.service.interfaces;

import java.util.Date;
import java.util.List;

import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import br.com.accesstage.trustion.model.BkOfficeDetalhe;
import br.com.accesstage.trustion.model.SkytefDetalhe;

public interface IRelatorioConcilCartoesService {

	public List<BkOfficeDetalhe> retornarTotalBandeiras(Date date);

	public List<AdquirenteDetalhe> retornarTotalBandeirasAdq(Date date);
	
	public List<SkytefDetalhe> retornarTotalBandeirasSky(Date date);

}
