package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DomicilioBancarioMapper implements RowMapper<DomicilioBancario> {

    @Override
    public DomicilioBancario mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        DomicilioBancario domicilioBancarioEntity = new DomicilioBancario();

        domicilioBancarioEntity.setDescContaBanco(rs.getString("DSC_CONTA_BANCO"));
        domicilioBancarioEntity.setNroBanco(rs.getInt("NRO_BANCO"));
        domicilioBancarioEntity.setNroContaCorrente(rs.getString("NRO_CONTA_CORRENTE"));
        domicilioBancarioEntity.setNroAgencia(rs.getInt("NRO_AGENCIA"));
        domicilioBancarioEntity.setStaAtivo(rs.getString("STA_ATIVO"));
        domicilioBancarioEntity.setEmpID(rs.getInt("EMPID"));

        return domicilioBancarioEntity;
    }

}
