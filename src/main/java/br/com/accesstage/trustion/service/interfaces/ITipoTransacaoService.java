package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoTipoProdutoDTO;

import java.util.List;

public interface ITipoTransacaoService {

    List<CategorizacaoTipoProdutoDTO> getTotalAVistaParceladoOutros() throws Exception;

}
