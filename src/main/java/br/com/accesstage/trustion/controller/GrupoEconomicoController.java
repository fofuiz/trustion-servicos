package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoRepository;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IGrupoEconomicoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrupoEconomicoController {

    @Autowired
    private IGrupoEconomicoService grupoService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IRelatorioAnaliticoCreditoRepository relatorioAnaliticoCreditoRepository;

    @Log
    private static Logger LOGGER;

    @PostMapping(value = "/grupoEconomico", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GrupoEconomicoDTO> criar(@Valid @RequestBody GrupoEconomicoDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        GrupoEconomicoDTO grupoCriadoDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            grupoCriadoDTO = grupoService.criar(dto);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"Grupo"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(grupoCriadoDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/grupoEconomico", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GrupoEconomicoDTO> alterar(@Valid @RequestBody GrupoEconomicoDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        GrupoEconomicoDTO grupoAlteradoDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {
            grupoAlteradoDTO = grupoService.alterar(dto);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"Grupo"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(grupoAlteradoDTO);
    }

    @RequestMapping(value = "/grupoEconomico/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GrupoEconomicoDTO> excluir(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        try {
            grupoService.excluir(id);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.excluir", new Object[]{"Grupo"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/gruposEconomicos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GrupoEconomicoDTO>> listar() {

        LOGGER.info(Utils.getInicioMetodo());

        List<GrupoEconomicoDTO> dtos;
        try {
            dtos = grupoService.listarTodos();
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/gruposEconomicos/ocorrencia/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarPorIdOcorencia(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());
        EmpresaDTO empresaDTO = new EmpresaDTO();

        List<EmpresaDTO> dtos = new ArrayList<>();
        try {
            RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioAnaliticoCreditoRepository.findOneByIdOcorrencia(id);

            if (relatorioAnaliticoCredito != null) {
                empresaDTO.setIdGrupoEconomico(relatorioAnaliticoCredito.getIdGrupoEconomico());
                dtos = empresaService.listarCriterios(empresaDTO);
            }
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/gruposEconomicos/perfil/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GrupoEconomicoDTO>> listarPorPerfilUsuario(@PathVariable("id") Long idPerfil) {

        LOGGER.info(Utils.getInicioMetodo());

        List<GrupoEconomicoDTO> dtos;
        try {
            dtos = grupoService.listarPorPerfilUsuario(idPerfil);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/gruposEconomicos/criterio", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Collection<GrupoEconomicoDTO>> listarGruposPorSpecs(@RequestBody GrupoEconomicoDTO dto) {

        LOGGER.info(Utils.getInicioMetodo());

        List<GrupoEconomicoDTO> dtos;
        try {
            dtos = grupoService.listarGruposSpecs(dto);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/gruposEconomicos/criterio/page", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Page<GrupoEconomicoDTO>> listarGruposPorSpecs(@RequestBody GrupoEconomicoDTO dto, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<GrupoEconomicoDTO> dtos = null;

        try {
            dtos = grupoService.listarGruposSpecs(dto, pageable);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/grupoEconomico/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GrupoEconomicoDTO> pesquisar(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        GrupoEconomicoDTO dto;
        try {
            dto = grupoService.pesquisar(id);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{"Grupo"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/gruposEconomicos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GrupoEconomicoDTO>> listarPorUsuario(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        List<GrupoEconomicoDTO> dtos;
        try {
            dtos = grupoService.listarPorUsuario(id);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Grupos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }
}
