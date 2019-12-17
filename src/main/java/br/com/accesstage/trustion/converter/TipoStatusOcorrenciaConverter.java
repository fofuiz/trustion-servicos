package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TipoStatusOcorrenciaDTO;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;

public class TipoStatusOcorrenciaConverter {

	public static TipoStatusOcorrenciaDTO paraDTO(TipoStatusOcorrencia entidade) {
		TipoStatusOcorrenciaDTO dto = new TipoStatusOcorrenciaDTO();
		dto.setIdTipoStatusOcorrencia(entidade.getIdTipoStatusOcorrencia());
		dto.setDescricao(entidade.getDescricao());
		dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
		dto.setDataAlteracao(entidade.getDataAlteracao());
		return dto;
	}
	
	public static TipoStatusOcorrencia paraEntidade(TipoStatusOcorrenciaDTO dto) {
		TipoStatusOcorrencia entidade = new TipoStatusOcorrencia();
		entidade.setIdTipoStatusOcorrencia(dto.getIdTipoStatusOcorrencia());
		entidade.setDescricao(dto.getDescricao());
		entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
		entidade.setDataCriacao(dto.getDataCriacao());
		entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
		entidade.setDataAlteracao(dto.getDataAlteracao());
		return entidade;
	}
}
