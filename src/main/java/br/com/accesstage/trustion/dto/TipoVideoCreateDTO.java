package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.TipoVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TipoVideoCreateDTO {

	private Long id; 

	public TipoVideoCreateDTO(TipoVideo tipoVideo) {
		this.id = tipoVideo.getIdTipoVideo();
	}

}
