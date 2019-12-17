package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.ProdutoCA;
import java.util.List;

public interface IProdutoCAService {

    List<ProdutoCA> carregarComboProduto(Long id, Integer permiteSelecionar) throws Exception;
    
}
