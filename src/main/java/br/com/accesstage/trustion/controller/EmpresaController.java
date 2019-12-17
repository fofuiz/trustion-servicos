package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IEmpresaSegmentoService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import br.com.caelum.stella.validation.InvalidStateException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IEmpresaSegmentoService empresaSegmentoService;

    @Log
    private static Logger LOGGER;

    private static final String EMPRESA = "Empresa";
    private static final String EMPRESAS = "Empresas";

    @RequestMapping(value = "/empresa/validar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmpresaDTO> validar(@Valid @RequestBody EmpresaDTO dto){

        LOGGER.info(Utils.getInicioMetodo());

        EmpresaDTO empresaDTO = new EmpresaDTO();

        try {
            empresaDTO = empresaService.validar(dto);
        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção" + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.validar", new Object[]{EMPRESA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(empresaDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/empresa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmpresaDTO> criar(@Valid @RequestBody EmpresaDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        EmpresaDTO empresaDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            empresaDTO = empresaService.criar(dto);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{EMPRESA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(empresaDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/empresa", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmpresaDTO> alterar(@Valid @RequestBody EmpresaDTO dto, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        EmpresaDTO empresaDTO;

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            empresaDTO = empresaService.alterar(dto);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{EMPRESA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(empresaDTO);
    }

    @RequestMapping(value = "/empresa/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmpresaDTO> pesquisar(@PathVariable("id") Long id) {

        LOGGER.info(Utils.getInicioMetodo());

        EmpresaDTO empresaDTO;
        try {

            empresaDTO = empresaService.pesquisar(id);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{EMPRESA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(empresaDTO);
    }

    @RequestMapping(value = "/empresas/criterios/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<EmpresaDTO> listarSpec(@RequestBody EmpresaDTO empresaDTO, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo());

        Page<EmpresaDTO> lstEmpresaDTO;
        try {

            lstEmpresaDTO = empresaService.listarCriterios(empresaDTO, pageable);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return lstEmpresaDTO;
    }

    @RequestMapping(value = "/empresas/criterios", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarSpec(@RequestBody EmpresaDTO empresaDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> lstEmpresaDTO;
        try {

            lstEmpresaDTO = empresaService.listarCriterios(empresaDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lstEmpresaDTO);
    }

    @RequestMapping(value = "/empresas/grpEcon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarSpec(@RequestBody List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> lstEmpresaDTO = null;
        try {

            lstEmpresaDTO = empresaService.listarCriterios(listaGrpEconFiltroDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lstEmpresaDTO);
    }
    
    //CRIANDOOOOOOO NOVOOO
    @RequestMapping(value = "/empresas/grpEcon", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarSpecGet(@RequestBody List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> lstEmpresaDTO = null;
        try {

            lstEmpresaDTO = empresaService.listarCriterios(listaGrpEconFiltroDTO);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lstEmpresaDTO);
    }

    @RequestMapping(value = "/empresas/modelos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarEmpresasPorModelo(@RequestBody List<Long> idsModeloNegocio) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> lstEmpresaDTO = null;

        try {

            lstEmpresaDTO = empresaService.listaEmpresasPorIdsModeloNegocio(idsModeloNegocio);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(lstEmpresaDTO);
    }

    @RequestMapping(value = "/empresas/grpEcon/d1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarEmpresasD1(@RequestBody List<GrupoEconomicoDTO> filtro) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> dtos = null;
        try {

            dtos = empresaService.listarEmpresasD1(filtro);

        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(value = "/empresas/grpEcon/d0", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> listarEmpresasD0(@RequestBody List<GrupoEconomicoDTO> filtro) {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaDTO> dtos = null;
        try {

            dtos = empresaService.listarEmpresasD0(filtro);

        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{EMPRESAS}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/empresas/cnpj", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarEmpresasCNPJ() {

        LOGGER.info(Utils.getInicioMetodo());

        List<TableauFilterDTO> cnpjsDTOs;
        try {

            cnpjsDTOs = empresaService.listarEmpresasCNPJ();
            
            if (null != cnpjsDTOs && !cnpjsDTOs.isEmpty()) {
            	for (TableauFilterDTO tableauFilterDTO : cnpjsDTOs) {
            		LOGGER.error("CNPJ =============>  " + tableauFilterDTO.getCnpj());
				}
			}            

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"CNPJs das Empresas"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(cnpjsDTOs);

    }

    @RequestMapping(value = "/empresa/segmentos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaSegmentoDTO>> listarTodosEmpresaSegmento() {

        LOGGER.info(Utils.getInicioMetodo());

        List<EmpresaSegmentoDTO> dtos;
        try {

            dtos = empresaSegmentoService.listarTodos();

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Segmentos"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(value = "/empresas", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EmpresaDTO>> criarEmpresas(@Valid @RequestBody List<EmpresaDTO> dtos, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo());

        EmpresaDTO empresaDTO;
        List<EmpresaDTO> listEmpresaDTO = new ArrayList<>();

        if (camposInvalidos.hasErrors()) {
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        try {

            for (EmpresaDTO dto : dtos) {
                empresaDTO = empresaService.criar(dto);
                listEmpresaDTO.add(empresaDTO);
            }

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{EMPRESA}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(listEmpresaDTO);

    }

    @GetMapping(value = "/empresas/download", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void download(HttpServletResponse response) {

        LOGGER.info(Utils.getInicioMetodo());

        try {

            empresaService.download(response);

        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"Arquivo de Empresas para download"}));
        }

        LOGGER.info(Utils.getFimMetodo());
    }

    @RequestMapping(value = "/empresas/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void upload(@RequestParam("file") MultipartFile multipart, UploadEmpresaDTO uploadEmpresaDTO) {

        LOGGER.info(Utils.getInicioMetodo());

        try {

            empresaService.upload(multipart, uploadEmpresaDTO);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (InvalidStateException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.erro.ao.fazer.upload.do.arquivo", new Object[]{multipart.getOriginalFilename()}));
        }

        LOGGER.info(Utils.getFimMetodo());
    }

}
