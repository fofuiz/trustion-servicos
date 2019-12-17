package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModeloNegocioController {

    @Autowired
    private IModeloNegocioService modeloService;

    @Log
    private static Logger LOGGER;

    @PostMapping(value = "/modeloNegocio", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeloNegocioDTO> criar(@Valid @RequestBody ModeloNegocioDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        ModeloNegocioDTO modeloCriado = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            modeloCriado = modeloService.criar(dto);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(modeloCriado, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/modeloNegocio", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeloNegocioDTO> alterar(@Valid @RequestBody ModeloNegocioDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        ModeloNegocioDTO modeloAlterado = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            modeloAlterado = modeloService.alterar(dto);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(modeloAlterado);
    }

    @RequestMapping(value = "/modeloNegocio/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeloNegocioDTO> excluir(@PathVariable("id") Long idModeloNegocio) {

        LOGGER.info(Utils.getInicioMetodo());

        try {
            modeloService.excluir(idModeloNegocio);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.excluir", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/modeloNegocio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeloNegocioDTO> pesquisar(@PathVariable("id") Long idModeloNegocio) {

        LOGGER.info(Utils.getInicioMetodo());

        ModeloNegocioDTO dto = null;

        try {
            dto = modeloService.pesquisar(idModeloNegocio);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisa", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/modelosNegocios/criterios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ModeloNegocioDTO>> listarCriterio(@RequestBody ModeloNegocioDTO dto) {
        LOGGER.info(Utils.getInicioMetodo());

        List<ModeloNegocioDTO> dtos = null;

        try {
            dtos = modeloService.listarCriterio(dto);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/modelosNegocios/criterios/page", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ModeloNegocioDTO>> listarCriterio(@RequestBody ModeloNegocioDTO dto, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<ModeloNegocioDTO> dtos;

        try {
            dtos = modeloService.listarCriterio(dto, pageable);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/modelosNegocios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ModeloNegocioDTO>> listarPorEmpresa() {

        LOGGER.info(Utils.getInicioMetodo());

        Set<ModeloNegocioDTO> dtos;

        try {
            dtos = modeloService.listarPorIdEmpresa();

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping(value = "/modelosNegocios/idEmpresa/{idEmpresa}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ModeloNegocioDTO>> listarPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {

        LOGGER.info(Utils.getInicioMetodo());

        Set<ModeloNegocioDTO> dtos;

        try {
            dtos = modeloService.listarPorIdEmpresa(idEmpresa);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }    

    @GetMapping(value = "/modelosNegocios/grupo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ModeloNegocioDTO>> listarPorGrupo(@PathVariable("id") Long idGrupo) {

        LOGGER.info(Utils.getInicioMetodo());

        List<ModeloNegocioDTO> dtos = null;

        try {
            dtos = modeloService.listarPorIdGrupo(idGrupo);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"modelo de " + UTF8.negocio}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

}
