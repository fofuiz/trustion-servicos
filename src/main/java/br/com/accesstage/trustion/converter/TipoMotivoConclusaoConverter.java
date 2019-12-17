package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TipoMotivoConclusaoDTO;
import br.com.accesstage.trustion.model.TipoMotivoConclusao;

public class TipoMotivoConclusaoConverter {

	public static TipoMotivoConclusaoDTO paraDTO(TipoMotivoConclusao entidade) {
		TipoMotivoConclusaoDTO dto = new TipoMotivoConclusaoDTO();
		dto.setIdTipoMotivoConclusao(entidade.getIdMotivo());
		dto.setDescricao(entidade.getDescricao());

		return dto;
	}
	
	public static TipoMotivoConclusao paraEntidade(TipoMotivoConclusaoDTO dto) {
		TipoMotivoConclusao entidade = new TipoMotivoConclusao();
		entidade.setIdMotivo(dto.getIdTipoMotivoConclusao());
		entidade.setDescricao(dto.getDescricao());
 
		return entidade;
	}
}
