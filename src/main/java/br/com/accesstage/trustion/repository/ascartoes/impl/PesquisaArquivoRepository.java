package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.PesquisaArquivoDTO;
import br.com.accesstage.trustion.util.Funcoes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PesquisaArquivoRepository {

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    /**
     * Pesquisa arquivo por nome ou sequencial
     *
     * @param nomeArquivo
     * @param sequencial
     * @param empId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PesquisaArquivoDTO> pesquisarArquivo(String nomeArquivo, String sequencial, String empId) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT x.nme_arquivo_origem AS 	NOME_ARQUIVO ");
        sb.append(", x.nro_seq_arquivo as SEQUENCIAL");
        sb.append(", x.cod_arquivo as COD_ARQUIVO");
        sb.append(", x.dta_criacao as DATA_CRIACAO");
        sb.append(", x.dta_criacao as DATA_CRIACAO");
        sb.append(", c.qtd_registros_processados as QTD_REGISTROS_PROCESSADOS");
        sb.append(" FROM ARQUIVO x");
        sb.append(" , CONTROLE_CARGA c");
        sb.append(" WHERE x.EMPID = ");
        sb.append(empId);
        sb.append(" AND c.cod_arquivo = x.cod_arquivo ");
        sb.append(" AND c.sta_processamento = 'F' ");
        if (sequencial != null && !sequencial.isEmpty()) {
            sb.append("   and x.nro_seq_arquivo = ");
            sb.append(sequencial);
        }
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            sb.append("   and x.nme_arquivo_origem LIKE '%");
            sb.append(nomeArquivo);
            sb.append("%'");
        }

        sb.append(" AND x.tpo_arquivo LIKE '%REMC%' ");

        LOGGER.info("Consulta pesquisarArquivo: " + sb.toString());

        List<PesquisaArquivoDTO> listaArquivo = new ArrayList<>();
        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {

                    PesquisaArquivoDTO vo = new PesquisaArquivoDTO();

                    vo.setSequencial(rs.getString("SEQUENCIAL"));
                    vo.setNomeArquivo(rs.getString("NOME_ARQUIVO"));
                    vo.setId(rs.getLong("COD_ARQUIVO"));
                    vo.setDataEnvio(Funcoes.formataDataPesquisaDataProcessamento(rs.getDate("DATA_CRIACAO")));
                    vo.setQtdRegistro(rs.getLong("QTD_REGISTROS_PROCESSADOS"));

                    listaArquivo.add(vo);

                }
            } catch (SQLException ex) {
                LOGGER.error("Ocorreu um erro no metodo PesquisaArquivoRepository.pesquisarArquivo " + ex.getMessage());
            }
        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo PesquisaArquivoRepository.pesquisarArquivo " + ex.getMessage());
        }

        return listaArquivo;
    }

    /**
     * *
     * Pesquisa arquivo por data
     *
     * @param dataInicial
     * @param dataFinal
     * @param empId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PesquisaArquivoDTO> pesquisarArquivoData(Date dataInicial, Date dataFinal, String empId) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT x.nme_arquivo_origem AS 	NOME_ARQUIVO ");
        sb.append(", x.nro_seq_arquivo as SEQUENCIAL");
        sb.append(", x.cod_arquivo as COD_ARQUIVO");
        sb.append(", x.dta_criacao as DATA_CRIACAO");
        sb.append(", c.qtd_registros_processados as QTD_REGISTROS_PROCESSADOS");
        sb.append(" FROM ARQUIVO x");
        sb.append(" , CONTROLE_CARGA c");
        sb.append(" WHERE x.EMPID = ");
        sb.append(empId);
        sb.append(" AND c.cod_arquivo = x.cod_arquivo ");
        sb.append(" AND c.sta_processamento = 'F' ");
        sb.append("   and trunc(x.dta_criacao) between ? and ?   ");
        sb.append("   and x.TPO_ARQUIVO LIKE '%REMC%'   ");

        LOGGER.info("Consulta pesquisarArquivoData: " + sb.toString());

        List<PesquisaArquivoDTO> listaArquivo = new ArrayList<>();

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

            pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
            pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {

                    PesquisaArquivoDTO dto = new PesquisaArquivoDTO();

                    dto.setSequencial(rs.getString("SEQUENCIAL"));
                    dto.setNomeArquivo(rs.getString("NOME_ARQUIVO"));
                    dto.setId(rs.getLong("COD_ARQUIVO"));
                    dto.setDataEnvio(Funcoes.formataDataPesquisaDataProcessamento(rs.getDate("DATA_CRIACAO")));
                    dto.setQtdRegistro(rs.getLong("QTD_REGISTROS_PROCESSADOS"));

                    listaArquivo.add(dto);

                }
            } catch (SQLException ex) {
                LOGGER.error("Ocorreu um erro no metodo PesquisaArquivoRepository.pesquisarArquivoData " + ex.getMessage());
            }
        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo PesquisaArquivoRepository.pesquisarArquivoData " + ex.getMessage());
        }

        return listaArquivo;

    }

}
