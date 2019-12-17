package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import br.com.accesstage.trustion.model.GrupoEconomico;

public class GrupoEconomicoConverter {

    public static GrupoEconomico paraEntidade(GrupoEconomicoDTO dto) {

        GrupoEconomico grupo = new GrupoEconomico();

        grupo.setIdGrupoEconomico(dto.getIdGrupoEconomico());
        grupo.setCnpj(dto.getCnpj());
        grupo.setNome(dto.getNome());
        grupo.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        grupo.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        grupo.setDataCriacao(dto.getDataCriacao());
        grupo.setDataAlteracao(dto.getDataAlteracao());

        return grupo;
    }

    public static GrupoEconomicoDTO paraDTO(GrupoEconomico grupo) {

        GrupoEconomicoDTO dto = new GrupoEconomicoDTO();

        dto.setIdGrupoEconomico(grupo.getIdGrupoEconomico());
        dto.setCnpj(grupo.getCnpj());
        dto.setNome(grupo.getNome());
        dto.setIdUsuarioCriacao(grupo.getIdUsuarioCriacao());
        dto.setIdUsuarioAlteracao(grupo.getIdUsuarioAlteracao());
        dto.setDataCriacao(grupo.getDataCriacao());
        dto.setDataAlteracao(grupo.getDataAlteracao());

        return dto;
    }

}
