package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.TipoVideo;
import br.com.accesstage.trustion.model.enums.TipoVideoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class TipoVideoDTO {

	private Long id;
	private String link;
	private TipoVideoEnum tipoVideo;

	public TipoVideoDTO(TipoVideo tipoVideo) {
		this.id = tipoVideo.getIdTipoVideo();
		this.link = tipoVideo.getLink();
		this.tipoVideo = tipoVideo.getTipoVideo();
	}

}