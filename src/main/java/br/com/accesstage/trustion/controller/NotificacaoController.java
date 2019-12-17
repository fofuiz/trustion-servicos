package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.NotificacaoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.INotificacaoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@RestController
public class NotificacaoController {

    @Autowired
    private INotificacaoService notificacaoService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/notificacao", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificacaoDTO> criar(@Valid @RequestBody NotificacaoDTO notificacaoDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        NotificacaoDTO dto;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            dto = notificacaoService.criar(notificacaoDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/notificacao", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificacaoDTO> alterar(@Valid @RequestBody NotificacaoDTO notificacaoDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        NotificacaoDTO dto;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            dto = notificacaoService.alterar(notificacaoDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/notificacao/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificacaoDTO> pesquisar(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        NotificacaoDTO dto;

        try {

            dto = notificacaoService.pesquisar(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/notificacoes/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<NotificacaoDTO> listarSpec(@RequestBody NotificacaoDTO notificacaoDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<NotificacaoDTO> lstNotificacaoDTO;

        try {

            lstNotificacaoDTO = notificacaoService.listarCriterios(notificacaoDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return lstNotificacaoDTO;
    }

    @RequestMapping(value = "/notificacoes/criterios", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<NotificacaoDTO>> listarSpec(@RequestBody NotificacaoDTO notificacaoDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<NotificacaoDTO> lstNotificacaoDTO;

        try {

            lstNotificacaoDTO = notificacaoService.listarCriterios(notificacaoDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lstNotificacaoDTO);
    }

    @RequestMapping(value = "/notificacao/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<NotificacaoDTO> excluir(@PathVariable Long id) {
        
        LOGGER.info(Utils.getInicioMetodo());

        try {

            notificacaoService.excluir(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{UTF8.Notificacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
