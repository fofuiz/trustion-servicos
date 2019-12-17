package br.com.accesstage.trustion.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.accesstage.trustion.dto.LinkVideoDTO.LinkVideoDTOBuilder;
import br.com.accesstage.trustion.model.LinkVideo;
import br.com.accesstage.trustion.model.TipoVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LinkVideoCreateDTO {

	private Long id;
	private Long idTipoVideo;

	public LinkVideoCreateDTO(LinkVideo linkVideo) {
		this.id = linkVideo.getIdLinkVideo();
		this.idTipoVideo = linkVideo.getVideos().get(0).getIdTipoVideo();
	}

	public LinkVideoCreateDTO(LinkVideo linkVideo, List<TipoVideo> saveAll) {
		this.id = linkVideo.getIdLinkVideo();
		this.idTipoVideo = saveAll.get(0).getIdTipoVideo();
	}

}
