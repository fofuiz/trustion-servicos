package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.TipoQuestionamentoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ITipoQuestionamentoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tiposQuestionamentos")
public class TipoQuestionamentoController {

    @Autowired
    private ITipoQuestionamentoService tipoQuestionamentoService;

    @Log
    private static Logger LOGGER;

    @GetMapping
    public ResponseEntity<Collection<TipoQuestionamentoDTO>> listar(@RequestParam(value = "descricao", required = false) final String descricao) {

        LOGGER.info(Utils.getInicioMetodo());

        List<TipoQuestionamentoDTO> dtos = null;
        try {
            
            if (StringUtils.isEmpty(descricao))
                dtos = tipoQuestionamentoService.listarTodos();
            else
                dtos = tipoQuestionamentoService.listarPorDescricao(descricao);
            
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de questionamento"}));
        }

        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoQuestionamentoDTO> listar(@PathVariable Long id) {
        LOGGER.info(Utils.getInicioMetodo());
        
        TipoQuestionamentoDTO tipoQuestionamentoDTO = new TipoQuestionamentoDTO();
        
        try {
            tipoQuestionamentoDTO = tipoQuestionamentoService.pesquisar(id);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"tipos de questionamento"}));
        }
        
        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(tipoQuestionamentoDTO);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criar(@RequestBody @Valid TipoQuestionamentoDTO tipoQuestionamentoDTO) {
        LOGGER.info(Utils.getInicioMetodo());
        
        try {
            tipoQuestionamentoService.criar(tipoQuestionamentoDTO);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"tipos de questionamento"}));
        }
        
        LOGGER.info(Utils.getFimMetodo());
    }
    
    @PutMapping()
    public ResponseEntity<TipoQuestionamentoDTO> atualizar(@RequestBody TipoQuestionamentoDTO tipoQuestionamentoDTO) {
        LOGGER.info(Utils.getInicioMetodo());
        
        TipoQuestionamentoDTO tipoQuestionamentoDTOEditado;
        
        try {
            tipoQuestionamentoDTOEditado  = tipoQuestionamentoService.alterar(tipoQuestionamentoDTO);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"tipos de questionamento"}));
        }
        
        LOGGER.info(Utils.getFimMetodo());
        return ResponseEntity.ok(tipoQuestionamentoDTOEditado);
    }
}