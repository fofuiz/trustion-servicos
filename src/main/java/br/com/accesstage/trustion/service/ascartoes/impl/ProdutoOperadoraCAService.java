package br.com.accesstage.trustion.service.ascartoes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA;
import br.com.accesstage.trustion.repository.ascartoes.impl.ProdutoOperadoraCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IProdutoOperadoraCAService;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoOperadoraCAService implements IProdutoOperadoraCAService {

    @Autowired
    private ProdutoOperadoraCARepository repository;

    @Override
    @Transactional(readOnly=true)
    public List<ProdutoOperadoraCA> carregarComboProdutoOperadora(Long codOperadora) throws DataAccessException {
        return repository.listarProdutoPorOperadora(codOperadora);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ProdutoOperadoraCA> buscarProdutoPorOperadoraECodProduto(Long codOperadora, Long codProduto) throws DataAccessException {
        return repository.buscarProdutoPorOperadoraECodProduto(codOperadora, codProduto);
    }
}
