package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.MotivoConciliacao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MotivoConciliacaoMapper implements RowMapper<MotivoConciliacao> {


    public MotivoConciliacao mapRow(ResultSet rs, int rowNum) throws SQLException {

        MotivoConciliacao entity = new MotivoConciliacao();

        entity.setCodigoMotivoConciliacao(rs.getLong("COD_MOTIVO_CONC_MANUAL"));
        entity.setDescConciliacao(rs.getString("DSC_MOTIVO_CONC_MANUAL"));

        return entity;
    }
}
