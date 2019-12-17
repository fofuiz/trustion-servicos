package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.ResumoConciliacaoCartaoEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConciliacaoCartaoResumoMapper2 implements RowMapper<ResumoConciliacaoCartaoEntity> {

    public ResumoConciliacaoCartaoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        ResumoConciliacaoCartaoEntity entity = new ResumoConciliacaoCartaoEntity();

        entity.setCodOperadora(rs.getLong("COD_OPERADORA"));
        entity.setNomeOperadora(rs.getString("NME_EXIBICAO_PORTAL"));

        String dataExtrPagamento = rs.getString("DTA_EXTR_PAGAMENTO");

        entity.setDataExtrPagamento(dataExtrPagamento.substring(4) + "/" + dataExtrPagamento.substring(0, 4));

        entity.setNumeroBanco(rs.getInt("COD_BANCO"));
        entity.setNumeroAgencia(rs.getInt("COD_AGENCIA"));
        entity.setNumeroConta(rs.getString("COD_CONTA"));

        try {
            entity.setValorPagamento(rs.getDouble("VLR_PGTO_TOTAL"));
        } catch (SQLException e) { }
        
        try {
            entity.setValorExtrato(rs.getDouble("VLR_EXTR_TOTAL"));
        } catch (SQLException e) { }

        entity.setStatusConciliado(rs.getString("STA_CONCILIADO"));

        return entity;
    }

}
