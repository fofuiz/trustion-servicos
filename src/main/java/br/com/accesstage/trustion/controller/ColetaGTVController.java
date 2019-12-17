package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ColetaGTVDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.impl.ColetaGTVService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;

@RestController
public class ColetaGTVController {

    @Autowired
    private ColetaGTVService coletaGTVService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/detalheGTV/criterios", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ColetaGTVDTO>> listarColetaGTV(@RequestBody ColetaGTVDTO coletaGTVDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<ColetaGTVDTO> listaColetaGTVDTO;
        try {
            listaColetaGTVDTO = coletaGTVService.listarColetaGTV(coletaGTVDTO);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Coletas GTV"}));
        }
        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(listaColetaGTVDTO);
    }

    @RequestMapping(value = "/detalheGTV/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ColetaGTVDTO>> listarColetaGTV(@RequestBody ColetaGTVDTO coletaGTVDTO, Pageable pageable) {
        LOGGER.info(Utils.getInicioMetodo());
        Page<ColetaGTVDTO> listaColetaGTVDTO;

        try {
            listaColetaGTVDTO = coletaGTVService.listarColetaGTV(coletaGTVDTO, pageable);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Coletas GTV"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(listaColetaGTVDTO);
    }
}
