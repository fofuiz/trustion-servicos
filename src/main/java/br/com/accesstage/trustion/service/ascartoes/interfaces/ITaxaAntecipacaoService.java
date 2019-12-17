package br.com.accesstage.trustion.service.ascartoes.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ITaxaAntecipacaoService {

    List<TaxaAntecipacaoDTO> pesquisar(FiltroTaxaAntecipacaoDTO filtro) throws DataAccessException;

    Page<TaxaAntecipacaoDTO> pesquisarPage(FiltroTaxaAntecipacaoDTO filtro, Pageable pageable) throws DataAccessException;

}
