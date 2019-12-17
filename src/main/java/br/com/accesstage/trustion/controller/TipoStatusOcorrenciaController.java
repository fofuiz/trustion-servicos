package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.TipoStatusOcorrenciaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ITipoStatusOcorrenciaService;
import br.com.accesstage.trustion.util.Mensagem;

@RestController
public class TipoStatusOcorrenciaController {

	@Autowired
	private ITipoStatusOcorrenciaService tipoStatusOcorrenciaService;

	@Log
	private Logger LOG;

	@RequestMapping(value = "/tiposStatusOcorrencias", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<TipoStatusOcorrenciaDTO>> listarTodos() {

		LOG.info(Utils.getInicioMetodo());

		List<TipoStatusOcorrenciaDTO> dtos = null;

		try {
			dtos = tipoStatusOcorrenciaService.listarTodos();

		} catch (BadRequestResponseException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;

		} catch(Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de status"}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/tiposStatusOcorrenciasReabertura", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<TipoStatusOcorrenciaDTO>> listarReabertura() {

		LOG.info(Utils.getInicioMetodo());

		List<TipoStatusOcorrenciaDTO> dtos = null;

		try {
			dtos = tipoStatusOcorrenciaService.listarReabertura();

		} catch (BadRequestResponseException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;

		} catch(Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de status"}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}
