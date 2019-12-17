package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaCadastroDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ITaxaAdministrativaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;

import java.util.Collection;
import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TaxaAdministrativaController {

    @Autowired
    private ITaxaAdministrativaService taxaAdministrativaService;

    @Log
    private static Logger LOGGER;
    
    private static final String MSG = "msg.nao.foi.possivel.listar";
    private static final String TAXA_ADM = "taxa administrativa";

    /**
     * Metodo para efetuar a pesquisa conforme os filtros selecionados na tela
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/taxaadministrativa/criterios", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TaxaAdministrativaDTO>> pesquisar(@Valid @RequestBody FiltroTaxaAdministrativaDTO filtro, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<TaxaAdministrativaDTO> dtos;
        
        try {

            dtos = taxaAdministrativaService.pesquisar(filtro);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção pesquisar " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção pesquisar " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{TAXA_ADM}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Metodo para efetuar a pesquisa paginada conforme os filtros selecionados na tela
     * @param filtro
     * @param camposInvalidos
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/taxaadministrativa/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TaxaAdministrativaDTO>> pesquisarPage(@Valid @RequestBody FiltroTaxaAdministrativaDTO filtro, BindingResult camposInvalidos, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<TaxaAdministrativaDTO> dtos;

        try {

            dtos = taxaAdministrativaService.pesquisarPage(filtro, pageable);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção pesquisarPage " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção pesquisarPage " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{TAXA_ADM}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Metodo pesquisarCadastro para efetuar a pesquisa conforme os filtros selecionados na tela para cadastro
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/taxaadministrativa/cadastro", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TaxaAdministrativaCadastroDTO>> pesquisarCadastro(@Valid @RequestBody FiltroTaxasAdministrativasCadastroDTO filtro, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<TaxaAdministrativaCadastroDTO> dtos;
        try {

            dtos = taxaAdministrativaService.pesquisarCadastro(filtro);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção pesquisarCadastro " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção pesquisarCadastro " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{TAXA_ADM}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Metodo para salvar ou atualizar taxas localizadas no pesquisarCadastro
     * @param dtos
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/taxaadministrativa/salvar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TaxaAdministrativa>> salvar(@Valid @RequestBody List<TaxaAdministrativaCadastroDTO> dtos, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<TaxaAdministrativa> rDtos;
        
        try {

            rDtos = taxaAdministrativaService.salvar(dtos);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção salvar " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção salvar " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{TAXA_ADM}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(rDtos, HttpStatus.OK);
    }
    
}
