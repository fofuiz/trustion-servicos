package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.CofreDTO;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ICofreService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CofreController {

    @Autowired
    private ICofreService cofreService;

    @Log
    private static Logger LOGGER;

    @PostMapping(value = "/cofre", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CofreDTO> criar(@Valid @RequestBody CofreDTO cofreDTO, BindingResult camposInvalidos) {
        LOGGER.info(Utils.getInicioMetodo());
        CofreDTO dto;

        if (camposInvalidos.hasErrors()) {
            LOGGER.error(Mensagem.get(camposInvalidos));
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dto = cofreService.criar(cofreDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"Cofre"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/cofre", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CofreDTO> alterar(@Valid @RequestBody CofreDTO cofreDTO, BindingResult camposInvalidos) {
        LOGGER.info(Utils.getInicioMetodo());
        CofreDTO dto;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dto = cofreService.alterar(cofreDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"Cofre"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/cofre/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CofreDTO> pesquisar(@PathVariable("id") Long id) {
        LOGGER.info(Utils.getInicioMetodo());
        CofreDTO dto;

        try {
            dto = cofreService.pesquisar(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"Cofre"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/cofres/criterios/page", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<CofreDTO> listarSpec(@RequestBody CofreDTO cofreDTO, Pageable pageable) {
        LOGGER.info(Utils.getInicioMetodo());
        Page<CofreDTO> lstDTO;

        try {
            lstDTO = cofreService.listarCriterios(cofreDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Notificação"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return lstDTO;
    }

    @PostMapping(value = "/cofres/criterios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CofreDTO>> listarSpec(@RequestBody CofreDTO cofreDTO) {
        LOGGER.info(Utils.getInicioMetodo());
        List<CofreDTO> lstDTO;

        try {
            lstDTO = cofreService.listarCriterios(cofreDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Notificação"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(lstDTO);
    }

    @PostMapping(value = "/cofres/empresas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CofreDTO>> listarSpec(@RequestBody List<EmpresaDTO> listaEmpresaFiltroDTO) {
        LOGGER.info(Utils.getInicioMetodo());
        List<CofreDTO> lstDTO;

        try {
            lstDTO = cofreService.listarCriterios(listaEmpresaFiltroDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Notificação"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(lstDTO);
    }
}
