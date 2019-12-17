package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoRegraClienteAdquirenteDTO;

import java.util.List;
import org.springframework.dao.DataAccessException;

public interface IRegraClienteAdquirenteService {

    /**
     * metodo para pesquisar regra cliente/adquirente
     *
     * @param filtro parametros pesquisa da regra por cliente/adquirente
     * @return lista de resultadoRegraClienteAdquirente.
     * @throws DataAccessException
     */
    List<ResultadoRegraClienteAdquirenteDTO> pesquisarRegraClienteAdquirente(FiltroTelaRegraClienteAdquirenteDTO filtro) throws DataAccessException;

}
