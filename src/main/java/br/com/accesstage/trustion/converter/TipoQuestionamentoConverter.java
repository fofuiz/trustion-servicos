package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TipoQuestionamentoDTO;
import br.com.accesstage.trustion.model.TipoQuestionamento;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TipoQuestionamentoConverter {

    public static TipoQuestionamentoDTO paraDTO(TipoQuestionamento entidade) {
        TipoQuestionamentoDTO dto = new TipoQuestionamentoDTO();
        dto.setIdTipoQuestionamento(entidade.getIdTipoQuestionamento());
        dto.setDescricao(entidade.getDescricao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataCriacao(converterLocalDateTimeParaDate(entidade.getDataCriacao()));
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
        dto.setDataAlteracao(converterLocalDateTimeParaDate(entidade.getDataAlteracao()));
        dto.setNomeUsuarioCriacao(null == entidade.getUsuarioCriacao() ? null : entidade.getUsuarioCriacao().getNome());
        dto.setNomeUsuarioAlteracao(null == entidade.getUsuarioAlteracao() ? null : entidade.getUsuarioAlteracao().getNome());
        return dto;
    }

    public static TipoQuestionamento paraEntidade(TipoQuestionamentoDTO dto) {
        TipoQuestionamento entidade = new TipoQuestionamento();
        entidade.setIdTipoQuestionamento(dto.getIdTipoQuestionamento());
        entidade.setDescricao(dto.getDescricao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        return entidade;
    }
    
    private static Date converterLocalDateTimeParaDate(LocalDateTime dataParaConverter) {
        Date dataConvertida = null;
        
        if(null != dataParaConverter) {
            dataConvertida = Date.from(dataParaConverter.atZone(ZoneId.systemDefault()).toInstant());
        }
        
        return dataConvertida;
    }
    
    private static LocalDateTime converterDateParaLocalDateTime(Date dataParaConverter) {
        LocalDateTime dataConvertida = null;
        
        if(null != dataParaConverter) {
            dataConvertida = LocalDateTime.ofInstant(Instant.ofEpochMilli(dataParaConverter.getTime()), ZoneId.systemDefault());
        }
        
        return dataConvertida;
    }
}
