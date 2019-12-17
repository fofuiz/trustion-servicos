package br.com.accesstage.trustion.client.cartoes.resource;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoStatusTransacaoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.OperadoraStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.ProdutoStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroMultiplasDatasDTO;
import br.com.accesstage.trustion.client.cartoes.enums.StatusTransacaoEnum;

import java.util.List;

public interface IStatusTransacaoResource {

    List<CategorizacaoStatusTransacaoDTO> getTotalStatusTransacao(FiltroMultiplasDatasDTO dto);
    List<OperadoraStatusDTO> pesquisarTotalPorOperadoraStatus(FiltroDataDTO dto, StatusTransacaoEnum transacaoEnum);
    List<ProdutoStatusDTO> pesquisarTotalPorProdutoStatus(FiltroDataDTO dto, Long idOperadora, StatusTransacaoEnum transacaoEnum);

}
