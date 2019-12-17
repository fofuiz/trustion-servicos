package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.AtividadeD1DTO;
import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IAtividadeD1Service;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;

@RestController
public class AtividadeD1Controller {
	
	@Autowired
	private IAtividadeD1Service atividadeD1Service;
        
	@Autowired
	private IAtividadeService atividadeService;        

	@Log
	private Logger LOG;
	
	@RequestMapping(value = "/atividade/d1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeDTO> criar(@Valid @RequestBody AtividadeDTO dto, BindingResult camposInvalidos){
		LOG.info(Utils.getInicioMetodo());

		AtividadeDTO dtoCriado = null;
		
		if(camposInvalidos.hasErrors()) {
			throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
		}
		
		try {
			dtoCriado = atividadeService.criar(dto);
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar"));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dtoCriado, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/atividade/d1/acao", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeD1DTO> criarPorAcao(@RequestBody AtividadeD1DTO dados){
		LOG.info(Utils.getInicioMetodo());

		AtividadeD1DTO dto = null;

		try {
			dto = atividadeD1Service.criarPorAcao(dados);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/atividades/d1/ocorrencia/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<AtividadeD1DTO>> pesquisarPorOcorrencia(@PathVariable("id") Long idOcorrencia){
		LOG.info(Utils.getInicioMetodo());

		List<AtividadeD1DTO> dtos = null;
		
		try {
			dtos = atividadeD1Service.listarPorOcorrencia(idOcorrencia);
		}  catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar"));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<Collection<AtividadeD1DTO>>(dtos, HttpStatus.OK);
	}
	
	
}
