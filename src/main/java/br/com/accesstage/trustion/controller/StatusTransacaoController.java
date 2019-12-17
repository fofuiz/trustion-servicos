package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoStatusTransacaoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.OperadoraStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.ProdutoStatusDTO;
import br.com.accesstage.trustion.client.cartoes.enums.StatusTransacaoEnum;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.UnauthorizedException;
import br.com.accesstage.trustion.service.interfaces.IStatusTransacaoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transacao/status/")
public class StatusTransacaoController {

    private IStatusTransacaoService statusTransacaoService;

    @Log
    private Logger LOG;

    @Autowired
    public StatusTransacaoController(IStatusTransacaoService statusTransacaoService) {
        this.statusTransacaoService = statusTransacaoService;
    }

    @GetMapping(value = "total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<?>> getTotalStatusTransacao(){
        List<CategorizacaoStatusTransacaoDTO> dtos;
        LOG.info(Utils.getInicioMetodo());

        try{
            dtos = statusTransacaoService.getTotalStatusTransacao();
        }catch (ForbiddenResponseException | UnauthorizedException | InternalServerErrorResponseException | BadRequestResponseException e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.erro", new Object[]{Mensagem.get("msg.erro.interno")}));
        }

        LOG.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "operadora/{statusTransacao}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<?>> pesquisarTotalPorOperadoraStatus(@PathVariable("statusTransacao") StatusTransacaoEnum transacaoEnum){
        List<OperadoraStatusDTO> dtos;

        try{
            dtos = statusTransacaoService.pesquisarTotalPorOperadoraStatus(transacaoEnum);
        }catch (ForbiddenResponseException | UnauthorizedException | InternalServerErrorResponseException | BadRequestResponseException e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.erro", new Object[]{Mensagem.get("msg.erro.interno")}));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "operadora/{statusTransacao}/{idOperadora}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<?>> pesquisarTotalPorOperadoraStatus(@PathVariable("statusTransacao") StatusTransacaoEnum transacaoEnum, @PathVariable("idOperadora") Long idOperadora){
        List<ProdutoStatusDTO> dtos;

        try{
            dtos = statusTransacaoService.pesquisarTotalPorProdutoStatus(idOperadora, transacaoEnum);
        }catch (ForbiddenResponseException | UnauthorizedException | InternalServerErrorResponseException | BadRequestResponseException e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.erro", new Object[]{Mensagem.get("msg.erro.interno")}));
        }
        return ResponseEntity.ok(dtos);
    }
}
