package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TipoCreditoDTO;
import br.com.accesstage.trustion.model.TipoCredito;

public class TipoCreditoConverter {

	public static TipoCredito paraEntidade(TipoCreditoDTO dto){
		TipoCredito entidade = new TipoCredito();
		entidade.setIdTipoCredito(dto.getIdTipoCredito());
		entidade.setDescricao(dto.getDescricao());
		entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
		entidade.setDataCriacao(dto.getDataCriacao());
		entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
		entidade.setDataAlteracao(dto.getDataAlteracao());
		return entidade;
	}
	
	public static TipoCreditoDTO paraDTO(TipoCredito entidade){
		TipoCreditoDTO dto = new TipoCreditoDTO();
		dto.setIdTipoCredito(entidade.getIdTipoCredito());
		dto.setDescricao(entidade.getDescricao());
		dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
		dto.setDataAlteracao(entidade.getDataAlteracao());
		return dto;
	}
}
