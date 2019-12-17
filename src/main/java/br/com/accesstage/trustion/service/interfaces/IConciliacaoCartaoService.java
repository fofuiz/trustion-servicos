package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.FiltroTelasConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.ResultadoConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaNovaConciliacaoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelasConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoConciliacaoCartaoDetalheDTO;

import java.util.List;

public interface IConciliacaoCartaoService {

    List<ResultadoConciliacaoCartaoResumoDTO> pesquisarRegistrosResumo(FiltroTelasConciliacaoCartaoResumoDTO filtro) throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalhe(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheExtratoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheCartaoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheExtrato(FiltroTelasConciliacaoCartaoDetalheDTO filtro)  throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheCartao(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws  Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> obterRegistrosExtratoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception;

    List<ResultadoConciliacaoCartaoDetalheDTO> obterRegistrosCartaoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception;

    void conciliar(FiltroTelaNovaConciliacaoDTO filtro) throws Exception;

}
