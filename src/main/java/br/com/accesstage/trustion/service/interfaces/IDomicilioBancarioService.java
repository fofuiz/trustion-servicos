package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;

import java.util.List;

/**
 * @author cristiano.silva
 *
 */
public interface IDomicilioBancarioService {

    /**
     * metodo para pesquisar detalhe do domicílio bancário
     *
     * @param filtroDomicilioBancarioDTO parametros pesquisa detalhe do
     * domicílio bancário
     * @return lista de resultadoDetalheDomicilioBancario.
     * @throws Exception
     */
    public List<ResultadoDomicilioBancarioDTO> perquisarDetalheDomicilioBancario(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) throws Exception;

    /**
     * metodo para pesquisar todos os bancos ativos.
     *
     * @return lsita bancos ativos.
     * @throws Exception
     */
    public List<ResultadoDomicilioBancarioDTO> pesquisarTodosBancos(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) throws Exception;

}
