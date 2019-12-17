package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.DetalheConciliacaoCartaoEntity;
import br.com.accesstage.trustion.configs.log.Log;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;

public class ConciliacaoCartaoDetalheExtratoMapper implements RowMapper<DetalheConciliacaoCartaoEntity> {

    @Log
    private static Logger LOGGER;
    
    public DetalheConciliacaoCartaoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        DetalheConciliacaoCartaoEntity entity = new DetalheConciliacaoCartaoEntity();

        try {
            entity.setId(rs.getLong("ID_FIN_CONTABIL"));
        } catch (SQLException ex) {
            LOGGER.error("Erro ao recuperar ID_FIN_CONTABIL", ex);
        }

        entity.setNroGrupoConciliado(rs.getLong("NRO_GRUPO_CONCILIADO"));
        entity.setCodEmpresa(rs.getLong("COD_EMPRESA"));
        entity.setCodOperadora(rs.getLong("COD_OPERADORA"));
        entity.setCodMovimento(rs.getInt("COD_MOVIMENTO"));
        entity.setNomeOperadora(rs.getString("NME_EXIBICAO_PORTAL"));

        try {
            entity.setNomeProduto(rs.getString("NME_PRODUTO"));
        } catch (SQLException ex) {
            LOGGER.error("Erro ao recuperar NME_PRODUTO", ex);
        }

        entity.setDataExtrPagamento(rs.getDate("DTA_EXTR_PAGAMENTO"));
        entity.setNumeroBanco(rs.getInt("COD_BANCO"));
        entity.setNumeroAgencia(rs.getInt("COD_AGENCIA"));
        entity.setNumeroConta(rs.getString("COD_CONTA"));
        entity.setValorPagamento(rs.getBigDecimal("VLR_EXTR_TOTAL"));
        entity.setTipoConciliacao(rs.getString("TPO_CONCILIACAO"));
        entity.setStatus(rs.getString("STA_CONCILIACAO"));
        entity.setDscExtrHistorico(rs.getString("DSC_EXTR_HISTORICO"));
        entity.setDscExtrDocumento(rs.getString("DSC_EXTR_DOCUMENTO"));
        entity.setNomeArquivoOrigem(rs.getString("NME_ARQUIVO_ORIGEM"));

        return entity;
    }
}

