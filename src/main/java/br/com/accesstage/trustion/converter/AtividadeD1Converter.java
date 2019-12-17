package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.AtividadeD1DTO;
import br.com.accesstage.trustion.model.Atividade;

public class AtividadeD1Converter {

    public static AtividadeD1DTO paraDTO(Atividade entidade) {

        if (entidade == null) {
            return null;
        }

        AtividadeD1DTO dto = new AtividadeD1DTO();
        dto.setIdAtividade(entidade.getIdAtividade());
        dto.setIdOcorrencia(entidade.getIdOcorrencia());
        dto.setIdUsuario(entidade.getIdUsuario());
        dto.setDataHorario(entidade.getDataHorario());
        dto.setAtividade(entidade.getAtividade());
        dto.setTipoAtividade(entidade.getTipoAtividade());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdTipoOcorrencia(entidade.getIdtTipoOcorrencia());
        dto.setResponsavel(entidade.getResponsavel());
        dto.setAtividadeMescla(entidade.isMescla());
        return dto;
    }

    public static Atividade paraEntidade(AtividadeD1DTO dto) {

        if (dto == null) {
            return null;
        }

        Atividade entidade = new Atividade();
        entidade.setIdAtividade(dto.getIdAtividade());
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setIdUsuario(dto.getIdUsuario());
        entidade.setDataHorario(dto.getDataHorario());
        entidade.setAtividade(dto.getAtividade());
        entidade.setTipoAtividade(dto.getTipoAtividade());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setResponsavel(dto.getResponsavel());
        entidade.setIdtTipoOcorrencia(dto.getIdTipoOcorrencia());
        return entidade;
    }

}
