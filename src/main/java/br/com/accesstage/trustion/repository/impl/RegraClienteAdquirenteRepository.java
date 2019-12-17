package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.asconciliacao.model.RegraClienteAdquirenteEntity;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesConciliacaoCartao;
import br.com.accesstage.trustion.repository.mapper.RegraClienteAdquirenteMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;
import org.springframework.dao.DataAccessException;

@Repository
public class RegraClienteAdquirenteRepository {

    @Autowired
    @Qualifier(value = "cartoesDataSource")
    private DataSource dataSource;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo para pesquisar Regra Cliente Adquirente
     *
     * @param filtro
     * @return
     * @throws Exception
     */
    public List<RegraClienteAdquirenteEntity> pesquisarRegraClienteAdquirente(FiltroTelaRegraClienteAdquirenteDTO filtro) throws Exception {

        List<RegraClienteAdquirenteEntity> listaResultado;

        try {

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = obtemParametrosQuery(filtro);

            String sql = complementaQuery(ConstantesConciliacaoCartao.QUERY_PESQUISA_REGRA_CLIENTE_ADQUIRENTE, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new RegraClienteAdquirenteMapper());

        } catch (DataAccessException e) {
            LOGGER.error(">>RegraClienteAdquirenteRepository.pesquisarRegraClienteAdquirente - Erro ao pesquisar Regra por Cliente/Adquirente." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    /**
     * Metoto para obter parametros do filtro
     *
     * @param filtro
     * @return
     */
    private Object[] obtemParametrosQuery(FiltroTelaRegraClienteAdquirenteDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosQuery(filtro)];
        int indiceAtivo = 0;

        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (informadoCampo(filtro.getCodigoBancoStr())) {
            parametros[indiceAtivo] = filtro.getCodigoBancoStr();
            indiceAtivo++;
        }

        if (filtro.getCodigoAquirenteStr() != null && informadoCampo(filtro.getCodigoAquirenteStr().toString())) {
            parametros[indiceAtivo] = filtro.getCodigoAquirenteStr();
            indiceAtivo++;

        }

        return parametros;
    }

    /**
     * Metodo para devolver quantidade de parametros
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosQuery(FiltroTelaRegraClienteAdquirenteDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        if (informadoCampo(filtro.getCodigoBancoStr())) {
            paramentrosQtd++;
        }

        if (filtro.getCodigoAquirenteStr() != null && informadoCampo(filtro.getCodigoAquirenteStr().toString())) {
            paramentrosQtd++;
        }

        return paramentrosQtd;
    }

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param query
     * @param filtro
     * @return query
     */
    private String complementaQuery(String sql, FiltroTelaRegraClienteAdquirenteDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (informadoCampo(filtro.getCodigoBancoStr())) {
            query.append(" AND FIN.COD_BANCO = ?");
        }

        if (filtro.getCodigoAquirenteStr() != null && informadoCampo(filtro.getCodigoAquirenteStr().toString())) {
            query.append(" AND FIN.COD_OPERADORA = ?");
        }

        return query.toString();
    }

    public static boolean informadoCampo(String valorCampo) {
        return (valorCampo != null) && !"".equals(valorCampo.trim());
    }
}
