package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.PeriodoResumoNumerarioDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoNumerarioService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/periodo/numerario")
public class PeriodoResumoNumerarioController {


    private IPeriodoResumoNumerarioService periodoResumoNumerarioService;

    private IUsuarioService usuarioService;

    @Log
    private Logger LOG;

    @Autowired
    public PeriodoResumoNumerarioController(
            IPeriodoResumoNumerarioService periodoResumoNumerarioService,
            IUsuarioService usuarioService) {
        this.periodoResumoNumerarioService = periodoResumoNumerarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodoResumoNumerarioDTO> consultar(){
        LOG.info(Utils.getInicioMetodo());

        PeriodoResumoNumerarioDTO dto;

        try{
            dto = periodoResumoNumerarioService.pesquisar(UsuarioAutenticado.getIdUsuario());
        }catch (BadRequestResponseException | ForbiddenResponseException e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{"resumo"}));
        }

        LOG.info(Utils.getFimMetodo());
        return ResponseEntity.ok(dto);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodoResumoNumerarioDTO> alterar(@RequestBody @Valid PeriodoResumoNumerarioDTO dto, BindingResult camposInvalidos){

        if(camposInvalidos.hasErrors())
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));

        PeriodoResumoNumerarioDTO dtoPesquisa;

        try{
            dtoPesquisa = periodoResumoNumerarioService.alterar(dto);
        }catch (BadRequestResponseException | ForbiddenResponseException e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"resumo"}));
        }

        return ResponseEntity.ok(dtoPesquisa);
    }

    @GetMapping(value = "/gerar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> gerar() throws Exception {
        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.ADMINISTRADOR.get())
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"resumo"}));

        List<UsuarioDTO> usuDTOs = usuarioService.listarTodos();

        for(UsuarioDTO usuarioDTO: usuDTOs){
            try{
                if(usuarioDTO.getIdPerfil() == CodigoPerfilEnum.ADMINISTRADOR.get()) continue;
                periodoResumoNumerarioService.criar(usuarioDTO);
            }catch (Exception e){
                LOG.error("Resumo numerário: " + e.getLocalizedMessage(), e);
            }
        }

        return ResponseEntity.ok("Geração executada com sucesso!");
    }
}
