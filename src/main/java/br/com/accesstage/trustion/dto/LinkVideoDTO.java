package br.com.accesstage.trustion.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.accesstage.trustion.model.LinkVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LinkVideoDTO {

	private Long id;
	private Long gtv;
	private List<TipoVideoDTO> tipos;

	public LinkVideoDTO(LinkVideo linkVideo) {
		this.id = linkVideo.getIdLinkVideo();
		this.gtv = linkVideo.getGtv();
		this.tipos = new ArrayList<>();
		this.tipos.addAll(linkVideo.getVideos().stream()
				.map(TipoVideoDTO::new)
				.collect(Collectors.toList()));
	}

}
