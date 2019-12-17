package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.TipoStatusOcorrenciaDTO;

public interface ITipoStatusOcorrenciaService {

	List<TipoStatusOcorrenciaDTO> listarTodos() throws Exception;
	List<TipoStatusOcorrenciaDTO> listarReabertura() throws Exception;

}
