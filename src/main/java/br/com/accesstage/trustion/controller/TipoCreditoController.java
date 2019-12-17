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

import br.com.accesstage.trustion.dto.TipoCreditoDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ITipoCreditoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@RestController
public class TipoCreditoController {

	@Autowired
	private ITipoCreditoService creditoService;

	@Log
	private Logger LOG;
	
	@RequestMapping(value = "/tiposCreditos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<TipoCreditoDTO>> listarTodos(){

		LOG.info(Utils.getInicioMetodo());

		List<TipoCreditoDTO> dtos = null;
		
		try {
			dtos = creditoService.listarTodos();
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de " + UTF8.creditos}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<Collection<TipoCreditoDTO>>(dtos, HttpStatus.OK);
	}
}
