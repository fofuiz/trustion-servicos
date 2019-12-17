package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditoriaController {

    @Autowired
    private IAuditoriaService auditoriaService;

    @Log
    private Logger LOG;

    @RequestMapping(value = "/auditorias/criterio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AuditoriaDTO>> listarSpecs(@Valid @RequestBody AuditoriaDTO dto, BindingResult camposInvalidos) {
        LOG.info(Utils.getInicioMetodo());

        List<AuditoriaDTO> dtos;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dtos = auditoriaService.listarPorUsuario(dto);
        } catch (Exception e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Registros de Auditoria"}));
        }
        LOG.info(Utils.getFimMetodo());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/auditorias/criterio/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AuditoriaDTO>> listarSpecsPage(@Valid @RequestBody AuditoriaDTO dto, BindingResult camposInvalidos, Pageable pageable) {
        LOG.info(Utils.getInicioMetodo());

        Page<AuditoriaDTO> dtos;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dtos = auditoriaService.listarPorUsuario(dto, pageable);
        } catch (Exception e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Registros de Auditoria"}));
        }

        LOG.info(Utils.getFimMetodo());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}