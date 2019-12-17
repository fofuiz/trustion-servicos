package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.PerfilDTO;

public interface IPerfilService {

	List<PerfilDTO> listarTodosCadastro() throws Exception;
	List<PerfilDTO> listarTodosPesquisa() throws Exception;
}
