package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.PerfilDTO;
import br.com.accesstage.trustion.model.Perfil;

public class PerfilConverter {

	public static Perfil paraEntidade(PerfilDTO perfilDTO) {
		
		Perfil perfil = new Perfil();
		
		perfil.setIdPerfil(perfilDTO.getIdPerfil());
		perfil.setDescricao(perfilDTO.getDescricao());

		return perfil;
	}

	public static PerfilDTO paraDTO(Perfil perfil) {
		
		PerfilDTO perfilDTO = new PerfilDTO();
		
		perfilDTO.setIdPerfil(perfil.getIdPerfil());
		perfilDTO.setDescricao(perfil.getDescricao());

		return perfilDTO;
	}
}
