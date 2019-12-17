package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.ProdutoCA;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.repository.ascartoes.interfaces.IProdutoCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IProdutoCAService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoCAService implements IProdutoCAService {

    @Autowired
    private IProdutoCARepository repository;

    @Override
    @Transactional(readOnly=true)
    public List<ProdutoCA> carregarComboProduto(Long id, Integer permiteSelecionar) throws Exception {
        return repository.findByIdNotAndPermiteSelecionarAndNomeIsNotNullOrderByNomeAsc(id, permiteSelecionar);
    }

}
