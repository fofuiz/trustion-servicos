package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.model.Ocorrencia;

public class OcorrenciaConverter {

    public static OcorrenciaDTO paraDTO(Ocorrencia entidade) {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setIdOcorrencia(entidade.getIdOcorrencia());
        dto.setIdTipoQuestionamento(entidade.getIdTipoQuestionamento());
        dto.setValorQuestionado(entidade.getValorQuestionado());
        dto.setValorAjuste(entidade.getValorAjuste());
        dto.setIdTipoStatusOcorrencia(entidade.getIdTipoStatusOcorrencia());
        dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
        dto.setIdOcorrenciaMescla(entidade.getIdOcorrenciaMescla());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setValorAjuste(entidade.getValorAjuste());
        dto.setIsOcorrenciaD1(entidade.isIsOcorrenciaD1());
        dto.setIdTransportadora(entidade.getIdTransportadora());
        // Somente no dto por ser um atributo readonly
        dto.setMesclas(entidade.getMesclas());
        
        return dto;
    }

    public static Ocorrencia paraEntidade(OcorrenciaDTO dto) {
        Ocorrencia entidade = new Ocorrencia();
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setIdTipoQuestionamento(dto.getIdTipoQuestionamento());
        entidade.setValorQuestionado(dto.getValorQuestionado());
        entidade.setIdTipoStatusOcorrencia(dto.getIdTipoStatusOcorrencia());
        entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
        entidade.setIdOcorrenciaMescla(dto.getIdOcorrenciaMescla());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setValorAjuste(dto.getValorAjuste());
        if(dto.getIsOcorrenciaD1() != null) {
            entidade.setIsOcorrenciaD1(dto.getIsOcorrenciaD1());
        }
        
        entidade.setIdTransportadora(dto.getIdTransportadora());
        return entidade;
    }

}
