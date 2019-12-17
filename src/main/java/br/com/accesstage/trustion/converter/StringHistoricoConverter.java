package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.StringHistoricoDTO;
import br.com.accesstage.trustion.model.StringHistorico;

public class StringHistoricoConverter {

    public static StringHistorico paraEntidade(StringHistoricoDTO dto) {

        if (dto == null) {
            return null;
        }

        StringHistorico entidade = new StringHistorico();
        entidade.setIdStringHistorico(dto.getIdStringHistorico());
        entidade.setIdListaBanco(dto.getIdListaBanco());
        entidade.setTexto(dto.getTexto());
        entidade.setStatus(dto.getStatus());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());

        return entidade;
    }

    public static StringHistoricoDTO paraDTO(StringHistorico entidade) {

        if (entidade == null) {
            return null;
        }

        StringHistoricoDTO dto = new StringHistoricoDTO();
        dto.setIdStringHistorico(entidade.getIdStringHistorico());
        dto.setIdListaBanco(entidade.getIdListaBanco());
        dto.setTexto(entidade.getTexto());
        dto.setStatus(entidade.getStatus());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;
    }
}
