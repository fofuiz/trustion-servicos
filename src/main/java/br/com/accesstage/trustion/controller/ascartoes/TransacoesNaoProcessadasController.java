package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IEmpresaCaService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.ITransacoesNaoProcessadasService;
import br.com.accesstage.trustion.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author raphael
 */
@RestController
@RequestMapping("/transacoes/naoprocessadas")
public class TransacoesNaoProcessadasController {

    private static final Long OPCAO_EXTRATO_PAGAMENTO = 5L;
    private static final Long OPCAO_EXTRATO_ANTECIPACAO = 6L;

    @Log
    private static Logger LOGGER;

    @Autowired
    private IEmpresaCaService empresaCaService;

    @Autowired
    private ITransacoesNaoProcessadasService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<TransacoesNaoProcessadasDTO>> pesquisar(FiltroTransacoesNaoProcessadasDTO filtro) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        try {
            filtro.setEmpresa(getEmpresaCA(filtro.getListaEmpresas()));
            List<TransacoesNaoProcessadasDTO> transacoesDTOs = new ArrayList<>();

            if (OPCAO_EXTRATO_PAGAMENTO.equals(filtro.getOpcaoExtrato())) {
                transacoesDTOs = service.buscarHistoricoTransacoesNaoProcessadas(filtro);

            } else if (OPCAO_EXTRATO_ANTECIPACAO.equals(filtro.getOpcaoExtrato())) {
                transacoesDTOs = service.buscarHistoricoTransacoesNaoProcessadasAntecipacao(filtro);
            }

            LOGGER.info(Utils.getFimMetodo());
            return new ResponseEntity<>(transacoesDTOs, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        }
    }

    private EmpresaCA getEmpresaCA(List<String> empresas) {
        String empresa = empresas.get(0);
        if (empresa != null) {
            long id = Long.parseLong(empresa);
            return empresaCaService.find(id);
        }
        return null;
    }
}
