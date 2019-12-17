package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OcorrenciaController {

    @Autowired
    private IOcorrenciaService ocorrenciaService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/ocorrencia", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> criar(@Valid @RequestBody OcorrenciaDTO ocorrenciaDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dto = ocorrenciaService.criar(ocorrenciaDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/ocorrencia", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> alterar(@Valid @RequestBody OcorrenciaDTO ocorrenciaDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.alterar(dto);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/ocorrencia/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> pesquisar(@PathVariable("id") Long idOcorrencia) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.pesquisar(idOcorrencia);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/ocorrencias", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OcorrenciaDTO>> listarTodos() {

        LOGGER.info(Utils.getInicioMetodo());

        List<OcorrenciaDTO> dtos = null;

        try {
            dtos = ocorrenciaService.listarTodos();
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(value = "/ocorrencia/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> alterarStatus(@RequestBody OcorrenciaDTO ocorrenciaDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.alterarStatus(ocorrenciaDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"o status da " + UTF8.ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/ocorrencias/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<RelatorioOcorrenciaDTO> listarPorCriterio(@RequestBody RelatorioOcorrenciaDTO dto, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<RelatorioOcorrenciaDTO> listaRelOcorrencias;

        try {
            listaRelOcorrencias = ocorrenciaService.listarPorCriterio(dto, pageable);

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

        return listaRelOcorrencias;
    }

    @RequestMapping(value = "/ocorrencias/criterios/exportar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<RelatorioOcorrenciaDTO>> listarPorCriterio(@RequestBody RelatorioOcorrenciaDTO dto) {

        LOGGER.info(Utils.getInicioMetodo());

        List<RelatorioOcorrenciaDTO> listaRelOcorrencias = null;

        try {
            listaRelOcorrencias = ocorrenciaService.listarPorCriterioExportar(dto);

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

        return ResponseEntity.ok(listaRelOcorrencias);
    }

}
