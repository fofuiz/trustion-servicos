package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.DetalheRelatorioDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IDetalheRelatorioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@RestController
public class DetalheRelatorioController {

	@Autowired
	private IDetalheRelatorioService detalheRelatorioService;

	@Log
	private Logger LOG;


	@RequestMapping(value = "/detalheRelatorio/fechamento/{codigoFechamento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<DetalheRelatorioDTO>> listarPorCodigoFechamento(@PathVariable("codigoFechamento") Long codigoFechamento) {

		LOG.info(Utils.getInicioMetodo());

		List<DetalheRelatorioDTO> listaDetalheRelatorioDTO;

		try {
			listaDetalheRelatorioDTO = detalheRelatorioService.listarPorCodigoFechamento(codigoFechamento);



		} catch (BadRequestResponseException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new BadRequestResponseException(e.getMessage());

		}catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos " + UTF8.Economicos}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<Collection<DetalheRelatorioDTO>>(listaDetalheRelatorioDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/detalheRelatorio/fechamento/{codigoFechamento}/{numSerie}/criterios/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DetalheRelatorioDTO> listarPorCodigoFechamentoPaginado(@PathVariable("codigoFechamento") Long codigoFechamento,@PathVariable("numSerie") String numSerie, Pageable pageable) {

		LOG.info(Utils.getInicioMetodo());

		Page<DetalheRelatorioDTO> listaDetalheRelatorioDTO;

		try {			
			listaDetalheRelatorioDTO = detalheRelatorioService.listarPorCodigoFechamento(codigoFechamento,numSerie,pageable);
			
		} catch (BadRequestResponseException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new BadRequestResponseException(e.getMessage());

		}catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos " + UTF8.Economicos}));
		}

		LOG.info(Utils.getFimMetodo());


		return listaDetalheRelatorioDTO;
	}
}
