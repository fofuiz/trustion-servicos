package br.com.accesstage.trustion.controller;

import java.util.ArrayList;
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

import br.com.accesstage.trustion.dto.PerfilDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IPerfilService;
import br.com.accesstage.trustion.util.Mensagem;

@RestController
public class PerfilController {

	@Autowired
	private IPerfilService perfilService;

	@Log
	private Logger LOG;
	
	@RequestMapping(value="/perfisCadastro", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PerfilDTO>> listarCadastro() {

		LOG.info(Utils.getInicioMetodo());

		List<PerfilDTO> listaPerfil = new ArrayList<>();

		try {
			listaPerfil = perfilService.listarTodosCadastro();

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(
					Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"perfis"}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<>(listaPerfil, HttpStatus.OK);
	}
	
	@RequestMapping(value="/perfisPesquisa", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PerfilDTO>> listarPesquisa() {

		LOG.info(Utils.getInicioMetodo());

		List<PerfilDTO> listaPerfil = new ArrayList<>();

		try {
			listaPerfil = perfilService.listarTodosPesquisa();

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(
					Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"perfis"}));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<>(listaPerfil, HttpStatus.OK);
	}
}
