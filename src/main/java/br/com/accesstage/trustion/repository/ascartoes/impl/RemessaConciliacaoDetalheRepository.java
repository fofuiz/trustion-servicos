package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.RemessaConciliacaoDetalhe;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.RemessaConciliacaoDetalheDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesDB;
import br.com.accesstage.trustion.util.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.internal.SessionImpl;

@Repository
public class RemessaConciliacaoDetalheRepository {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    /**
     * Lista remessas pendentes de envio.
     *
     * @param codLoja
     * @param empId
     * @return lista de RemessaConciliacaoDetalheDTO
     */
    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> listaRemessasPendentes(Long codLoja, Long empId) {

        List<RemessaConciliacaoDetalheDTO> lista = new ArrayList<>();

        String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaRemessaLoja.sql");

        query = query.replace(":CODLOJA", codLoja.toString());
        query = query.replace(":EMPID", empId.toString());

        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {
         
            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();

                    Long nroDt = result.getLong("DTVENDA");
                    Date dtVenda = null;
                    try {
                        dtVenda = dateFormat.parse(nroDt.toString());
                    } catch (Exception e) {
                    }
                    remessaDetalhe.setDtaVenda(dtVenda);
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VLRBRUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("NROPLANO"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setCodDetalhe(result.getLong("COD_DETALHE"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSCAREACLIENTE"));
                    remessaDetalhe.setCodOperadora(result.getLong("COD_OPERADORA"));
                    lista.add(remessaDetalhe);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe!", e);
        }
        return lista;
    }

    /**
     * Lista transações a serem conciliadas.
     *
     * @param codDetalhe
     * @param empId
     * @param data
     * @return lista de RemessaConciliacaoDetalheDTO
     */
    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> listaTransacoesASeremConciliadas(Long codDetalhe, Long empId, Date data) {

        List<RemessaConciliacaoDetalheDTO> lista = new ArrayList<>();

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.setTime(data);
        beforeDate.add(Calendar.DAY_OF_YEAR, -10);

        Calendar afterDate = Calendar.getInstance();
        afterDate.setTime(data);
        afterDate.add(Calendar.DAY_OF_YEAR, 10);

        String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaTransacoesASerConciliada.sql");
        LOGGER.info("QUERY: (buscaTransacoesASerConciliada.sql) -" + query);

        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setLong(1, codDetalhe);
            pstm.setLong(2, empId);
            pstm.setString(3, dateFormat.format(beforeDate.getTime()));
            pstm.setString(4, dateFormat.format(afterDate.getTime()));

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();
                    Long dataVenda = result.getLong("DATA");

                    if (dataVenda != null && dataVenda.intValue() != 0) {
                        remessaDetalhe.setDtaVenda(dateFormat.parse(String.valueOf(dataVenda)));
                    }

                    remessaDetalhe.setNmeLoja(result.getString("NOME_LOJA"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("PLANO"));
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VALOR_BRUTO"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSC_AREA_CLIENTE"));
                    remessaDetalhe.setCodDetalhe(result.getLong("COD_DETALHE"));
                    lista.add(remessaDetalhe);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe! - listarTransacoesASerConciliada", e);
        }
        return lista;
    }

    /**
     * Lista possíveis transações.
     *
     * @param dscAutorizacao
     * @param codNsu
     * @param empId
     * @param data
     * @param codLoja
     * @param codOperadora
     * @return lista de RemessaConciliacaoDetalheDTO
     */
    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> listaPossiveisTransacoes(String dscAutorizacao, Long codNsu, Long empId, Date data,
            Long codLoja, Long codOperadora) {
        List<RemessaConciliacaoDetalheDTO> lista = new ArrayList<>();

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.setTime(data);
        beforeDate.add(Calendar.DAY_OF_YEAR, -10);

        Calendar afterDate = Calendar.getInstance();
        afterDate.setTime(data);
        afterDate.add(Calendar.DAY_OF_YEAR, 10);

        String query = null;

        if (codNsu != 0L && dscAutorizacao != null) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaPossiveisTransacoes.sql");
            LOGGER.info("QUERY: (consulta-buscaPossiveisTransacoes.sql) -" + query);
        } else if (dscAutorizacao != null) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaPossiveisTransacoesPorAutorizacao.sql");
            LOGGER.info("QUERY: (consulta-buscaPossiveisTransacoesPorAutorizacao.sql) -" + query);
        } else if (codNsu != 0L) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaPossiveisTransacoesPorNSU.sql");
            LOGGER.info("QUERY: (consulta-buscaPossiveisTransacoesPorNSU.sql) -" + query);
        }

        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setLong(1, empId);

            if (codNsu != 0L && dscAutorizacao != null) {
                pstm.setString(2, dscAutorizacao);
                pstm.setLong(3, codNsu);
                pstm.setString(4, dateFormat.format(beforeDate.getTime()));
                pstm.setString(5, dateFormat.format(afterDate.getTime()));

            } else if (dscAutorizacao != null) {
                pstm.setString(2, dscAutorizacao);
                pstm.setString(3, dateFormat.format(beforeDate.getTime()));
                pstm.setString(4, dateFormat.format(afterDate.getTime()));

            } else if (codNsu != 0L) {
                pstm.setLong(2, codNsu);
                pstm.setString(3, dateFormat.format(beforeDate.getTime()));
                pstm.setString(4, dateFormat.format(afterDate.getTime()));
            }

            try (ResultSet result = pstm.executeQuery()) {
                int rowKey = 1;

                while (result.next()) {
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();

                    Long dataVenda = result.getLong("DATA");
                    if (dataVenda != null && dataVenda.intValue() != 0) {
                        remessaDetalhe.setDtaVenda(dateFormat.parse(String.valueOf(dataVenda)));
                    }

                    remessaDetalhe.setNmeLoja(result.getString("NOME_LOJA"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("PLANO"));
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VALOR_BRUTO"));
                    remessaDetalhe.setCodOperadora(result.getLong("COD_OPERADORA"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSC_AREA_CLIENTE"));
                    remessaDetalhe.setQuantidadeItensAgrupado(result.getLong("QUANTIDADE_ITENS_AGRUPADOS"));
                    remessaDetalhe.setRowKey(rowKey++);

                    lista.add(remessaDetalhe);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe! - listarPossiveisTransacoes", e);
        }
        return lista;
    }

    /**
     * Busca uma remessa a partir de seu código.
     *
     * @param codDetalhe
     * @return remessa
     */
    @Transactional(readOnly = true)
    public RemessaConciliacaoDetalhe findById(Long codDetalhe) {
        RemessaConciliacaoDetalhe remessaListarPorID = new RemessaConciliacaoDetalhe();

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL, "listarPorId.sql");
            query = query.replace(":COD_DETALHE", codDetalhe.toString());

            Query q = em.createQuery(query);
            remessaListarPorID = (RemessaConciliacaoDetalhe) q.getSingleResult();

        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe!", e);
            throw e;
        }
        return remessaListarPorID;
    }

    /**
     * A lógica deste método tem que ser praticamente igual ao do método
     * listarPossiveisTransacoes, salvo alguns parâmetros a mais de entrada.
     *
     * @param dscAutorizacao
     * @param codNsu
     * @param empId
     * @param data
     * @param codOperadora
     * @param nroPlano
     * @param codProduto
     * @return lista de RemessaConciliacaoDetalheDTO
     */
    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> desagrupaTransacoes(String dscAutorizacao, Long codNsu, Long empId, Date data,
            Long codOperadora, Long nroPlano, String codProduto) {
        List<RemessaConciliacaoDetalheDTO> lista = new ArrayList<>();

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.setTime(data);
        beforeDate.add(Calendar.DAY_OF_YEAR, -10);

        Calendar afterDate = Calendar.getInstance();
        afterDate.setTime(data);
        afterDate.add(Calendar.DAY_OF_YEAR, 10);

        String query = null;

        if (codNsu != 0L && dscAutorizacao != null) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL,
                    "buscaPossiveisTransacoes_desagruparTransacoes.sql");
        } else if (dscAutorizacao != null) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL,
                    "buscaPossiveisTransacoesPorAutorizacao_desagruparTransacoes.sql");
        } else if (codNsu != 0L) {
            query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL,
                    "buscaPossiveisTransacoesPorNSU_desagruparTransacoes.sql");
        }

        LOGGER.info("desagrupaTransacoes QUERY: " + query);
        
        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setLong(1, empId);
            pstm.setLong(2, codOperadora);
            pstm.setLong(3, nroPlano);
            pstm.setString(4, codProduto);

            if (codNsu != 0L && dscAutorizacao != null) {
                pstm.setString(5, dscAutorizacao);
                pstm.setLong(6, codNsu);
                pstm.setString(7, dateFormat.format(beforeDate.getTime()));
                pstm.setString(8, dateFormat.format(afterDate.getTime()));

            } else if (dscAutorizacao != null) {
                pstm.setString(5, dscAutorizacao);
                pstm.setString(6, dateFormat.format(beforeDate.getTime()));
                pstm.setString(7, dateFormat.format(afterDate.getTime()));

            } else if (codNsu != 0L) {
                pstm.setLong(5, codNsu);
                pstm.setString(6, dateFormat.format(beforeDate.getTime()));
                pstm.setString(7, dateFormat.format(afterDate.getTime()));
            }

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();

                    Long dataVenda = result.getLong("DATA");
                    if (dataVenda != null && dataVenda.intValue() != 0) {
                        remessaDetalhe.setDtaVenda(dateFormat.parse(String.valueOf(dataVenda)));
                    }

                    remessaDetalhe.setCodLoja(result.getLong("COD_LOJA"));
                    remessaDetalhe.setNmeLoja(result.getString("NOME_LOJA"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("PLANO"));
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setCodOperadora(result.getLong("COD_OPERADORA"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSC_AREA_CLIENTE"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VALOR_BRUTO"));
                    remessaDetalhe.setHashValue(result.getString("HASH_VALUE"));

                    lista.add(remessaDetalhe);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe! - listarPossiveisTransacoes", e);
        }
        return lista;
    }

    /**
     * Busca uma remessa a partir do campo dscAutorizacao.
     *
     * @param dscAutorizacao
     * @return remessa
     */
    @Transactional(readOnly = true)
    public RemessaConciliacaoDetalhe findByDscAutorizacao(String dscAutorizacao) {
        RemessaConciliacaoDetalhe remessaListarAutorizacao = new RemessaConciliacaoDetalhe();

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "listarAutorizacao.sql");
            query = query.replace(":DSC_AUTORIZACAO", "'" + dscAutorizacao + "'");

            Query q = em.createQuery(query);
            remessaListarAutorizacao = (RemessaConciliacaoDetalhe) q.getSingleResult();

        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe!", e);
        }
        return remessaListarAutorizacao;
    }

    /**
     * Atualiza o status da remessa.
     *
     * @param codRemessaHeader
     * @param status
     */
    @Transactional
    public void atualizaStatusRemessa(Long codRemessaHeader, String status) {
        String sql = "UPDATE REMESSA_CONCILIACAO_DETALHE set STA_CONCILIACAO=?1 WHERE COD_HEADER=?2";

        em.joinTransaction();
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, status);
        query.setParameter(2, codRemessaHeader);
        query.executeUpdate();
    }

    /**
     * Persiste ou atualiza uma remessa.
     *
     * @param remessaConciliacaoDetalhe
     * @return remessa, em caso de atualização
     */
    @Transactional
    public RemessaConciliacaoDetalhe salvaRemessa(RemessaConciliacaoDetalhe remessaConciliacaoDetalhe) {
        if (remessaConciliacaoDetalhe.getCodDetalhe() == null) {
            em.persist(remessaConciliacaoDetalhe);
        } else {
            em.merge(remessaConciliacaoDetalhe);
        }
        return remessaConciliacaoDetalhe;
    }

    /**
     * Remove uma remessa a partir de seu código.
     *
     * @param codRemessaDetalhe
     */
    @Transactional
    public void removeRemessa(Long codRemessaDetalhe) {
        RemessaConciliacaoDetalhe remessa = em.find(RemessaConciliacaoDetalhe.class, codRemessaDetalhe);
        em.remove(remessa);
    }

    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> remessaDesconciliar(String hashValue) {

        List<RemessaConciliacaoDetalheDTO> remessa = new ArrayList<>();

        String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscaRemessaTransacoesDesconciliar.sql");

        LOGGER.info("QUERY: (buscaRemessaTransacoesDesconciliar.sql) -" + query);

        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, hashValue);

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();
                    Long dataVenda = result.getLong("DATA");

                    if (dataVenda != null && dataVenda.intValue() != 0) {
                        remessaDetalhe.setDtaVenda(dateFormat.parse(String.valueOf(dataVenda)));
                    }

                    remessaDetalhe.setNmeLoja(result.getString("NOME_LOJA"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("PLANO"));
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VALOR_BRUTO"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSC_AREA_CLIENTE"));
                    remessaDetalhe.setHashValue(hashValue);

                    remessa.add(remessaDetalhe);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe! - listarTransacoesASerConciliada", e);
        }

        return remessa;

    }

    @Transactional(readOnly = true)
    public List<RemessaConciliacaoDetalheDTO> fatoTransacaoDesconciliar(GestaoVendasDTO filtro) {

        List<RemessaConciliacaoDetalheDTO> remessa = new ArrayList<>();

        String query = null;

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.setTime(filtro.getData());
        beforeDate.add(Calendar.DAY_OF_YEAR, -10);

        Calendar afterDate = Calendar.getInstance();
        afterDate.setTime(filtro.getData());
        afterDate.add(Calendar.DAY_OF_YEAR, 10);

        LOGGER.info(" Inicio - " + dateFormat.format(beforeDate.getTime()));
        LOGGER.info(" FIM - " + dateFormat.format(afterDate.getTime()));

        LOGGER.info(" EMPID - " + Funcoes.formataArray(filtro.getListaEmpresas()));
        LOGGER.info(" NSU - " + filtro.getNsu());
        LOGGER.info(" CODIGO AUTORIZACAO - " + filtro.getCodAutorizacao());
        LOGGER.info("*********************************************************");

        query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_REMESSA_MANUAL, "buscarTransacoesFatoDesconciliar.sql");

        query = query.replace(":empid", Funcoes.formataArray(filtro.getListaEmpresas()));
        query = query.replace(":nroNsu", filtro.getNsu());
        query = query.replace(":dataInicio", dateFormat.format(beforeDate.getTime()));
        query = query.replace(":dataFim", dateFormat.format(afterDate.getTime()));

        try (Connection conn =  em.unwrap(SessionImpl.class).connection(); PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, filtro.getCodAutorizacao());

            LOGGER.info("QUERY: (buscarTransacoesFatoDesconciliar.sql) -" + query);

            try (ResultSet result = pstm.executeQuery()) {
                int rowKey = 1;

                while (result.next()) {

                    LOGGER.info(" Retorno da query - fatoTransacaoDesconciliar()");
                    
                    RemessaConciliacaoDetalheDTO remessaDetalhe = new RemessaConciliacaoDetalheDTO();

                    Long dataVenda = result.getLong("DATA");
                    if (dataVenda != null && dataVenda.intValue() != 0) {
                        remessaDetalhe.setDtaVenda(dateFormat.parse(String.valueOf(dataVenda)));
                    }
                    remessaDetalhe.setNmeLoja(result.getString("NOME_LOJA"));
                    remessaDetalhe.setNomeProduto(result.getString("PRODUTO"));
                    remessaDetalhe.setNroPlano(result.getLong("PLANO"));
                    remessaDetalhe.setCodNsu(result.getLong("NSU"));
                    remessaDetalhe.setDscAutorizacao(result.getString("AUTORIZACAO"));
                    remessaDetalhe.setVlrBruto(result.getBigDecimal("VALOR_BRUTO"));
                    remessaDetalhe.setCodOperadora(result.getLong("COD_OPERADORA"));
                    remessaDetalhe.setDscAreaCliente(result.getString("DSC_AREA_CLIENTE"));
                    remessaDetalhe.setQuantidadeItensAgrupado(result.getLong("QUANTIDADE_ITENS_AGRUPADOS"));
                    remessaDetalhe.setRowKey(rowKey++);

                    remessa.add(remessaDetalhe);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar Remessa Detalhe! - buscarTransacoesFatoDesconciliar.sql", e);
        }

        return remessa;

    }

    @Transactional
    public void desconciliarTransacoes(String hashValue) {
        String sql1 = "UPDATE REMESSA_CONCILIACAO_DETALHE set FLG_CONCILIACAO=3, DSC_AREA_CLIENTE = NULL, HASH_VALUE = NULL WHERE HASH_VALUE=?1";

        em.joinTransaction();
        Query query = em.createNativeQuery(sql1);
        query.setParameter(1, hashValue);
        query.executeUpdate();
        LOGGER.info("QUERY: (desconciliarTransacoes) -" + sql1 + ": " + hashValue);

        String sql2 = "UPDATE FATO_TRANSACAO set FLG_CONCILIACAO = 0, DSC_AREA_CLIENTE = NULL, DTA_CONCILIACAO = NULL, FLG_EXPORTADO = 0 WHERE HASH_VALUE=?1";

        Query query2 = em.createNativeQuery(sql2);
        query2.setParameter(1, hashValue);
        query2.executeUpdate();
        LOGGER.info("QUERY: (desconciliarTransacoes) -" + sql2 + ": " + hashValue);
    }
    
    @Transactional
    public void atualizarFlagConciliacaoOperadora(String hashValue, Long empid) {

        String sql2 = "UPDATE FATO_TRANSACAO f set f.FLG_CONCILIACAO=2 WHERE f.HASH_VALUE=?1 AND f.EMPID =?2";
        
        em.joinTransaction();
        Query query2 = em.createNativeQuery(sql2);
        query2.setParameter(1, hashValue);
        query2.setParameter(2, empid);
        query2.executeUpdate();

        LOGGER.info("QUERY: (atualizarFlagConciliacaoOperadora.sql) -" + sql2);
        LOGGER.info("QUERY: (atualizarFlagConciliacaoOperadora.sql) -" + hashValue);
        LOGGER.info("QUERY: (atualizarFlagConciliacaoOperadora.sql) -" + empid);

    }

    @Transactional
    public RemessaConciliacaoDetalhe merge(RemessaConciliacaoDetalhe remessa) {
        em.joinTransaction();
        return em.merge(remessa);
    }

}
