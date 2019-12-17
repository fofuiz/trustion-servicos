package br.com.accesstage.trustion.controller.conciliacao;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.service.interfaces.IRegraClienteAdquirenteService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RegraClienteAdquirenteController {

    @Autowired
    private IRegraClienteAdquirenteService regraClienteAdquirenteService;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo para efetuar a pesquisar conforme os filtro selecionado na tela
     * @param filtro
     * @return listaResumo
     */
    @RequestMapping(value = "/regraClienteAdquirente/pesquisarRegraClienteAdquirente", method = RequestMethod.POST)
    public List<ResultadoRegraClienteAdquirenteDTO> perquisarRegraClienteAdquirente(@RequestBody FiltroTelaRegraClienteAdquirenteDTO filtro) {

        LOGGER.info(">>RegraClienteAdquirenteController.pesquisarRegraClienteAdquirente: " + filtro);

        List<ResultadoRegraClienteAdquirenteDTO> listaRegraClienteAdquirente = new ArrayList<>();

        try {
            
            listaRegraClienteAdquirente = regraClienteAdquirenteService.pesquisarRegraClienteAdquirente(filtro);
        
        } catch (Exception e) {
            LOGGER.error("RegraClienteAdquirenteController.pesquisarRegraClienteAdquirente - Erro ao buscar regra por cliente/adquirente." + e.getMessage(), e);
        }

        LOGGER.info("<<RegraClienteAdquirenteController.pesquisarRegraClienteAdquirente");

        return listaRegraClienteAdquirente;
    }

}
