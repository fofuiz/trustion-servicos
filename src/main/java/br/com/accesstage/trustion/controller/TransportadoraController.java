package br.com.accesstage.trustion.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
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

import br.com.accesstage.trustion.dto.TransportadoraDTO;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ITransportadoraService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.caelum.stella.validation.InvalidStateException;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TransportadoraController {

    @Autowired
    private ITransportadoraService transportadoraService;

    @Log
    private static Logger LOGGER;

    private static final String TRANSPORTADORA = "transportadora";
    private static final String PARAM_TIPO_CREDITO_D0 = "D0";

    @RequestMapping(value = "/transportadora", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportadoraDTO> criar(@Valid @RequestBody TransportadoraDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        TransportadoraDTO transportadoraCriadaDTO = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            transportadoraCriadaDTO = transportadoraService.criar(dto);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(transportadoraCriadaDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/transportadora", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportadoraDTO> alterar(@Valid @RequestBody TransportadoraDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        TransportadoraDTO transportadoraAlteradaDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            transportadoraAlteradaDTO = transportadoraService.alterar(dto);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(transportadoraAlteradaDTO);
    }

    @RequestMapping(value = "/transportadora/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TransportadoraDTO> exluir(@PathVariable Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        try {
            transportadoraService.excluir(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.excluir", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/transportadoras", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TransportadoraDTO>> listar() {

        LOGGER.info(Utils.getInicioMetodo());

        List<TransportadoraDTO> lista = new ArrayList<>();

        try {
            lista = transportadoraService.listarTodos();

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lista);
    }

    @RequestMapping(value = "/transportadora/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportadoraDTO> listar(@PathVariable Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        TransportadoraDTO t = new TransportadoraDTO();

        try {
            t = transportadoraService.pesquisar(id);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = "/transportadoras/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TransportadoraDTO> listarPorCriterio(@RequestBody TransportadoraDTO dto, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<TransportadoraDTO> lista;

        try {
            lista = transportadoraService.listarCriterios(dto, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return lista;
    }

    @RequestMapping(value = "/transportadoras/criterios", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TransportadoraDTO>> listarPorCriterio(@RequestBody TransportadoraDTO dto) {

        LOGGER.info(Utils.getInicioMetodo());

        List<TransportadoraDTO> lista;

        try {
            lista = transportadoraService.listarCriterios(dto);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lista);
    }
    
    @GetMapping(value = "/transportadoras/perfil/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TransportadoraDTO>> listarPorPerfilUsuarioETipoCredito(@PathVariable("id") Long idPerfil, @Param("tipoCredito") String tipoCredito) {

        LOGGER.info(Utils.getInicioMetodo());

        List<TransportadoraDTO> dtos;
        try {
            dtos = transportadoraService.listarPorPerfilUsuarioETipoCredito(idPerfil, PARAM_TIPO_CREDITO_D0.equals(tipoCredito) ? CodigoTipoCreditoEnum.CREDITOD0.getId() : CodigoTipoCreditoEnum.CREDITOD1.getId());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{TRANSPORTADORA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }    
}
