package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.LogApiDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ILogApiService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
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
public class LogApiController {

    @Autowired
    private ILogApiService logApiService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "logsApi/criterio/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<LogApiDTO>> pesquisarSpecs(@Valid @RequestBody LogApiDTO dto, BindingResult camposInvalidos, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<LogApiDTO> dtos = null;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            dtos = logApiService.listaLogsSpecs(dto, pageable);
        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Logs da API"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }
}
