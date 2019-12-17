package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.BaixaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroBaixaDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesDB;
import br.com.accesstage.trustion.util.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author raphael
 */
@Repository
public class BaixaRepository {

    private static final String MSG_ERRO_GERACAO_ARQUIVO = "BaixaDAO: Ocorreu um erro ao buscar os "
            + "dados para geração do arquivo de Vendas Conciliadas.";

    private static final String MSG_ERRO_GERACAO_ARQUIVO_PARCELA = MSG_ERRO_GERACAO_ARQUIVO + " - Parcela.";

    private static final String QUERY_AND_PONTO_VENDA = " AND (pv.cod_loja = %d OR f.cod_ponto_venda = %d)";

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<BaixaDTO> consultaConcParcela(FiltroBaixaDTO filtro) {
        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasConciliaParcelaTela.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() > 0L) {
                query = query.replace(":pvOuLoja",
                        String.format(QUERY_AND_PONTO_VENDA, filtro.getCodLojaOuPv(), filtro.getCodLojaOuPv()));
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasConciliaParcelaTela.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<BaixaDTO> result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();
                        baixa.setDtaVenda(rs.getInt("DTA_VENDA"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        // baixa.setIdtOperadora(rs.getString("IDT_OPERADORA"));
                        baixa.setIdtProduto(rs.getString("IDT_PRODUTO"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        // baixa.setNroEstabelecimento(rs.getString("COD_PONTO_VENDA"));
                        // baixa.setVlrLiquido(rs.getLong("VLR_LIQUIDO"));
                        // baixa.setVlrComissao(rs.getLong("VLR_COMISSAO"));
                        // baixa.setVlrTxAntecipacao(rs.getLong("VLR_TAXA_ANTECIPACAO"));
                        // baixa.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));
                        // baixa.setDtaCredito(rs.getInt("DTA_CREDITO"));
                        // baixa.setNroBanco(rs.getInt("NRO_BANCO"));
                        // baixa.setNroCC(rs.getString("NRO_CC"));
                        // baixa.setNroAg(rs.getInt("NRO_AG"));
                        // baixa.setDscLote(rs.getString("DSC_LOTE"));
                        // baixa.setDdsCliente(rs.getString("DDS_CLIENTE"));
                        baixa.setCodPontoVenda(rs.getLong("COD_PONTO_VENDA"));
                        baixa.setDscOperadora(rs.getString("DSC_OPERADORA"));
                        baixa.setDscProduto(rs.getString("DSC_PRODUTO"));

                        result.add(baixa);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
            throw new RuntimeException(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
        }
    }

    @Transactional(readOnly = true)
    public List<BaixaDTO> consulta(FiltroBaixaDTO filtro) {
        List<BaixaDTO> result = null;

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasTela.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() > 0L) {
                query = query.replace(":pvOuLoja", " AND (pv.cod_loja = " + filtro.getCodLojaOuPv()
                        + " OR f.cod_ponto_venda = " + filtro.getCodLojaOuPv() + ")");
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasTela.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();
                        // baixa.setNroEstabelecimento(rs.getString("NRO_TERMINAL"));
                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        // baixa.setVlrLiquido(rs.getLong("VLR_LIQUIDO"));
                        baixa.setVlrComissao(rs.getLong("VLR_COMISSAO"));
                        baixa.setVlrTxAntecipacao(rs.getLong("VLR_TAXA_ANTECIPACAO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));
                        baixa.setDtaVenda(rs.getInt("DTA_VENDA"));
                        // baixa.setDtaCredito(rs.getInt("DTA_CREDITO"));
                        baixa.setNroBanco(rs.getInt("NRO_BANCO"));
                        baixa.setNroCC(rs.getString("NRO_CC"));
                        baixa.setNroAg(rs.getInt("NRO_AG"));
                        // baixa.setIdtOperadora(rs.getString("IDT_OPERADORA"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        baixa.setIdtProduto(rs.getString("IDT_PRODUTO"));
                        // baixa.setDscLote(rs.getString("DSC_LOTE"));
                        // baixa.setDdsCliente(rs.getString("DDS_CLIENTE"));
                        baixa.setDscProduto(rs.getString("DSC_PRODUTO"));
                        baixa.setDscOperadora(rs.getString("DSC_OPERADORA"));
                        baixa.setCodPontoVenda(rs.getLong("COD_PONTO_VENDA"));

                        result.add(baixa);
                    }
                }
            }
        } catch (Exception e) {
            String msg = "BaixaDAO: Ocorreu um erro ao buscar os dados para geração do arquivo CSV de Vendas Conciliadas.";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<BaixaDTO> consultaLayout2(FiltroBaixaDTO filtro) {
        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasLayout2.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() > 0L) {
                query = query.replace(":pvOuLoja",
                        String.format(QUERY_AND_PONTO_VENDA, filtro.getCodLojaOuPv(), filtro.getCodLojaOuPv()));
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasLayout2.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<BaixaDTO> result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();

                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        baixa.setVlrLiquido(rs.getLong("VLR_LIQUIDO"));
                        baixa.setVlrComissao(rs.getLong("VLR_COMISSAO"));
                        baixa.setVlrTxAntecipacao(rs.getLong("VLR_TAXA_ANTECIPACAO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));
                        baixa.setDtaVenda(rs.getInt("DTA_VENDA"));
                        baixa.setDtaCredito(rs.getInt("DTA_CREDITO"));
                        baixa.setNroBanco(rs.getInt("NRO_BANCO"));
                        baixa.setNroCC(rs.getString("NRO_CC"));
                        baixa.setNroAg(rs.getInt("NRO_AG"));
                        baixa.setIdtOperadora(rs.getString("IDT_OPERADORA"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        baixa.setIdtProduto(rs.getString("IDT_PRODUTO"));
                        baixa.setDscLote(rs.getString("DSC_LOTE"));
                        baixa.setDdsCliente(rs.getString("DDS_CLIENTE"));
                        String cartaoFormatado = Funcoes.formataNroCartao(rs.getString("DSC_NUMEROCARTAO"),
                                rs.getString("SGL_OPERADORA"));
                        baixa.setDscNumeroCartao(cartaoFormatado);
                        // baixa.setNroEstabelecimento(rs.getString("COD_PONTO_VENDA"));
                        baixa.setNroEstabelecimento(rs.getString("NRO_TERMINAL"));
                        baixa.setDscProduto(rs.getString("COD_PRODUTO"));
                        baixa.setDscOperadora(rs.getString("COD_OPERADORA"));
                        baixa.setIdConciliacao(rs.getString("DSC_AREA_CLIENTE"));

                        result.add(baixa);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_GERACAO_ARQUIVO, e);
            throw new RuntimeException(MSG_ERRO_GERACAO_ARQUIVO, e);
        }
    }

    @Transactional(readOnly = true)
    public List<BaixaDTO> consultaLayout2ConcParcela(FiltroBaixaDTO filtro) {
        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasLayout2ConciliaParcela.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() > 0L) {
                query = query.replace(":pvOuLoja",
                        String.format(QUERY_AND_PONTO_VENDA, filtro.getCodLojaOuPv(), filtro.getCodLojaOuPv()));
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasLayout2ConciliaParcela.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<BaixaDTO> result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();

                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        baixa.setVlrLiquido(rs.getLong("VLR_LIQUIDO"));
                        baixa.setVlrComissao(rs.getLong("VLR_COMISSAO"));
                        baixa.setVlrTxAntecipacao(rs.getLong("VLR_TAXA_ANTECIPACAO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));
                        baixa.setDtaVenda(rs.getInt("DTA_VENDA"));
                        baixa.setDtaCredito(rs.getInt("DTA_CREDITO"));
                        baixa.setNroBanco(rs.getInt("NRO_BANCO"));
                        baixa.setNroCC(rs.getString("NRO_CC"));
                        baixa.setNroAg(rs.getInt("NRO_AG"));
                        baixa.setIdtOperadora(rs.getString("IDT_OPERADORA"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        baixa.setIdtProduto(rs.getString("IDT_PRODUTO"));
                        baixa.setDscLote(rs.getString("DSC_LOTE"));
                        baixa.setDdsCliente(rs.getString("DDS_CLIENTE"));
                        String cartaoFormatado = Funcoes.formataNroCartao(rs.getString("DSC_NUMEROCARTAO"),
                                rs.getString("SGL_OPERADORA"));
                        baixa.setDscNumeroCartao(cartaoFormatado);
                        // baixa.setNroEstabelecimento(rs.getString("COD_PONTO_VENDA"));
                        baixa.setNroEstabelecimento(rs.getString("NRO_TERMINAL"));
                        baixa.setDscProduto(rs.getString("COD_PRODUTO"));
                        baixa.setDscOperadora(rs.getString("COD_OPERADORA"));
                        baixa.setIdConciliacao(rs.getString("DSC_AREA_CLIENTE"));

                        result.add(baixa);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
            throw new RuntimeException(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
        }
    }

    @Transactional(readOnly = true)
    public List<BaixaDTO> consultaLayoutCSV(FiltroBaixaDTO filtro) {
        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasCSV.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() != -1L) {
                query = query.replace(":pvOuLoja",
                        String.format(QUERY_AND_PONTO_VENDA, filtro.getCodLojaOuPv(), filtro.getCodLojaOuPv()));
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasCSV.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<BaixaDTO> result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();

                        baixa.setNroEstabelecimento(rs.getString("NRO_TERMINAL"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        baixa.setDscLote(rs.getString("DSC_LOTE"));
                        baixa.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setDtaVendaDt(Funcoes.formataDataRelatorio(rs.getLong("DTA_VENDA")));
                        baixa.setDtaCreditoDt(Funcoes.formataDataRelatorio(rs.getLong("DTA_CREDITO")));
                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        baixa.setVlrComissaoD(Funcoes.dividePorCem(rs.getDouble("VLR_COMISSAO")));
                        baixa.setVlrLiquidoD(Funcoes.dividePorCem(rs.getDouble("VLR_LIQUIDO")));
                        baixa.setDscProduto(rs.getString("DSC_PRODUTO"));
                        baixa.setDscOperadora(rs.getString("DSC_OPERADORA"));
                        baixa.setNroAg(rs.getInt("NRO_AG"));
                        baixa.setNroBanco(rs.getInt("NRO_BANCO"));
                        baixa.setNroCC(rs.getString("NRO_CC"));
                        baixa.setIdConciliacao(rs.getString("DSC_AREA_CLIENTE"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));

                        result.add(baixa);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            String msg = "BaixaDAO: Ocorreu um erro ao buscar os dados para geracao do arquivo CSV de Vendas Conciliadas.";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Transactional(readOnly = true)
    public List<BaixaDTO> consultaLayoutCSVConcParcela(FiltroBaixaDTO filtro) {
        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_VENDAS_CONCILIADAS, "BaixasConciliaParcelaCSV.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() != -1L) {
                query = query.replace(":pvOuLoja",
                        String.format(QUERY_AND_PONTO_VENDA, filtro.getCodLojaOuPv(), filtro.getCodLojaOuPv()));
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            LOGGER.info("QUERY: (BaixasConciliaParcelaCSV.sql) - " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstmt.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<BaixaDTO> result = new ArrayList<>();

                    while (rs.next()) {
                        BaixaDTO baixa = new BaixaDTO();
                        baixa.setDtaVenda(rs.getInt("DTA_VENDA"));
                        baixa.setNmeLoja(rs.getString("NME_LOJA"));
                        // baixas.setIdtOperadora(rs.getString("IDT_OPERADORA"));
                        baixa.setIdtProduto(rs.getString("IDT_PRODUTO"));
                        baixa.setNroPlano(rs.getInt("NRO_PLANO"));
                        baixa.setNroNsu(rs.getLong("NRO_NSU"));
                        baixa.setCodAutorizacao(rs.getString("COD_AUTORIZACAO"));
                        baixa.setVlrBruto(rs.getLong("VLR_BRUTO"));
                        baixa.setVlrBrutoD(Funcoes.dividePorCem(rs.getDouble("VLR_BRUTO")));
                        // baixas.setNroEstabelecimento(rs.getString("COD_PONTO_VENDA"));
                        // baixas.setVlrLiquido(rs.getLong("VLR_LIQUIDO"));
                        // baixas.setVlrComissao(rs.getLong("VLR_COMISSAO"));
                        // baixas.setVlrTxAntecipacao(rs.getLong("VLR_TAXA_ANTECIPACAO"));
                        // baixas.setNroParcela(rs.getInt("NRO_PARCELA"));
                        baixa.setCodNatureza(rs.getInt("COD_NATUREZA"));
                        // baixas.setDtaCredito(rs.getInt("DTA_CREDITO"));
                        // baixas.setNroBanco(rs.getInt("NRO_BANCO"));
                        // baixas.setNroCC(rs.getString("NRO_CC"));
                        // baixas.setNroAg(rs.getInt("NRO_AG"));
                        // baixas.setDscLote(rs.getString("DSC_LOTE"));
                        // baixas.setDdsCliente(rs.getString("DDS_CLIENTE"));

                        result.add(baixa);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
            throw new RuntimeException(MSG_ERRO_GERACAO_ARQUIVO_PARCELA, e);
        }
    }
}
