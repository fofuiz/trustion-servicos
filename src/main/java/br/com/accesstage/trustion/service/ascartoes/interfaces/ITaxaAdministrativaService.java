package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import java.util.List;

import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;


public interface ITaxaAdministrativaService {

    List<TaxaAdministrativaDTO> pesquisar(FiltroTaxaAdministrativaDTO filtro) throws DataAccessException;

    List<TaxaAdministrativaDTO> pesquisarPage(FiltroTaxaAdministrativaDTO filtro, Pageable pageable) throws DataAccessException;

    List<TaxaAdministrativaCadastroDTO> pesquisarCadastro(FiltroTaxasAdministrativasCadastroDTO filtro) throws DataAccessException;

    List<TaxaAdministrativa> salvar(List<TaxaAdministrativaCadastroDTO> listTaxaAdminitrativaCadastro) throws DataAccessException;
}
