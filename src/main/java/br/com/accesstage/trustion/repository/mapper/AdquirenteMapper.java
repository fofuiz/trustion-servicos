package br.com.accesstage.trustion.repository.mapper;


import br.com.accesstage.trustion.dto.conciliacao.AdquirenteDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdquirenteMapper implements RowMapper<AdquirenteDTO> {

    @Override
    public AdquirenteDTO mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        AdquirenteDTO adquirenteEntity = new AdquirenteDTO();

        adquirenteEntity.setCodOperadora(rs.getLong("COD_OPERADORA"));
        adquirenteEntity.setNomeOperadora(rs.getString("NME_EXIBICAO_PORTAL"));

        return adquirenteEntity;
    }

}