package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.CategoriaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.impl.CategoriaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Log
    private static Logger LOGGER;

    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        CategoriaDTO dto;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            dto = categoriaService.criar(categoriaDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"Categoria"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CategoriaDTO> alterar(@Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        CategoriaDTO dto;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            dto = categoriaService.alterar(categoriaDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"Categoria"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoriaDTO> pesquisar(@PathVariable("id") Integer id) {

        LOGGER.info(Utils.getInicioMetodo());

        CategoriaDTO dto;

        try {

            dto = categoriaService.pesquisar(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{"Categoria"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/criterios/page")
    public Page<CategoriaDTO> listarSpec(@RequestBody CategoriaDTO categoriaDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<CategoriaDTO> lstCategoriaDTO;

        try {

            lstCategoriaDTO = categoriaService.listarCriterios(categoriaDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Categoria"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return lstCategoriaDTO;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CategoriaDTO> excluir(@PathVariable("id") Integer idCategoria) {

        LOGGER.info(Utils.getInicioMetodo());

        try {
            categoriaService.excluir(idCategoria);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.excluir", new Object[]{"Categoria"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
