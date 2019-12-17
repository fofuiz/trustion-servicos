package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MesclarOcorrenciaController {

    @Autowired
    private IOcorrenciaService ocorrenciaService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/mescla/{ocorrencia}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OcorrenciaDTO>> listarTodosParaMescla(@PathVariable("ocorrencia") Long ocorrencia) {

        LOGGER.info(Utils.getInicioMetodo());

        List<OcorrenciaDTO> dtos = null;

        try {
            dtos = ocorrenciaService.listarParaMescla(ocorrencia);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(value = "/mescla", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> mesclar(@Valid @RequestBody OcorrenciaDTO ocorrenciaDTO,
            BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dto = ocorrenciaService.mesclar(ocorrenciaDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/mescla/desfaz", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> desfaz(@Valid @RequestBody OcorrenciaDTO ocorrenciaDTO,
            BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.desmesclar(ocorrenciaDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/mesclas/{ocorrencia}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OcorrenciaDTO>> listarMesclados(@PathVariable("ocorrencia") Long ocorrencia) {

        LOGGER.info(Utils.getInicioMetodo());

        List<OcorrenciaDTO> dtos = null;

        try {
            dtos = ocorrenciaService.listarMesclados(ocorrencia);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(value = "/mescla/aprovar/{ocorrencia}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> aprovarMescla(@PathVariable("ocorrencia") Long ocorrencia) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.aprovarMescla(ocorrencia);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.aprovar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/mescla/rejeitar/{ocorrencia}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OcorrenciaDTO> rejeitarMescla(@PathVariable("ocorrencia") Long ocorrencia) {

        LOGGER.info(Utils.getInicioMetodo());

        OcorrenciaDTO dto = null;

        try {
            dto = ocorrenciaService.rejeitarMescla(ocorrencia);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.aprovar", new Object[]{UTF8.Ocorrencia}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

}
