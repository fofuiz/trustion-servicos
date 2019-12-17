package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.DetalheBilheteDTO;
import br.com.accesstage.trustion.dto.ascartoes.DetalhesNsuDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import java.util.List;

public interface INsuService {

    DetalheBilheteDTO pesquisarDetalhesDoBilhete(String idDscAreaCliente, String codArquivo);

    List<DetalhesNsuDTO> consultaDetalhesNsu(GestaoVendasDTO filtro, DetalheBilheteDTO detalheBilhete);

}
