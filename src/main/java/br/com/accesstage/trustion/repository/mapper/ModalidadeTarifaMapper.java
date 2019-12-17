package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.ModalidadeTarifa;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModalidadeTarifaMapper implements RowMapper<ModalidadeTarifa> {

    @Override
    public ModalidadeTarifa mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        ModalidadeTarifa modalidadeEntity = new ModalidadeTarifa();

        modalidadeEntity.setCodigoModalidadeStr(rs.getString("COD_MODALIDADE"));
        modalidadeEntity.setDescricaoModalidade(rs.getString("DSC_MODALIDADE"));
        modalidadeEntity.setTipoArrecadacao(rs.getString("TPO_ARREC"));

        return modalidadeEntity;

    }

}
