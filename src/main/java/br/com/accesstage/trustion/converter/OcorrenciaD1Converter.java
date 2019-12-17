package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.OcorrenciaD1DTO;
import br.com.accesstage.trustion.model.Ocorrencia;

public class OcorrenciaD1Converter {
	
	
	public static OcorrenciaD1DTO paraDTO(Ocorrencia entidade) {
		OcorrenciaD1DTO dto = new OcorrenciaD1DTO();
		dto.setIdOcorrencia(entidade.getIdOcorrencia());
		dto.setIdTipoQuestionamento(entidade.getIdTipoQuestionamento());
		dto.setValorQuestionado(entidade.getValorQuestionado());
		dto.setIdTipoStatusOcorrencia(entidade.getIdTipoStatusOcorrencia());
		dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
		dto.setDataAlteracao(entidade.getDataAlteracao());
		dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
//		dto.setValorDeclarado(entidade.getValorDeclarado());
		dto.setIdOcorrenciaMescla(entidade.getIdOcorrenciaMescla() == null ? null : entidade.getIdOcorrenciaMescla());
		dto.setMesclas(entidade.getMesclas());
		dto.setValorAjuste(entidade.getValorAjuste());
		
		return dto;
	}
	
	public static Ocorrencia paraEntidade(OcorrenciaD1DTO dto) {
		Ocorrencia entidade = new Ocorrencia();
		entidade.setIdOcorrencia(dto.getIdOcorrencia());
		entidade.setIdTipoQuestionamento(dto.getIdTipoQuestionamento());
		entidade.setValorQuestionado(dto.getValorQuestionado());
		entidade.setIdTipoStatusOcorrencia(dto.getIdTipoStatusOcorrencia());
		entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
		entidade.setDataCriacao(dto.getDataCriacao());
		entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
		entidade.setDataAlteracao(dto.getDataAlteracao());
		entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
		entidade.setValorAjuste(dto.getValorAjuste());
                entidade.setIsOcorrenciaD1(true);
		return entidade;
	}
}
