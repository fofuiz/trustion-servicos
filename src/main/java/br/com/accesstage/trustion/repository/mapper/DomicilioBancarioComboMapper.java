package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DomicilioBancarioComboMapper implements RowMapper<DomicilioBancario> {

    @Override
    public DomicilioBancario mapRow(ResultSet rs, int rowNum) throws SQLException {

        DomicilioBancario domicilioBancarioEntity = new DomicilioBancario();

        domicilioBancarioEntity.setNroBanco(rs.getInt("NRO_BANCO"));

        return domicilioBancarioEntity;
    }

}
