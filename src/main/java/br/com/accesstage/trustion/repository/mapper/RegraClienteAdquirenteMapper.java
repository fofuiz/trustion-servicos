package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.RegraClienteAdquirenteEntity;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RegraClienteAdquirenteMapper implements RowMapper<RegraClienteAdquirenteEntity> {

    @Override
    public RegraClienteAdquirenteEntity mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        RegraClienteAdquirenteEntity regraClienteAdquirenteEntity = new RegraClienteAdquirenteEntity();

        regraClienteAdquirenteEntity.setCodBanco(rs.getString("COD_BANCO"));
        regraClienteAdquirenteEntity.setCodOperadora(rs.getInt("COD_OPERADORA"));
        regraClienteAdquirenteEntity.setNmeExibicaoPortal(rs.getString("NME_EXIBICAO_PORTAL"));
        regraClienteAdquirenteEntity.setEmpId(rs.getInt("EMPID"));
        regraClienteAdquirenteEntity.setDscChavePrincipal(rs.getString("DSC_CHAVE_PRINCIPAL"));
        regraClienteAdquirenteEntity.setDscChaveSecundaria(rs.getString("DSC_CHAVE_SECUNDARIA"));
        regraClienteAdquirenteEntity.setStaAtivo(rs.getString("STA_ATIVO"));

        return regraClienteAdquirenteEntity;
    }

}
