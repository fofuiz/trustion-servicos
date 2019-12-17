package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ILojaCAService {

    List<LojaOuPontoVendaDTO> carregarComboLoja(Long idEmpresa) throws DataAccessException;

    List<LojaOuPontoVendaDTO> carregarComboLojaPorCnpjEmpresa(String cnpj) throws DataAccessException;

    List<LojaOuPontoVendaDTO> carregarComboSomenteLoja(Long idEmpresa) throws DataAccessException;

    List<LojaOuPontoVendaDTO> carregarComboSomenteLojaPorCnpjEmpresa(String cnpj) throws DataAccessException;
}
