package br.com.accesstage.trustion.client.cartoes.resource;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoTipoProdutoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;

import java.util.List;

public interface ITipoTransacaoResource {

    List<CategorizacaoTipoProdutoDTO> getTotalAVistaParceladoOutros(FiltroDataDTO dto);

}
