package br.com.accesstage.trustion.controller.conciliacao;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroTelasConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.ResultadoConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaNovaConciliacaoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelasConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IConciliacaoCartaoService;
import br.com.accesstage.trustion.util.ConstantUtil;
import br.com.accesstage.trustion.util.Mensagem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ConciliacaoCartaoController {

    @Autowired
    private IConciliacaoCartaoService conciliacaoCartaoService;

    @Log
    private static Logger LOGGER;

    private static final String MSG = "msg.nao.foi.possivel.pesquisar";

    /**
     * Metodo para efetuar a pesquisar conforme os filtro selecionado na tela
     *
     * @param filtro
     * @return listaResumo
     */
    @RequestMapping(value = "/cartao/pesquisarResumo", method = RequestMethod.POST)
    public List<ResultadoConciliacaoCartaoResumoDTO> perquisarResumo(@RequestBody FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.perquisarResumo: " + filtro);

        filtro.setAnoMesRefStr(filtro.getAnoRef() + filtro.getAnoMesRefStr());

        List<ResultadoConciliacaoCartaoResumoDTO> listaResumo;

        try {
            listaResumo = conciliacaoCartaoService.pesquisarRegistrosResumo(filtro);
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.perquisarResumo - Erro ao buscar dados do Resumo da conciliacao de cartao." + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"dados do Resumo da conciliacao de cartao"}));
        }

        LOGGER.info("<<ConciliacaoCartaoController.perquisarResumo");

        return listaResumo;
    }

    /**
     * Metodo para efetuar a pesquisar conforme os filtro selecionado na tela
     *
     * @param filtro
     * @return listaDetalhe
     */
    @RequestMapping(value = "/cartao/pesquisarDetalhe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoConciliacaoCartaoDetalheDTO> perquisarDetalhe(@RequestBody FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.perquisarDetalhe");

        List<ResultadoConciliacaoCartaoDetalheDTO> listaDetalhe;

        try {
            listaDetalhe = conciliacaoCartaoService.pesquisarRegistrosDetalhe(filtro);
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.perquisarDetalhe - Erro ao buscar dados do detalhe da conciliacao de cartao." + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"dados do detalhe da conciliacao de cartao"}));
        }

        LOGGER.info("<<ConciliacaoCartaoController.perquisarDetalhe");

        return listaDetalhe;
    }

    /**
     * Metodo para obter o detalhe de um registro de extrato nao conciliado
     *
     * @param filtro
     * @return listaResumo
     */
    @RequestMapping(value = "/cartao/pesquisarDetalheExtratoNaoConciliados", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarDetalheExtratoNaoConciliados(@RequestBody FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.pesquisarDetalheExtratoNaoConciliados");

        List<ResultadoConciliacaoCartaoDetalheDTO> listaDetalhe = new ArrayList<>();

        try {
            listaDetalhe.addAll(conciliacaoCartaoService.pesquisarRegistrosDetalheExtratoNaoConciliados(filtro));
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.pesquisarDetalheExtratoNaoConciliados - Erro ao buscar dados dos registros." + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"dados dos registros"}));
        }

        LOGGER.info("<<ConciliacaoCartaoController.pesquisarDetalheExtratoNaoConciliados");

        return listaDetalhe;
    }

    /**
     * Metodo para obter o detalhe de um registro de cartao nao conciliado
     *
     * @param filtro
     * @return listaResumo
     */
    @RequestMapping(value = "/cartao/pesquisarDetalheCartaoNaoConciliados", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarDetalheCartaoNaoConciliados(@RequestBody FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.pesquisarDetalheCartaoNaoConciliados");

        List<ResultadoConciliacaoCartaoDetalheDTO> listaDetalhe = new ArrayList<>();

        try {
            listaDetalhe.addAll(conciliacaoCartaoService.pesquisarRegistrosDetalheCartaoNaoConciliados(filtro));
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.pesquisarDetalheCartaoNaoConciliados - Erro ao buscar dados dos registros." + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"dados dos registros"}));
        }

        LOGGER.info("<<ConciliacaoCartaoController.pesquisarDetalheCartaoNaoConciliados");

        return listaDetalhe;
    }

    @RequestMapping(value = "/cartao/nova-conciliar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, String> conciliar(@RequestBody FiltroTelaNovaConciliacaoDTO filtro) {

        LOGGER.info(">>NovaConciliacaoManualController.conciliar");

        Map<String, String> responseBody = new HashMap<>();

        try {
            conciliacaoCartaoService.conciliar(filtro);
            responseBody.put("mensagem", ConstantUtil.MSG_NOVA_CONCILIACAO_SUCESSO_CONCILIAR_MANUAL);
        } catch (Exception e) {
            LOGGER.error("ConciliacaoManualController.conciliar - Ocorreu um erro ao conciliar " + e.getMessage(), e);
            responseBody.put("mensagem", ConstantUtil.MSG_NOVA_CONCILIACAO_ERRO_CONCILIAR_MANUAL);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"conciliar"}));
        }

        LOGGER.info("<<NovaConciliacaoManualController.conciliar");

        return responseBody;
    }

    @RequestMapping(value = "/cartao/obterDetalheExtrato", method = RequestMethod.POST)
    public @ResponseBody List<ResultadoConciliacaoCartaoDetalheDTO> obterDetalheExtratoPorId(@RequestBody FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.obterDetalheExtratoPorId");

        List<ResultadoConciliacaoCartaoDetalheDTO> listaDetalhe = new ArrayList<ResultadoConciliacaoCartaoDetalheDTO>();

        try {
            listaDetalhe.addAll(conciliacaoCartaoService.obterRegistrosExtratoPorId(filtro));
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.obterDetalheExtratoPorId - Erro ao buscar dados do detalhe do registro." + e.getMessage());
        }

        LOGGER.info("<<ConciliacaoCartaoController.obterDetalheExtratoPorId");

        return listaDetalhe;
    }

    /**
     * Metodo para obter o detalhe de um registro por id
     * @param filtro
     * @return listaDetalhe
     */

    @RequestMapping(value = "/cartao/obterDetalheCartao", method = RequestMethod.POST)
    public @ResponseBody List<ResultadoConciliacaoCartaoDetalheDTO> obterDetalheCartaoPorId(@RequestBody FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoController.obterDetalheCartaoPorId");



        List<ResultadoConciliacaoCartaoDetalheDTO> listaDetalhe = new ArrayList<ResultadoConciliacaoCartaoDetalheDTO>();

        try {
            listaDetalhe.addAll(conciliacaoCartaoService.obterRegistrosCartaoPorId(filtro));
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoController.obterDetalheCartaoPorId - Erro ao buscar dados do detalhe do registro." + e.getMessage());
        }

        LOGGER.info("<<ConciliacaoCartaoController.obterDetalheCartaoPorId");

        return listaDetalhe;
    }
}
