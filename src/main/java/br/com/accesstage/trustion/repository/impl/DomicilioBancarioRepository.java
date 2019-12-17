package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.repository.constantes.ConstantesDomicilioBancario;
import br.com.accesstage.trustion.repository.mapper.DomicilioBancarioComboMapper;
import br.com.accesstage.trustion.repository.mapper.DomicilioBancarioMapper;
import br.com.accesstage.trustion.util.ParametroQueryUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DomicilioBancarioRepository {

    @Autowired
    @Qualifier(value = "conciliacaoDataSource")
    DataSource dataSource;

    @Log
    private static Logger LOGGER;

    public List<DomicilioBancario> perquisarDetalheDomicilioBancario(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) {
        LOGGER.info(LogsEnum.DOMBANC007.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());
        List<DomicilioBancario> listaResultado = null;

        Object[] parameters = obtemParametrosQuery(filtroDomicilioBancarioDTO);

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String sql = complementaQuery(ConstantesDomicilioBancario.QUERE_PESQUISA_DOMICILIO_BANCARIO_POR_EMPRESA, filtroDomicilioBancarioDTO);

            listaResultado = jdbcTemplate.query(sql, parameters, new DomicilioBancarioMapper());
        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC008.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID());
            throw e;
        }
        return listaResultado;
    }

    public List<DomicilioBancario> pesquisarTodosBancos(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) {
        LOGGER.info(LogsEnum.DOMBANC009.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());
        List<DomicilioBancario> listaResultado = null;

        Integer idEmpresa = filtroDomicilioBancarioDTO.getEmpID();

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String sql = ConstantesDomicilioBancario.QUERY_NOVA_PESQUISA_TODOS_BANCOS;

            listaResultado = jdbcTemplate.query(sql, new Object[]{idEmpresa}, new DomicilioBancarioComboMapper());
        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC010.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(), e);
            throw e;
        }
        return listaResultado;
    }

    /**
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosQuery(FiltroDomicilioBancarioDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoBancoStr())) {
            paramentrosQtd++;
        }

        return paramentrosQtd;
    }

    /**
     *
     * @param filtro
     * @return
     */
    private Object[] obtemParametrosQuery(FiltroDomicilioBancarioDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosQuery(filtro)];
        int indiceAtivo = 0;

        parametros[indiceAtivo] = filtro.getEmpID();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoBancoStr())) {
            parametros[indiceAtivo] = filtro.getCodigoBancoStr();
            indiceAtivo++;
        }

        return parametros;
    }

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    private String complementaQuery(String sql, FiltroDomicilioBancarioDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoBancoStr())) {
            query.append(" AND NRO_BANCO = ?");
        }

        return query.toString();
    }
}
