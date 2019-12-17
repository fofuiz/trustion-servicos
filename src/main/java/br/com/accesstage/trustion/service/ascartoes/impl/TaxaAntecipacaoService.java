package br.com.accesstage.trustion.service.ascartoes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.TaxaAntecipacaoRepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ITaxaAntecipacaoService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaxaAntecipacaoService implements ITaxaAntecipacaoService {

    @Autowired
    private TaxaAntecipacaoRepository repository;

    @Override
    @Transactional(readOnly=true)
    public List<TaxaAntecipacaoDTO> pesquisar(FiltroTaxaAntecipacaoDTO filtro) throws DataAccessException {
        return repository.consulta(filtro);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<TaxaAntecipacaoDTO> pesquisarPage(FiltroTaxaAntecipacaoDTO filtro, Pageable pageable) throws DataAccessException {
        return repository.consultaPage(filtro, pageable);
    }

}
