package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.DetalheBilheteDTO;
import br.com.accesstage.trustion.dto.ascartoes.DetalhesNsuDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesDB;
import br.com.accesstage.trustion.util.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;

@Repository
public class NsuRepository {

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    public List<DetalhesNsuDTO> consultaDetalhesNsu(GestaoVendasDTO filtro, DetalheBilheteDTO detalheBilhete) {

        List<DetalhesNsuDTO> result = new ArrayList<>();

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_NSU, "DetalheNsuGestaoVendas.sql");
            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            LOGGER.info("QUERY: (detalheNsuGestaoVendas.sql) -" + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setLong(1, Funcoes.formataDataRelatorio(filtro.getDtaVendaDt()));
                pstmt.setString(2, filtro.getNsu());
                pstmt.setString(3, filtro.getCodAutorizacao());

                try (ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        DetalhesNsuDTO detalhesNsu = new DetalhesNsuDTO();
                        detalhesNsu.setValorBruto(Funcoes.dividePorCem(rs.getDouble(1)));
                        detalhesNsu.setValorLiquido(Funcoes.dividePorCem(rs.getDouble(2)));
                        detalhesNsu.setTaxaAntecipacao(Funcoes.dividePorCem(rs.getDouble(3)));
                        detalhesNsu.setNroNsu(rs.getString(4));
                        detalhesNsu.setDocumento(rs.getString(4));
                        detalhesNsu.setAutorizacao(rs.getString(5));
                        detalhesNsu.setParcela(rs.getString(6));
                        detalhesNsu.setNroPlano(rs.getString(7));
                        detalhesNsu.setAntecipacao(rs.getBoolean(8));
                        detalhesNsu.setOperadora(rs.getString(9));
                        detalhesNsu.setDataVenda(Funcoes.formataDataRelatorio(rs.getLong(10)));
                        detalhesNsu.setCartao(Funcoes.formataNroCartao(rs.getString(11), rs.getString(21)));
                        detalhesNsu.setDataCredito(Funcoes.formataDataRelatorio(rs.getLong(12)));
                        detalhesNsu.setAgencia(rs.getString(13));
                        detalhesNsu.setBanco(rs.getString(14));
                        detalhesNsu.setConta(rs.getString(15));
                        detalhesNsu.setLote(rs.getString(16));
                        detalhesNsu.setProduto(rs.getString(17));
                        detalhesNsu.setLoja(rs.getString(18) == null ? "" : rs.getString(18));
                        detalhesNsu.setPv(rs.getString(19));
                        detalhesNsu.setCodStatus(rs.getString(20));
                        detalhesNsu.setSglOperadora(rs.getString(21));
                        detalhesNsu.setTid(rs.getString(22) == null ? "" : rs.getString(22));
                        detalhesNsu.setConciliada(rs.getString(23) == null ? "" : rs.getString(23));
                        detalhesNsu.setNumLogico(rs.getString(24) == null ? "" : rs.getString(24));
                        detalhesNsu.setIdConciliacao(rs.getString(25) == null ? "" : rs.getString(25));
                        detalhesNsu.setValorTxAdmin(Funcoes.dividePorCem(rs.getDouble(26)));
                        detalhesNsu.setDtaReagendamento(Funcoes.formataDataRelatorio(rs.getLong(27)));
                        detalhesNsu.setDscStatus(rs.getString(28) == null ? "" : rs.getString(28));

                        if (detalheBilhete != null) {
                            detalhesNsu.setDetalheBilheteDTO(detalheBilhete);
                        }
                        result.add(detalhesNsu);
                    }

                }
            }
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao buscar os detalhes da transacao", e);
        }

        return result;

    }
    
    public DetalheBilheteDTO pesquisarDetalhesDoBilhete(String idDscAreaCliente, String codArquivo) {

        DetalheBilheteDTO detalheBilhete = null;
        String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_NSU, "DetalheBilhete.sql");
        query = query.replace(":codArquivo", codArquivo);
        query = query.replace(":cccfNumber", idDscAreaCliente);
        LOGGER.info("QUERY: (DetalheBilhete.sql) -" + query);

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    detalheBilhete = new DetalheBilheteDTO();
                    detalheBilhete.setBilhete(rs.getString("BILHETE"));
                    detalheBilhete.setBilheteGrupo(rs.getString("BILHETE"));
                    detalheBilhete.setEntrada(Funcoes.dividePorCem(rs.getBigDecimal("ENTRADA")));
                    detalheBilhete.setTxEmbarque(Funcoes.dividePorCem(rs.getBigDecimal("TAXA_EMBARQUE")));
                    detalheBilhete.setAgenteCode(rs.getString("AGENTE_CODE"));
                    detalheBilhete.setNomePassageiro(rs.getString("NOME_PASSAGEIRO"));
                }
            }
        } catch (Exception e) {
            String msg = "Ocorreu um erro ao buscar os detalhes do hash_value - ";
            LOGGER.error(msg, e);
        }

        return detalheBilhete;

    }

}
