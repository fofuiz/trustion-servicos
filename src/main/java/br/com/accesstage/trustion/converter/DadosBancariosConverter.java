package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.DadosBancariosDTO;
import br.com.accesstage.trustion.model.DadosBancarios;

public class DadosBancariosConverter {

    public static DadosBancarios paraEntidade(DadosBancariosDTO dto){

        if(dto == null)
            return null;

        DadosBancarios entidade = new DadosBancarios();
        entidade.setAgencia(dto.getAgencia());
        entidade.setConta(dto.getConta());
        entidade.setDv(dto.getDv());
        entidade.setIdBanco(Long.valueOf(dto.getIdBanco()));
        entidade.setIdEmpresa(dto.getIdEmpresa());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdDadosBancarios(dto.getIdDadosBancarios());
        entidade.setIdUsuario(dto.getIdUsuario());

        return entidade;
    }


    public static DadosBancariosDTO paraDTO(DadosBancarios entidade){

        if(entidade == null)
            return null;

        DadosBancariosDTO dto = new DadosBancariosDTO();
        dto.setIdBanco(entidade.getIdBanco());
        dto.setAgencia(entidade.getAgencia());
        dto.setConta(entidade.getConta());
        dto.setDv(entidade.getDv());
        dto.setIdDadosBancarios(entidade.getIdDadosBancarios());
        dto.setIdEmpresa(entidade.getIdEmpresa());
        dto.setIdUsuario(entidade.getIdUsuario());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;
    }
}
