package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.DepositoDTO;
import br.com.accesstage.trustion.service.interfaces.IDepositoService;
import br.com.accesstage.trustion.util.Utils;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepositoController {

    @Autowired
    private IDepositoService iDepositoService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/depositos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<DepositoDTO>> consultarAPI(@RequestBody DepositoDTO dto) throws Exception {

        LOGGER.info(Utils.getInicioMetodo());

        List<DepositoDTO> dtos;

        try {
            dtos = iDepositoService.consultaApi(dto);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        }
        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dtos);
    }
}
