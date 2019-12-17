package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.asconciliacao.model.ModalidadeTarifa;
import br.com.accesstage.trustion.asconciliacao.model.MotivoConciliacao;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.conciliacao.AdquirenteDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesFiltroComboBox;
import br.com.accesstage.trustion.repository.mapper.AdquirenteMapper;
import br.com.accesstage.trustion.repository.mapper.ModalidadeTarifaMapper;
import br.com.accesstage.trustion.repository.mapper.MotivoConciliacaoMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FiltroComboBoxRepository {
    @Log
    private static Logger LOGGER;

    @Autowired
    @Qualifier(value = "cartoesDataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "conciliacaoDataSource")
    private DataSource dataSourceConciliacao;


    /**
     * metodo para listar todos adquirentes.
     *
     * @return
     */
    public List<AdquirenteDTO> listarAdquirentes() {

        LOGGER.info(">>FiltroComboBoxDAOImpl.listarAdquirentes");

        List<AdquirenteDTO> listaResultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            StringBuilder sql = new StringBuilder(ConstantesFiltroComboBox.QUERY_PESQUISA_ADQUIRENTES);

            listaResultado = jdbcTemplate.query(sql.toString(), new AdquirenteMapper());
        } catch (Exception e) {
            LOGGER.error(">>FiltroComboBoxDAOImpl.listarAdquirentes - Erro ao buscar dados dos adquirentes." + e.getMessage());
            throw e;
        }

        LOGGER.info("<<FiltroComboBoxDAOImpl.listarAdquirentes");

        return listaResultado;
    }

    public List<ModalidadeTarifa> pesquisarTodasModalidadesTarifas() {
        LOGGER.info(">>FiltroComboBoxDAOImpl.pesquisarTodasModalidadesTarifas");
        List<ModalidadeTarifa> listaResultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceConciliacao);
            StringBuilder sql = new StringBuilder(
                    ConstantesFiltroComboBox.QUERY_PESQUISA_MODALIDADES_TARIFA);
            listaResultado = jdbcTemplate.query(sql.toString(),
                    new ModalidadeTarifaMapper());

        } catch (Exception e) {
            LOGGER.error(">>FiltroComboBoxDAOImpl.pesquisarTodasModalidadesTarifas - Erro ao buscar dados de Modalidades Tarifa." + e.getMessage());
            throw e;
        }
        LOGGER.info("<<FiltroComboBoxDAOImpl.pesquisarTodasModalidadesTarifas");

        return listaResultado;
    }

    /**
     * metodo para listar todos motivos da conciliacao.
     *
     * @param empID
     * @return
     */
    public List<MotivoConciliacao> listarMotivoConciliacaoPorEmpID(Long empID) {

        LOGGER.info(">>FiltroComboBoxDAOImpl.listarMotivoConciliacao");

        final String status = "S";

        List<MotivoConciliacao> listaResultado;

        Object [] params = new Object[2];

        params[0] = status;
        params[1] = empID;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceConciliacao);
            StringBuilder sql = new StringBuilder(
                    ConstantesFiltroComboBox.QUERY_PESQUISA_MOTIVO_CONCILIACAO_POR_EMPID);
            listaResultado = jdbcTemplate.query(sql.toString(),params,
                    new MotivoConciliacaoMapper());

        } catch (Exception e) {
            LOGGER.error(">>FiltroComboBoxDAOImpl.listarMotivoConciliacao - Erro ao buscar dados Motivo da Conciliacao." + e.getMessage());
            throw e;
        }

        LOGGER.info("<<FiltroComboBoxDAOImpl.listarMotivoConciliacao");

        return listaResultado;
    }


}
