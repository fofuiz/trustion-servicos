package br.com.accesstage.trustion.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.TipoMotivoConclusaoConverter;
import br.com.accesstage.trustion.dto.TipoMotivoConclusaoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.model.TipoMotivoConclusao;
import br.com.accesstage.trustion.service.interfaces.ITipoMotivoConclusaoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;

@Controller
public class TipoMotivoConclusaoController {

	@Autowired
	private ITipoMotivoConclusaoService tipoMotivoConclusaoService;

	@Log
	private Logger LOG;

	@RequestMapping(value = "/tiposMotivoConclusao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<TipoMotivoConclusaoDTO>> listarTodos(@RequestParam(value = "descricao", required = false) String descricao) {

		LOG.info(Utils.getInicioMetodo());

		List<TipoMotivoConclusaoDTO> dtos = null;

		try {

			dtos = new ArrayList();

			if(StringUtils.isEmpty(descricao)) {
				for (TipoMotivoConclusao entidade : tipoMotivoConclusaoService.listarMotivos()) {
					dtos.add(TipoMotivoConclusaoConverter.paraDTO(entidade));
				}
			} else {
				for (TipoMotivoConclusao entidade : tipoMotivoConclusaoService.listarMotivosPorDescricao(descricao)) {
					dtos.add(TipoMotivoConclusaoConverter.paraDTO(entidade));
				}
			}

		} catch (BadRequestResponseException e) {
			LOG.error("Exceção " + e.getMessage(), e);
			throw e;

		} catch (Exception e) {
			LOG.error("Exceção " + e.getMessage(), e);
			throw new InternalServerErrorResponseException(
					Mensagem.get("msg.nao.foi.possivel.listar", new Object[] { "tipos de status" }));
		}

		LOG.info(Utils.getFimMetodo());

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

    @RequestMapping(value = "/tiposMotivoConclusao/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoMotivoConclusaoDTO> listarPorId(@PathVariable("id") Long idMotivo) {

        LOG.info(Utils.getInicioMetodo());

        TipoMotivoConclusaoDTO dto;

        try {

            dto = TipoMotivoConclusaoConverter.paraDTO(tipoMotivoConclusaoService.listaPorId(idMotivo));

        } catch (BadRequestResponseException e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[] { "tipos de status" }));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<TipoMotivoConclusaoDTO>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/tiposMotivoConclusao", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoMotivoConclusaoDTO> criar(@RequestBody TipoMotivoConclusaoDTO tipoMotivoConclusaoDTO) {

        LOG.info(Utils.getInicioMetodo());

        TipoMotivoConclusaoDTO tipoMotivoConclusaoCriadoDTO = null;

        try {
            tipoMotivoConclusaoCriadoDTO = tipoMotivoConclusaoService.criar(tipoMotivoConclusaoDTO);

        } catch (BadRequestResponseException e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.criar", new Object[] { "tipos de status" }));
        }

        LOG.info(Utils.getFimMetodo());

        return new ResponseEntity<>(tipoMotivoConclusaoCriadoDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tiposMotivoConclusao", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoMotivoConclusaoDTO> alterar(@RequestBody TipoMotivoConclusaoDTO tipoMotivoConclusaoDTO) {

        LOG.info(Utils.getInicioMetodo());

        TipoMotivoConclusaoDTO tipoMotivoConclusaoAlteradoDTO;

        try {
            tipoMotivoConclusaoAlteradoDTO = tipoMotivoConclusaoService.alterar(tipoMotivoConclusaoDTO);

        } catch (BadRequestResponseException e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());

        } catch (Exception e) {
            LOG.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.alterar", new Object[] { "tipos de status" }));
        }

        LOG.info(Utils.getFimMetodo());

        return ResponseEntity.ok(tipoMotivoConclusaoAlteradoDTO);
    }

}
