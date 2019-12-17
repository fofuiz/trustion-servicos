package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import br.com.accesstage.trustion.dto.OcorrenciaD1DTO;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDNDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ConflictResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaD1Service;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;

@RestController
public class OcorrenciaD1Controller {
	
	@Autowired
	private IOcorrenciaD1Service ocorrenciaD1Service;

	@Log
	private Logger LOG;

	@RequestMapping(value = "/ocorrencia/d1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OcorrenciaD1DTO> criar(@Valid @RequestBody OcorrenciaD1DTO dto, BindingResult camposInvalidos){
		
		LOG.info(Utils.getInicioMetodo());

		OcorrenciaD1DTO dtoCriado = null;
		
		if(camposInvalidos.hasErrors()) {
			throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
		}
		
		try {
			dtoCriado = ocorrenciaD1Service.criar(dto);
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.Ocorrencia}));
		}

		LOG.info(Utils.getFimMetodo());
		
		return new ResponseEntity<>(dtoCriado, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ocorrencia/d1/{id}", method = RequestMethod.GET)
	public ResponseEntity<OcorrenciaD1DTO> pesquisar(@PathVariable("id") Long idOcorrencia){

		LOG.info(Utils.getInicioMetodo());

		OcorrenciaD1DTO dto = null;
		
		try {
			dto = ocorrenciaD1Service.pesquisar(idOcorrencia);
		} catch (ResourceNotFoundException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{UTF8.Ocorrencia}));
		}

		LOG.info(Utils.getFimMetodo());
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ocorrencia/d1/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OcorrenciaD1DTO> alterarStatus(@RequestBody OcorrenciaD1DTO dto){

		LOG.info(Utils.getInicioMetodo());

		OcorrenciaD1DTO dtoAlterado = null;
		
		try {
			dtoAlterado = ocorrenciaD1Service.alterarStatus(dto);
		} catch (ConflictResponseException | ResourceNotFoundException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;
		}catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{UTF8.Ocorrencia}));
		}

		LOG.info(Utils.getFimMetodo());
		
		return new ResponseEntity<>(dtoAlterado, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ocorrencias/d1/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<RelatorioOcorrenciaDNDTO> listarPorCriterio(@RequestBody RelatorioOcorrenciaDNDTO dto, Pageable pageable) {

		LOG.info(Utils.getInicioMetodo());

		Page<RelatorioOcorrenciaDNDTO> listaRelOcorrencias;

		try {
			listaRelOcorrencias = ocorrenciaD1Service.listarPorCriterio(dto, pageable);

		}catch (ForbiddenResponseException | ResourceNotFoundException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;
		}catch (InvalidStateException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.creditos}));
		}

		LOG.info(Utils.getFimMetodo());

		return listaRelOcorrencias;
	}
	
	@RequestMapping(value = "/ocorrencias/d1/criterios/exportar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<RelatorioOcorrenciaDNDTO>> listarPorCriterio(@RequestBody RelatorioOcorrenciaDNDTO dto) {

		LOG.info(Utils.getInicioMetodo());

		List<RelatorioOcorrenciaDNDTO> listaRelOcorrencias = null;

		try {
			listaRelOcorrencias = ocorrenciaD1Service.listarPorCriterioExportar(dto);

		}catch (ForbiddenResponseException | ResourceNotFoundException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;
		}catch (InvalidStateException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.creditos}));
		}

		LOG.info(Utils.getFimMetodo());

		  return new ResponseEntity<>(listaRelOcorrencias, HttpStatus.OK);
	}
	
}
