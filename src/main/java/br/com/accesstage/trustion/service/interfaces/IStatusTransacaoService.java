package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoStatusTransacaoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.OperadoraStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.ProdutoStatusDTO;
import br.com.accesstage.trustion.client.cartoes.enums.StatusTransacaoEnum;

import java.util.List;

public interface IStatusTransacaoService {

    List<CategorizacaoStatusTransacaoDTO> getTotalStatusTransacao() throws Exception;
    List<OperadoraStatusDTO> pesquisarTotalPorOperadoraStatus(StatusTransacaoEnum transacaoEnum) throws Exception;
    List<ProdutoStatusDTO> pesquisarTotalPorProdutoStatus(Long idOperadora, StatusTransacaoEnum transacaoEnum) throws Exception;

}
