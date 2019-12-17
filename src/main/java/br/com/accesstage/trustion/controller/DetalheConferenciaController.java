package br.com.accesstage.trustion.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IDetalheConferenciaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@RestController
public class DetalheConferenciaController {

    @Autowired
    private IDetalheConferenciaService detalheConferenciaService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/detalheConferencia/criterios", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<DetalheConferenciaDTO>> listarColetaGTV(@RequestBody RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO) {
        LOGGER.info(Utils.getInicioMetodo());

        List<DetalheConferenciaDTO> lstDetalheConferenciaDTO = new ArrayList<>();

        try {

            lstDetalheConferenciaDTO = detalheConferenciaService.pesquisarDetalheConferencia(relatorioAnaliticoCreditoD1DTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Detalhes da " + UTF8.Conferencia}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(lstDetalheConferenciaDTO);

    }
}
