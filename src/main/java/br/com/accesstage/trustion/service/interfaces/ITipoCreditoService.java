package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.TipoCreditoDTO;

public interface ITipoCreditoService {
	List<TipoCreditoDTO> listarTodos() throws Exception;
}
