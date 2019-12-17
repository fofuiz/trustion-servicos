package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.StringHistoricoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IStringHistoricoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class StringHistoricoController {

    @Autowired
    private IStringHistoricoService stringHistoricoService;

    @Log
    private static Logger LOGGER;

    @PostMapping(value = "/stringHistorico", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringHistoricoDTO> criar(@RequestBody @Valid StringHistoricoDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        if (Utils.validarString(dto.getTexto())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.valido2", new Object[]{"string"}));
        }

        StringHistoricoDTO dtoCriado;

        try {
            dtoCriado = stringHistoricoService.criar(dto);
        } catch (ForbiddenResponseException | BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"string"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtoCriado, HttpStatus.CREATED);
    }

    @PutMapping(value = "/stringHistorico", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringHistoricoDTO> alterar(@RequestBody @Valid StringHistoricoDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        if (dto.getIdStringHistorico() == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.param.obrigatorio", new String[]{"Id da String"}));
        }

        StringHistoricoDTO dtoAlterado;

        try {
            dtoAlterado = stringHistoricoService.alterar(dto);
        } catch (ForbiddenResponseException | BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"string"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtoAlterado);
    }

    @PostMapping(value = "/stringHistoricos/criterio", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StringHistoricoDTO>> pesquisar(@RequestBody StringHistoricoDTO dto, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<StringHistoricoDTO> dtos;

        try {
            dtos = stringHistoricoService.pesquisar(dto, pageable);
        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"string"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/stringHistorico/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringHistoricoDTO> pesquisar(@PathVariable("id") Long idStringHistorico) {

        LOGGER.info(Utils.getInicioMetodo());

        StringHistoricoDTO dto;

        try {
            dto = stringHistoricoService.pesquisar(idStringHistorico);
        } catch (ForbiddenResponseException | BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{"string"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }
}
