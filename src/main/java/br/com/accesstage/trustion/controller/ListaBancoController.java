package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ListaBancoDTO;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IListaBancoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class ListaBancoController {

    @Autowired
    private IListaBancoService listaBancoService;

    @Log
    private Logger LOG;

    @GetMapping(value = "/listaBancos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ListaBancoDTO>> listarTodos(){

        LOG.info(Utils.getInicioMetodo());

        List<ListaBancoDTO> dtos = null;

        try {
            dtos = listaBancoService.listarTodos();
        } catch (ForbiddenResponseException e) {
            LOG.error("Exceção " +e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            LOG.error("Exceção " +e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de " + UTF8.creditos}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
