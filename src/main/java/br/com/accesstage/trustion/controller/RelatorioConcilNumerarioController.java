package br.com.accesstage.trustion.controller;

import javax.validation.Valid;

import br.com.accesstage.trustion.model.ConciliacaoVendasPac;
import br.com.accesstage.trustion.service.interfaces.IRelatorioConcilNumerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.dto.ConciliacaoVendasNumerarioDTO;
import br.com.accesstage.trustion.dto.FiltroConciliacaoVendasNumerarioDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/relConciliacaoVendasNumerario")
public class RelatorioConcilNumerarioController {

	@Autowired
	private IRelatorioConcilNumerarioService relatorioConcilNumerarioService;

	@PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ConciliacaoVendasNumerarioDTO>> listarVideosGtv(
			@RequestBody @Valid FiltroConciliacaoVendasNumerarioDTO filtros, BindingResult camposInvalidos,
			Pageable paginacao) {

		log.info(Utils.getInicioMetodo());

		if (camposInvalidos.hasErrors()) {
			throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
		}
		
		Page<ConciliacaoVendasNumerarioDTO> listDTOConsolidado;

		try {
			Page<ConciliacaoVendasPac> listVideoGTV = this.relatorioConcilNumerarioService.findAllWithFilters(
					filtros.getDataInicial(),
					filtros.getDataFinal(),
					filtros.getGtv(), 
					paginacao);

			//ConvertToDTO
			listDTOConsolidado = ConciliacaoVendasNumerarioDTO.converter(listVideoGTV);

		} catch (ForbiddenResponseException | ResourceNotFoundException e) {
			log.error("Exceção " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error("Exceção " + e.getMessage(), e);
			throw new InternalServerErrorResponseException(
					Mensagem.get("msg.nao.foi.possivel.listar", new Object[] { "Relatorio de Conciliacao Numerario" }));
		}

		log.info(Utils.getFimMetodo());

		return new ResponseEntity<>(listDTOConsolidado, HttpStatus.OK);
	}

}
