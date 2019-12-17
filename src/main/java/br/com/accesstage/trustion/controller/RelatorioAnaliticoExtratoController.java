package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoFiltroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoExtratoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/relAnaliticoExtrato")
@RestController
public class RelatorioAnaliticoExtratoController {

    @Autowired
    private IRelatorioAnaliticoExtratoService relAnaliticoExtratoService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RelatorioAnaliticoExtratoDTO>> listarPorCriterio(@RequestBody RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO, BindingResult camposInvalidos, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());
        
        Page<RelatorioAnaliticoExtratoDTO> listaRelAnaliticoExtrato;
        
        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
        	listaRelAnaliticoExtrato = relAnaliticoExtratoService.listarCriterios(relAnaliticoExtratoFiltroDTO, pageable);
        	
        } catch (ForbiddenResponseException | ResourceNotFoundException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.creditos}));
        }

        LOGGER.info(Utils.getFimMetodo());
        
        return new ResponseEntity<>(listaRelAnaliticoExtrato, HttpStatus.OK);
    }
    

    @RequestMapping(value = "/exportar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RelatorioAnaliticoExtratoDTO> exportar(@RequestBody RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO) {
        LOGGER.info(Utils.getInicioMetodo());
        
        List<RelatorioAnaliticoExtratoDTO> listaRelatorioExtratoTodos;
        
        try {
        	listaRelatorioExtratoTodos = relAnaliticoExtratoService.exportar(relAnaliticoExtratoFiltroDTO);
        	
        } catch (ForbiddenResponseException | ResourceNotFoundException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.creditos}));
        }

        LOGGER.info(Utils.getFimMetodo());
        
        return listaRelatorioExtratoTodos;
    }

}
