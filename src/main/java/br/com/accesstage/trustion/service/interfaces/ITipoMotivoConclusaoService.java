package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.TipoMotivoConclusaoDTO;
import br.com.accesstage.trustion.model.TipoMotivoConclusao;


public interface ITipoMotivoConclusaoService {
	List<TipoMotivoConclusao> listarMotivos() throws Exception;

	List<TipoMotivoConclusao> listarMotivosPorDescricao(String descricao) throws Exception;

	TipoMotivoConclusao listaPorId(Long idMotivo) throws Exception;

	TipoMotivoConclusaoDTO criar(TipoMotivoConclusaoDTO tipoMotivoConclusaoTO) throws Exception;

	TipoMotivoConclusaoDTO alterar(TipoMotivoConclusaoDTO tipoMotivoConclusaoDTO);
}