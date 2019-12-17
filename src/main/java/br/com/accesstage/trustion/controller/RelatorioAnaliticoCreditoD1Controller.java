package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoD1Service;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelatorioAnaliticoCreditoD1Controller {

    @Autowired
    private IRelatorioAnaliticoCreditoD1Service relatorioD1Service;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/relAnaliticoCreditoD1/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RelatorioAnaliticoCreditoD1DTO>> listarCriterios(@Valid @RequestBody RelatorioAnaliticoCreditoD1DTO relAnaliticoCredDTO, BindingResult camposInvalidos, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<RelatorioAnaliticoCreditoD1DTO> dtos = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dtos = relatorioD1Service.listarCriterios(relAnaliticoCredDTO, pageable);
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

        return ResponseEntity.ok(dtos);
    }
}
