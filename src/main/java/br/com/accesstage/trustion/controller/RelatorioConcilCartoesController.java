package br.com.accesstage.trustion.controller;

import java.util.Date;
import java.util.List;

import br.com.accesstage.trustion.dto.ConcilCartaoAdquirenteSkytefBkofficeDTO;
import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import br.com.accesstage.trustion.model.SkytefDetalhe;
import br.com.accesstage.trustion.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.accesstage.trustion.form.FiltroRelatorioConcilCartoesForm;
import br.com.accesstage.trustion.model.BkOfficeDetalhe;
import br.com.accesstage.trustion.model.ConcilCartaoAdquirenteSkytefBkoffice;
import br.com.accesstage.trustion.service.interfaces.IConcilCartaoAdquirenteSkytefBkofficeService;
import br.com.accesstage.trustion.service.interfaces.IRelatorioConcilCartoesService;

@RestController
@RequestMapping("/relConciliacaoCartoes")
@Slf4j
public class RelatorioConcilCartoesController {

	@Autowired
	private IRelatorioConcilCartoesService relatorioConcilCartoesService;
	
	@Autowired
	private IConcilCartaoAdquirenteSkytefBkofficeService concilCartaoAdquirenteSkytefBkofficeService;

	@PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ConcilCartaoAdquirenteSkytefBkofficeDTO>> listarTotalBandeiras(
			@RequestBody FiltroRelatorioConcilCartoesForm form, Pageable paginacao) {

		try {
			List<Date> datasBuscar = Utils.getDatesBetweenAndZeroTime(form.getDataInicial(), form.getDataFinal());

			// Processamento por data
			for (Date localDate : datasBuscar) {
				log.info("Data Processada: " + localDate);

				// 1. Verifico se tem a data em nossa base
				Boolean validData = this.concilCartaoAdquirenteSkytefBkofficeService.validDataProcessada(Utils.getConvertDateToLocalDate(localDate));

				// Se NÃO tem eu busco meus relatórios para salvar em nossa base
				if (!validData){

					// BUSCA BKOFFICE
					List<BkOfficeDetalhe> bkOffices = relatorioConcilCartoesService.retornarTotalBandeiras(localDate);

					// BUSCO SKYTEF
					List<SkytefDetalhe> skytefs = relatorioConcilCartoesService.retornarTotalBandeirasSky(localDate);

					// BUSCA ADQUIRENTE
					List<AdquirenteDetalhe> adquirentes = relatorioConcilCartoesService.retornarTotalBandeirasAdq(localDate);

					//Validar se as LISTAS todas vieram DADOS, se SIM eu salvo
					if (!bkOffices.isEmpty() && !adquirentes.isEmpty()){
						ConcilCartaoAdquirenteSkytefBkoffice concilAtual = new ConcilCartaoAdquirenteSkytefBkoffice(Utils.getConvertDateToLocalDate(localDate));

						bkOffices.forEach( (b) -> {
							b.setConcilCartaoBkoffice(concilAtual);
						});

						adquirentes.forEach( (a) -> {
							a.setConcilCartaoAdquirente(concilAtual);
						});

						skytefs.forEach( (s) -> {
							s.setConcilCartaoSkytef(concilAtual);
						});

						concilAtual.setBkofficeDetalhes(bkOffices);
						concilAtual.setAdquirenteDetalhes(adquirentes);
						concilAtual.setSkytefDetalhes(skytefs);

						//Salvo o Concil completo
						this.concilCartaoAdquirenteSkytefBkofficeService.salvar(concilAtual);
					}
				}
			}
			
			return ResponseEntity.ok(ConcilCartaoAdquirenteSkytefBkofficeDTO.converter(
					buscarConcilCartoesPorRange(
							form.getDataInicial(),
							form.getDataFinal(),
							paginacao)));
		} catch (Exception e) {
			log.error("Error in Method listarTotalBandeiras in Class RelatorioConcilCartoesController" + e);
		}
		return ResponseEntity.badRequest().build();//400
	}

	private Page<ConcilCartaoAdquirenteSkytefBkoffice> buscarConcilCartoesPorRange(Date dataInicial, Date dataFinal, Pageable paginacao) {
		return this.concilCartaoAdquirenteSkytefBkofficeService.findAllRangeDate(
				Utils.getConvertDateToLocalDate(dataInicial),
				Utils.getConvertDateToLocalDate(dataFinal),
				paginacao);
	}

}
