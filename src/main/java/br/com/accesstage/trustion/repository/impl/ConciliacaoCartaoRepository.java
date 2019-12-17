package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.repository.mapper.ConciliacaoCartaoDetalheExtratoMapper;
import br.com.accesstage.trustion.repository.mapper.ResumoConciliacaoCartaoEntityMapper;
import br.com.accesstage.trustion.repository.mapper.ConciliacaoCartaoResumoMapper;
import br.com.accesstage.trustion.repository.mapper.ConciliacaoCartaoResumoMapper2;
import br.com.accesstage.trustion.repository.mapper.ConciliacaoCartaoDetalheCartaoMapper2;
import br.com.accesstage.trustion.repository.mapper.DetalheConciliacaoCartaoEntityMapper;
import br.com.accesstage.trustion.repository.mapper.ConciliacaoCartaoDetalheCartaoMapper;
import br.com.accesstage.trustion.asconciliacao.model.DetalheConciliacaoCartaoEntity;
import br.com.accesstage.trustion.asconciliacao.model.ResumoConciliacaoCartaoEntity;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroTelasConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelasConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesConciliacaoCartao;
import br.com.accesstage.trustion.util.ParametroQueryUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConciliacaoCartaoRepository {

    @Autowired
    @Qualifier(value = "cartoesDataSource")
    private DataSource dataSource;

    @Log
    private static Logger LOGGER;

    public List<ResumoConciliacaoCartaoEntity> pesquisarRegistrosResumoConciliados(FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoRepository.pesquisarRegistrosResumo");

        List<ResumoConciliacaoCartaoEntity> listaResultado;
        try {
            ResumoConciliacaoCartaoEntityMapper mapper = new ResumoConciliacaoCartaoEntityMapper();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = mapper.obtemParametrosResumoQuery1(filtro);

            String sql = mapper.complementaResumoQuery2(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_RESUMIDOS_CONCILIADOS, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoResumoMapper());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.pesquisarRegistrosResumo - Erro ao buscar dados de conciliacao resumo de cartao." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<DetalheConciliacaoCartaoEntity> pesquisarRegistrosDetalhe(FiltroTelasConciliacaoCartaoDetalheDTO filtro) {
        LOGGER.info(">>ConciliacaoCartaoRepository.pesquisarRegistrosDetalhe");

        List<DetalheConciliacaoCartaoEntity> listaResultado = new ArrayList<DetalheConciliacaoCartaoEntity>();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            JdbcTemplate jdbcTemplate1 = new JdbcTemplate(dataSource);

            DetalheConciliacaoCartaoEntityMapper mapperEntity = new DetalheConciliacaoCartaoEntityMapper();
            Object[] parametros = mapperEntity.obtemParametrosDetalheQuery(filtro);

            String sql = null;

            if (null == filtro.getCodigoMovimento()) {
                sql = mapperEntity.complementaDetalheQueryExtrato(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_EXTRATO_CONCILIADOS, filtro);
                listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheExtratoMapper());

                sql = mapperEntity.complementaDetalheQueryCartao(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_CARTAO_CONCILIADO, filtro);
                listaResultado.addAll(jdbcTemplate1.query(sql, parametros, new ConciliacaoCartaoDetalheCartaoMapper()));
            } else {
                switch (filtro.getCodigoMovimento()) {
                    case "":
                        sql = mapperEntity.complementaDetalheQueryExtrato(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_EXTRATO_CONCILIADOS, filtro);
                        listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheExtratoMapper());
                        sql = mapperEntity.complementaDetalheQueryCartao(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_CARTAO_CONCILIADO, filtro);
                        listaResultado.addAll(jdbcTemplate1.query(sql, parametros, new ConciliacaoCartaoDetalheCartaoMapper()));
                        break;
                    case "1":
                        sql = mapperEntity.complementaDetalheQueryExtrato(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_EXTRATO_CONCILIADOS, filtro);
                        listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheExtratoMapper());
                        break;
                    default:
                        sql = mapperEntity.complementaDetalheQueryCartao(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_CARTAO_CONCILIADO, filtro);
                        listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheCartaoMapper());
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.pesquisarRegistrosDetalhe - Erro ao buscar dados de conciliacao detalhe de cartao." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<ResumoConciliacaoCartaoEntity> buscarValoresExtratoNaoConciliados(FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoRepository.buscarValoresExtratoNaoConciliados");

        List<ResumoConciliacaoCartaoEntity> listaResultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = obtemParametrosResumoQuery(filtro);

            String sql = complementaResumoQuery(ConstantesConciliacaoCartao.QUERY_BUSCAR_VALORES_EXTRATO_RESUMIDO_NAO_CONCILIADO, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoResumoMapper());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.buscarValoresExtratoNaoConciliados - Erro ao buscar dados de conciliacao resumo de cartao." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<DetalheConciliacaoCartaoEntity> pesquisarRegistrosCartaoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro)
            throws Exception {
        LOGGER.info(">>ConciliacaoCartaoRepository.pesquisarRegistrosCartaoNaoConciliados");

        List<DetalheConciliacaoCartaoEntity> listaResultado;
        try {
            DetalheConciliacaoCartaoEntityMapper mapperEntity = new DetalheConciliacaoCartaoEntityMapper();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = obtemParametrosDetalheQuery(filtro);

            String sql = mapperEntity.complementaDetalheQueryCartao(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_CARTAO_NAO_CONCILIADOS, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheCartaoMapper2());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoDAOImpl.pesquisarRegistrosCartaoNaoConciliados - Erro ao buscar dados de detalhe de cartao." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<ResumoConciliacaoCartaoEntity> buscarValoresCartaoNaoConciliados(FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        LOGGER.info(">>ConciliacaoCartaoRepository.buscarValoresCartaoNaoConciliados");

        List<ResumoConciliacaoCartaoEntity> listaResultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = obtemParametrosResumoQuery(filtro);

            String sql = complementaResumoQuery1(ConstantesConciliacaoCartao.QUERY_BUSCAR_VALOR_CARTAO_RESUMIDO_NAO_CONCILIADO, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoResumoMapper2());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.buscarValoresCartaoNaoConciliados - Erro ao buscar dados de conciliacao resumo de cartao." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<DetalheConciliacaoCartaoEntity> pesquisarRegistrosExtratoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro)
            throws Exception {
        LOGGER.info(">>ConciliacaoCartaoRepository.pesquisarRegistrosExtratoNaoConciliados");

        List<DetalheConciliacaoCartaoEntity> listaResultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = obtemParametrosDetalheQuery(filtro);

            String sql = complementaDetalheQueryExtrato(ConstantesConciliacaoCartao.QUERY_BUSCAR_REGISTROS_EXTRATO_NAO_CONCILIADOS, filtro);

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheExtratoMapper());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.pesquisarRegistrosExtratoNaoConciliados - Erro ao buscar dados de detalhe de extrato." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    private String complementaResumoQuery1(String sql, FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())) {
            query.append(" AND C.NRO_BANCO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(" AND B.COD_OPERADORA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            query.append(" AND C.NRO_AGENCIA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            query.append(" AND C.NRO_CONTA_CORRENTE LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            query.append(" AND A.COD_DATA_CREDITO >= ? AND A.COD_DATA_CREDITO <= ?");
        }

        query.append(" GROUP BY "
                + "A.COD_OPERADORA, "
                + "B.NME_EXIBICAO_PORTAL, SUBSTRB(A.COD_DATA_CREDITO, 1, 6), "
                + "C.NRO_BANCO, C.NRO_AGENCIA, C.NRO_CONTA_CORRENTE, A.STA_CONCILIADO");

        return query.toString();
    }

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    private String complementaResumoQuery(String sql, FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())) {
            query.append(" AND NRO_BANCO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(" AND B.COD_OPERADORA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            query.append(" AND NRO_AGENCIA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            query.append(" AND NRO_CONTA_CORRENTE LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            query.append(" AND A.DTA_PAGAMENTO >= ? AND A.DTA_PAGAMENTO <= ?");
        }

        query.append(" GROUP BY "
                + "A.COD_CONTA_BANCO, A.COD_OPERADORA, "
                + "B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                + "NRO_BANCO, NRO_AGENCIA, NRO_CONTA_CORRENTE, A.STA_CONCILIADO");

        return query.toString();
    }

    /**
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosResumoQuery(FiltroTelasConciliacaoCartaoResumoDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            paramentrosQtd++;
            paramentrosQtd++;
        }

        return paramentrosQtd;
    }

    public Long obterIdRegistrosConciliados() {

        LOGGER.info(">>ConciliacaoCartaoDAOImpl.obterIdRegistrosConciliados");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = ConstantesConciliacaoCartao.SELECT_ID_REGISTRO_CONCILIADO;

        Long idRegistrosConciliados = jdbcTemplate.queryForObject(sql, Long.class);

        LOGGER.info("<<ConciliacaoCartaoDAOImpl.obterIdRegistrosConciliados");

        return idRegistrosConciliados;

    }

    public void atualizarConciliacaoCartaoDetalhe(DetalheConciliacaoCartaoEntity registro) {

        LOGGER.info(">>ConciliacaoCartaoDAOImpl.atualizarConciliacaoCartaoDetalhe");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = ConstantesConciliacaoCartao.UPDATE_REGISTRO_CARTAO;

        Object[] parameters = new Object[7];

        parameters[0] = registro.getStatusConciliado();
        parameters[1] = registro.getStatus();
        parameters[2] = registro.getDataConciliado();
        parameters[3] = registro.getTipoConciliacao();
        parameters[4] = registro.getStatusPreProcessamento();
        parameters[5] = registro.getNroGrupoConciliado();
        parameters[6] = registro.getHashValue();

        jdbcTemplate.update(sql, parameters);

        LOGGER.info("<<ConciliacaoCartaoDAOImpl.atualizarConciliacaoCartaoDetalhe");
    }

    public void atualizarConciliacaoExtratoDetalhe(DetalheConciliacaoCartaoEntity registro) {

        LOGGER.info(">>ConciliacaoCartaoDAOImpl.atualizarConciliacaoCartaoDetalhe");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = ConstantesConciliacaoCartao.UPDATE_REGISTRO_EXTRATO;

        Object[] parameters = new Object[8];

        parameters[0] = registro.getStatusConciliado();
        parameters[1] = registro.getStatus();
        parameters[2] = registro.getDataConciliado();
        parameters[3] = registro.getTipoConciliacao();
        parameters[4] = registro.getStatusPreProcessamento();
        parameters[5] = registro.getMotivoConciliacao();
        parameters[6] = registro.getNroGrupoConciliado();
        parameters[7] = registro.getId();

        jdbcTemplate.update(sql, parameters);

        LOGGER.info("<<ConciliacaoCartaoDAOImpl.atualizarConciliacaoCartaoDetalhe");
    }

    /**
     *
     * @param filtro
     * @return
     */
    private Object[] obtemParametrosResumoQuery(FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosResumoQuery(filtro)];
        int indiceAtivo = 0;

        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroBanco());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            parametros[indiceAtivo] = filtro.getCodigoOperadora();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroAgencia());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            parametros[indiceAtivo] = "%" + filtro.getNumeroConta() + "%";
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            parametros[indiceAtivo] = filtro.getStatusConciliado();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "01";
            indiceAtivo++;

            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "31";
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
    private String complementaDetalheQueryExtrato(String sql, FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())) {
            query.append(" AND D.NRO_BANCO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(" AND B.COD_OPERADORA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            query.append(" AND A.STA_CONCILIACAO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            query.append(" AND D.NRO_AGENCIA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            query.append(" AND D.NRO_CONTA_CORRENTE LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            query.append(" AND A.NME_ARQUIVO LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            query.append(" AND A.VLR_EXTR_TOTAL >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            query.append(" AND A.VLR_EXTR_TOTAL <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            query.append(" AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'yyyymmdd') >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            query.append(" AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'yyyymmdd') <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }

        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            query.append(" AND A.ID_FIN_CONTABIL = ?");
        }

        return query.toString();
    }

    /**
     *
     * @param filtro
     * @return
     */
    private Object[] obtemParametrosDetalheQuery(FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosDetalheQuery(filtro)];
        int indiceAtivo = 0;

        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            parametros[indiceAtivo] = Integer.valueOf(filtro.getNumeroBanco());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            parametros[indiceAtivo] = filtro.getCodigoOperadora();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            parametros[indiceAtivo] = filtro.getStatusConciliacao();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            parametros[indiceAtivo] = Integer.valueOf(filtro.getNumeroAgencia());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            parametros[indiceAtivo] = "%" + filtro.getNumeroConta() + "%";
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            parametros[indiceAtivo] = "%" + filtro.getNomeArquivo() + "%";
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            parametros[indiceAtivo] = Double.parseDouble(filtro.getValorInicial().replace(".", "").replace(",", ".")) * 100;
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            parametros[indiceAtivo] = Double.parseDouble(filtro.getValorFinal().replace(".", "").replace(",", ".")) * 100;
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            parametros[indiceAtivo] = filtro.getDataInicial();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            parametros[indiceAtivo] = filtro.getDataFinal();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            parametros[indiceAtivo] = filtro.getStatusConciliado();
            indiceAtivo++;
        }

        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            parametros[indiceAtivo] = filtro.getId();
            indiceAtivo++;
        }

        return parametros;
    }

    /**
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosDetalheQuery(FiltroTelasConciliacaoCartaoDetalheDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            paramentrosQtd++;
        }
        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            paramentrosQtd++;
        }

        return paramentrosQtd;
    }

    public List<DetalheConciliacaoCartaoEntity> buscarRegistroExtratoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {

        LOGGER.info(">>ConciliacaoCartaoRepository.buscarRegistroExtratoPorId");

        List<DetalheConciliacaoCartaoEntity> listaResultado = new ArrayList<DetalheConciliacaoCartaoEntity>();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = new Object[2];

            parametros[0] = filtro.getEmpId();
            parametros[1] = filtro.getNroGrupoConciliado();

            String sql = ConstantesConciliacaoCartao.QUERY_OBTER_EXTRATO_POR_ID;

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheExtratoMapper());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.buscarRegistroExtratoPorId - Erro ao buscar dados registro de extrato por id." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

    public List<DetalheConciliacaoCartaoEntity> buscarRegistroCartaoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {

        LOGGER.info(">>ConciliacaoCartaoRepository.buscarRegistroCartaoPorId");

        List<DetalheConciliacaoCartaoEntity> listaResultado = new ArrayList<DetalheConciliacaoCartaoEntity>();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Object[] parametros = new Object[2];

            parametros[0] = filtro.getEmpId();
            parametros[1] = filtro.getNroGrupoConciliado();

            String sql = ConstantesConciliacaoCartao.QUERY_OBTER_CARTAO_POR_ID;

            listaResultado = jdbcTemplate.query(sql, parametros, new ConciliacaoCartaoDetalheCartaoMapper());
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoRepository.buscarRegistroCartaoPorId - Erro ao buscar registro de cartao por id." + e.getMessage());
            throw e;
        }

        return listaResultado;
    }

}
