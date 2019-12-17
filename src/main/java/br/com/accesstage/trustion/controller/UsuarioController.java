package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.EsqueceuSenhaDTO;
import br.com.accesstage.trustion.dto.RedefinicaoSenhaDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.util.ArrayList;
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

@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        UsuarioDTO usuarioCriadoDTO = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            usuarioCriadoDTO = usuarioService.criar(usuarioDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{UTF8.usuario}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(usuarioCriadoDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/esqueceuSenha", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EsqueceuSenhaDTO> esqueceuSenha(@Valid @RequestBody EsqueceuSenhaDTO esqueceuSenhaDTO, BindingResult camposInvalidos) {
        String email;
        EsqueceuSenhaDTO esqueceuSenhaDTOResponse = new EsqueceuSenhaDTO();

        LOGGER.info(Utils.getInicioMetodo());

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            email = usuarioService.esqueceuSenha(esqueceuSenhaDTO);
            esqueceuSenhaDTOResponse.setEmail(email);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.registro.nao.valido", new Object[]{UTF8.usuario}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(esqueceuSenhaDTOResponse);
    }

    @RequestMapping(value = "/usuario", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> alterar(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        UsuarioDTO usuarioAlteradoDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            usuarioAlteradoDTO = usuarioService.alterar(usuarioDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{UTF8.usuario}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(usuarioAlteradoDTO);
    }

    @RequestMapping(value = "/usuario/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UsuarioDTO> exluir(@PathVariable Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        try {
            usuarioService.excluir(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.excluir", new Object[]{UTF8.usuario}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/usuarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDTO>> listar() {

        LOGGER.info(Utils.getInicioMetodo());

        List<UsuarioDTO> listaUsuario = new ArrayList<>();

        try {
            listaUsuario = usuarioService.listarTodos();

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.usuarios}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(listaUsuario);
    }

    @RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> listar(@PathVariable Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        UsuarioDTO usuario = new UsuarioDTO();

        try {
            usuario = usuarioService.pesquisar(id);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.usuario}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(value = "/redefinirSenha", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> redefinirSenha(@Valid @RequestBody RedefinicaoSenhaDTO redefinicaoSenhaDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            usuarioService.redefinirSenha(redefinicaoSenhaDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.redefinir.senha"));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/usuarios/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UsuarioDTO> listarPorCriterio(@RequestBody UsuarioDTO usuarioDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<UsuarioDTO> listaUsuario;

        try {
            listaUsuario = usuarioService.listarCriterios(usuarioDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.usuarios}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return listaUsuario;
    }

    @RequestMapping(value = "/usuarios/criterios", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDTO>> listarPorCriterio(@RequestBody UsuarioDTO usuarioDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<UsuarioDTO> listaUsuario;

        try {
            listaUsuario = usuarioService.listarCriterios(usuarioDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{UTF8.usuarios}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(listaUsuario);
    }
}
