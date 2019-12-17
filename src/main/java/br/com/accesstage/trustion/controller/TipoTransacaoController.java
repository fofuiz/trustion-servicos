package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoTipoProdutoDTO;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.UnauthorizedException;
import br.com.accesstage.trustion.service.interfaces.ITipoTransacaoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transacao/tipo/")
public class TipoTransacaoController {

    private ITipoTransacaoService transacaoService;

    @Log
    private Logger LOG;

    @Autowired
    public TipoTransacaoController(ITipoTransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping(value = "total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategorizacaoTipoProdutoDTO>> getTotalAVistaParceladoOutros(){
        LOG.info(Utils.getInicioMetodo());
        List<CategorizacaoTipoProdutoDTO> totais;

        try{
            totais = transacaoService.getTotalAVistaParceladoOutros();
        }catch (ForbiddenResponseException | UnauthorizedException | InternalServerErrorResponseException | BadRequestResponseException e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.erro", new Object[]{Mensagem.get("msg.erro.interno")}));
        }
        LOG.info(Utils.getFimMetodo());
        return ResponseEntity.ok(totais);
    }
}
