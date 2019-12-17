package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.model.Atividade;

public class AtividadeConverter {

    public static AtividadeDTO paraDTO(Atividade entidade) {

        AtividadeDTO dto = new AtividadeDTO();
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
        dto.setResponsavel(entidade.getResponsavel());
        dto.setIdTipoOcorrencia(entidade.getIdtTipoOcorrencia());
        dto.setAtividadeMescla(entidade.isMescla());

        return dto;
    }

    public static Atividade paraEntidade(AtividadeDTO dto) {
        Atividade entidade = new Atividade();
        entidade.setIdAtividade(dto.getIdAtividade());
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setIdUsuario(dto.getIdUsuario());
        entidade.setDataHorario(dto.getDataHorario());
        entidade.setAtividade(dto.getAtividade());
        entidade.setTipoAtividade(dto.getTipoAtividade());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setResponsavel(dto.getResponsavel());
        entidade.setIdtTipoOcorrencia(dto.getIdTipoOcorrencia());
        //entidade.setMescla(dto.);
        return entidade;
    }

}
