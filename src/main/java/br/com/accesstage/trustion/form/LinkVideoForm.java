package br.com.accesstage.trustion.form;

import java.util.List;

import br.com.accesstage.trustion.model.LinkVideo;
import br.com.accesstage.trustion.model.TipoVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVideoForm {
	
	private Long id;
	
	private Long gtv;

	private List<TipoVideo> tipos;

	public LinkVideo converter() {
		return new LinkVideo(gtv, tipos) ;
	}

}
