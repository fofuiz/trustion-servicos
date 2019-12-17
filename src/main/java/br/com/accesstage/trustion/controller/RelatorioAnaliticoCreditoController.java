package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@RestController
public class RelatorioAnaliticoCreditoController {

    @Autowired
    private IRelatorioAnaliticoCreditoService relAnaliticoCredService;

    @Log
    private static Logger LOGGER;

    @RequestMapping(value = "/relAnaliticoCredito/criterios/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<RelatorioAnaliticoCreditoDTO> listarPorCriterio(@RequestBody RelatorioAnaliticoCreditoDTO relAnaliticoCredDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<RelatorioAnaliticoCreditoDTO> listaRelAnaliticoCred;

        try {
            listaRelAnaliticoCred = relAnaliticoCredService.listarCriterios(relAnaliticoCredDTO, pageable);

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

        return listaRelAnaliticoCred;
    }

    @RequestMapping(value = "/relAnaliticoCredito/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RelatorioAnaliticoCreditoDTO> pesquisar(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        RelatorioAnaliticoCreditoDTO dto = null;

        try {
            dto = relAnaliticoCredService.pesquisar(id);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }
}
