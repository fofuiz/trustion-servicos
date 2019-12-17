package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ExtratoElegivelDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IExtratoElegivelService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class ExtratoElegivelController {

    @Autowired
    private IExtratoElegivelService extratoElegivelService;

    @Log
    private Logger LOG;

    @GetMapping(value = "/extratos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ExtratoElegivelDTO>> listarPorIdRelatorioAnaliticoD1(@PathVariable("id") Long idRelatorioAnaliticoD1) {

        LOG.info(Utils.getInicioMetodo());

        List<ExtratoElegivelDTO> dtos = null;

        try {

            dtos = extratoElegivelService.listarPorIdRelatorioAnaliticoD1(idRelatorioAnaliticoD1);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
           throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/extratos/d0/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ExtratoElegivelDTO>> listarPorIdRelatorioAnaliticoD0(@PathVariable("id") Long idRelatorioAnaliticoD0) {

        LOG.info(Utils.getInicioMetodo());

        List<ExtratoElegivelDTO> dtos = null;

        try {

        	LOG.info(">> idRelatorioAnaliticoD0: " + idRelatorioAnaliticoD0);
            dtos = extratoElegivelService.listarPorIdRelatorioAnaliticoD0(idRelatorioAnaliticoD0);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/extrato/desconciliar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desconciliar(@PathVariable("id") Long idConciliacao) {

        LOG.info(Utils.getInicioMetodo());

        try {

            extratoElegivelService.desconciliar(idConciliacao);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/extrato/desconciliar/d0/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desconciliarD0(@PathVariable("id") Long idConciliacao) {

        LOG.info(Utils.getInicioMetodo());

        try {

            extratoElegivelService.desconciliarD0(idConciliacao);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/extratos/relatorio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ExtratoElegivelDTO>> pesquisarExtratosParaConciliacao(@PathVariable("id") Long idRelatorioAnaliticoD1){

        List<ExtratoElegivelDTO> dtos;

        try{

        dtos = extratoElegivelService.pesquisarExtratosParaConciliacao(idRelatorioAnaliticoD1);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/extratos/relatorio/d0/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ExtratoElegivelDTO>> pesquisarExtratosParaConciliacaoD0(@PathVariable("id") Long idRelatorioAnalitico){

        List<ExtratoElegivelDTO> dtos;

        try{

            dtos = extratoElegivelService.pesquisarExtratosParaConciliacaoD0(idRelatorioAnalitico);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/extrato/conciliar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> conciliar(@PathVariable("id") Long idRelatorioAnaliticoD1, @RequestBody List<ExtratoElegivelDTO> extratosConciliados){

        LOG.info(Utils.getInicioMetodo());

        try {

            extratoElegivelService.conciliar(idRelatorioAnaliticoD1, extratosConciliados);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value = "/extrato/conciliar/d0/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> conciliarD0(@PathVariable("id") Long idRelatorioAnalitico, @RequestBody List<ExtratoElegivelDTO> extratosConciliados){

        LOG.info(Utils.getInicioMetodo());

        try {

            extratoElegivelService.conciliarD0(idRelatorioAnalitico, extratosConciliados);

        }catch (BadRequestResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Exceção: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"extratos"}));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
