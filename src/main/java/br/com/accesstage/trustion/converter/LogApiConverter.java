package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.LogApiDTO;
import br.com.accesstage.trustion.model.LogApi;

public class LogApiConverter {
	
	public static LogApiDTO paraDTO(LogApi entidade) {
		LogApiDTO dto = new LogApiDTO();
		dto.setIdLogAPI(entidade.getIdLogAPI());
		dto.setIdGrupoEconomico(entidade.getIdGrupoEconomico());
		dto.setIdEmpresa(entidade.getIdEmpresa());
		dto.setIdCofre(entidade.getIdCofre());
		dto.setApi(entidade.getApi());
		dto.setStatusConsulta(entidade.getStatusConsulta());
		dto.setDataLog(entidade.getDataLog());
		dto.setMensagem(entidade.getMensagem());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setDataAlteracao(entidade.getDataAlteracao());
		return dto;
	}
	
	public static LogApi paraEntidade(LogApiDTO dto) {
		LogApi entidade = new LogApi();
		entidade.setIdLogAPI(dto.getIdLogAPI());
		entidade.setIdGrupoEconomico(dto.getIdGrupoEconomico());
		entidade.setIdEmpresa(dto.getIdEmpresa());
		entidade.setIdCofre(dto.getIdCofre());
		entidade.setApi(dto.getApi());
		entidade.setStatusConsulta(dto.getStatusConsulta());
		entidade.setDataLog(dto.getDataLog());
		entidade.setMensagem(dto.getMensagem());
		entidade.setDataCriacao(dto.getDataCriacao());
		entidade.setDataAlteracao(dto.getDataAlteracao());
		return entidade;
	}
}
