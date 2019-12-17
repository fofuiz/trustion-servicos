package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
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

import br.com.accesstage.trustion.dto.ComposicaoDTO;
import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.impl.ComposicaoService;
import br.com.accesstage.trustion.util.Mensagem;

@RestController
public class ComposicaoController {

    @Autowired
    private ComposicaoService composicaoService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/detalheComposicao/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ComposicaoDTO>> listarCriterios(@RequestBody DetalheConferenciaDTO detalheConferenciaDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<ComposicaoDTO> lstComposicaoDTO;

        try {

            lstComposicaoDTO = composicaoService.listarComposicao(detalheConferenciaDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Composicao"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(lstComposicaoDTO);
    }
}
