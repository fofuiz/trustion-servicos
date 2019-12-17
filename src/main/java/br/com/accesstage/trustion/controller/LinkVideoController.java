package br.com.accesstage.trustion.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.accesstage.trustion.dto.LinkVideoCreateDTO;
import br.com.accesstage.trustion.dto.LinkVideoDTO;
import br.com.accesstage.trustion.form.LinkVideoForm;
import br.com.accesstage.trustion.model.LinkVideo;
import br.com.accesstage.trustion.model.TipoVideo;
import br.com.accesstage.trustion.repository.interfaces.ILinkVideoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoVideoRepository;

@RestController
@RequestMapping("/link")
public class LinkVideoController {

	@Autowired
	private ILinkVideoRepository linkVideoRepository;
	
	@Autowired
	private ITipoVideoRepository tipoVideoRepository;
	
	@PostMapping()
	public ResponseEntity<LinkVideoCreateDTO> cadastrar(@RequestBody  @Valid LinkVideoForm linkForm, UriComponentsBuilder uriBuilder) {
		Optional<LinkVideo> encontreiGtv = linkVideoRepository.findByGtv(linkForm.getGtv());
		
		if(encontreiGtv.isPresent()) {			
			for (TipoVideo tipoVideo : encontreiGtv.get().getVideos()) {
				if(tipoVideo.getLink().equals(linkForm.getTipos().get(0).getLink()) || tipoVideo.getTipoVideo().equals(linkForm.getTipos().get(0).getTipoVideo()) && tipoVideo.getLink().equals(linkForm.getTipos().get(0).getLink())){
						return ResponseEntity.badRequest().build();
					}
								
			}
			
			List<TipoVideo> saveAll = tipoVideoRepository.save(linkForm.getTipos());
			encontreiGtv.get().getVideos().addAll(saveAll);
			linkVideoRepository.save(encontreiGtv.get());
			
			
			LinkVideoCreateDTO tipoVideoDto = new LinkVideoCreateDTO(encontreiGtv.get(), saveAll);
			
			URI uri = uriBuilder.path("/link/{id}").buildAndExpand(linkForm.getTipos().get(0)).toUri();		
			
			return ResponseEntity.created(uri).body(tipoVideoDto);
		}
		
		LinkVideo converter = linkForm.converter();
		linkVideoRepository.save(converter);
		
		URI uri = uriBuilder.path("/link/{id}").buildAndExpand(converter.getIdLinkVideo()).toUri();		
		
		LinkVideoCreateDTO tipoVideoDto = new LinkVideoCreateDTO(converter);
		
		return ResponseEntity.created(uri).body(tipoVideoDto);
	}		
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		TipoVideo tipoVideo = tipoVideoRepository.findOne(id);
		
		tipoVideoRepository.delete(tipoVideo.getIdTipoVideo());
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/gtv/{id}")
	public ResponseEntity<?> removerGtv(@PathVariable Long id) {
		LinkVideo encontreiGtv = linkVideoRepository.findOne(id);
		
		if(encontreiGtv.getIdLinkVideo() != null) {
			linkVideoRepository.delete(id);
			return ResponseEntity.ok().build();
		}	
		
		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/{gtv}")
	public ResponseEntity<LinkVideoDTO> lista(@PathVariable Long gtv) {
		Optional<LinkVideo> encontreiGtv = linkVideoRepository.findByGtv(gtv);

		if (encontreiGtv.isPresent()) {
			return ResponseEntity.ok(new LinkVideoDTO(encontreiGtv.get()));
		}

		return ResponseEntity.notFound().build();
	}
	
}