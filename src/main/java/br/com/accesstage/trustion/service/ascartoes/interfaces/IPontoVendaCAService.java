package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IPontoVendaCAService {

    List<LojaOuPontoVendaDTO> carregarComboPontoVenda(Long idEmpresa) throws DataAccessException;

    List<LojaOuPontoVendaDTO> carregarComboPontoVendaPorCodLoja(Long codLoja) throws DataAccessException;
}
