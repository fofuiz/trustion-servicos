package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA;
import java.util.List;
import org.springframework.dao.DataAccessException;

public interface IProdutoOperadoraCAService {
    List<ProdutoOperadoraCA> carregarComboProdutoOperadora(Long codOperadora) throws DataAccessException;
    List<ProdutoOperadoraCA> buscarProdutoPorOperadoraECodProduto(Long codOperadora, Long codProduto) throws DataAccessException;
}
