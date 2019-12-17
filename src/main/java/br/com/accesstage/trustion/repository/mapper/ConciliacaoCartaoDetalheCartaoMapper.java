package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.DetalheConciliacaoCartaoEntity;
import br.com.accesstage.trustion.configs.log.Log;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;

public class ConciliacaoCartaoDetalheCartaoMapper implements RowMapper<DetalheConciliacaoCartaoEntity> {

    @Log
    private static Logger LOGGER;

    public DetalheConciliacaoCartaoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        DetalheConciliacaoCartaoEntity entity = new DetalheConciliacaoCartaoEntity();

        try {
            entity.setId(rs.getLong("ID_FATO_TRANSACAO_SUM"));
        } catch (SQLException ex) {
            LOGGER.error("Erro ao recuperar ID_FATO_TRANSACAO_SUM", ex);
        }

        entity.setCodEmpresa(rs.getLong("EMPID"));
        entity.setNroGrupoConciliado(rs.getLong("NRO_GRUPO_CONCILIADO"));
        entity.setCodOperadora(rs.getLong("COD_OPERADORA"));
        entity.setCodMovimento(rs.getInt("COD_MOVIMENTO"));
        entity.setNomeOperadora(rs.getString("NME_EXIBICAO_PORTAL"));

        try {
            entity.setNomeProduto(rs.getNString("NME_PRODUTO"));
        } catch (SQLException ex) {
            LOGGER.error("Erro ao recuperar NME_PRODUTO", ex);
        }

        try {
            entity.setDataExtrPagamento(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(rs.getInt("COD_DATA_CREDITO"))));
        } catch (SQLException | ParseException ex) {
            LOGGER.error("Erro ao recuperar COD_DATA_CREDITO", ex);
        }

        entity.setNumeroBanco(rs.getInt("COD_BANCO"));
        entity.setNumeroAgencia(rs.getInt("COD_AGENCIA"));
        entity.setNumeroConta(rs.getString("COD_CONTA"));
        entity.setValorPagamento(rs.getBigDecimal("VLR_LIQUIDO"));
        entity.setTipoConciliacao(rs.getString("TPO_CONCILIADO"));
        entity.setHashValue(rs.getString("HASH_VALUE"));
        entity.setStatus(rs.getString("STA_CONCILIACAO"));
        entity.setNomeArquivoOrigem(rs.getString("NME_ARQUIVO_ORIGEM"));

        return entity;
    }
}
