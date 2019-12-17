package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ITaxaAntecipacaoService;
import br.com.accesstage.trustion.util.Mensagem;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TaxaAntecipacaoController {

    @Autowired
    private ITaxaAntecipacaoService taxaAntecipacaoService;

    @Log
    private static Logger LOGGER;
    
    private static final String MSG = "msg.nao.foi.possivel.listar";

    /**
     * Metodo pesquisar para efetuar a pesquisa conforme os filtros selecionados na tela
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/taxaantecipacao/criterios", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TaxaAntecipacaoDTO>> pesquisar(@Valid @RequestBody FiltroTaxaAntecipacaoDTO filtro, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<TaxaAntecipacaoDTO> dtos;

        try {
            dtos = taxaAntecipacaoService.pesquisar(filtro);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção pesquisar " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção pesquisar " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"taxa antecipacao"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Metodo pesquisarPage para efetuar a pesquisa paginada conforme os filtros selecionados na tela
     * @param filtro
     * @param camposInvalidos
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/taxaantecipacao/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TaxaAntecipacaoDTO>> pesquisarPage(@Valid @RequestBody FiltroTaxaAntecipacaoDTO filtro, BindingResult camposInvalidos, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        Page<TaxaAntecipacaoDTO> dtos;

        try {
            dtos = taxaAntecipacaoService.pesquisarPage(filtro, pageable);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção pesquisarPage " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção pesquisarPage " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"taxa antecipacao"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
