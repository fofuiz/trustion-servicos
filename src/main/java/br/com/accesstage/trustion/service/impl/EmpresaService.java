package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.DadosBancariosConverter;
import br.com.accesstage.trustion.converter.EmpresaConverter;
import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.enums.PerfilEnum;
import br.com.accesstage.trustion.enums.StatusAtivoInativo;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.model.*;
import br.com.accesstage.trustion.repository.criteria.EmpresaSpecification;
import br.com.accesstage.trustion.repository.interfaces.*;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IGrupoEconomicoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.ValidarCNPJ;
import br.com.accesstage.trustion.util.Format;
import br.com.caelum.stella.validation.InvalidStateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.*;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.*;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

@Service
public class EmpresaService implements IEmpresaService {

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IModeloNegocioRepository modeloRepository;

    @Autowired
    private ITipoCreditoRepository tipoCreditoRepository;

    @Autowired
    private IGrupoEconomicoService grupoEconomicoService;

    @Autowired
    private IDadosBancariosRepository dadosBancariosRepository;

    @Autowired
    private IEmpresaModeloNegocioRepository empresaModeloNegocioRepository;

    @Autowired
    private IListaBancoRepository bancoRepository;

    @Log
    private static Logger LOGGER;

    private static final String SEMICOLON = ";";
    private static final int COLUMN_SIZE = 9;
    private static final String EMPRESAS = "Empresas";

    private final Converter<Empresa, EmpresaDTO> converter = new Converter<Empresa, EmpresaDTO>() {

        @Override
        public EmpresaDTO convert(Empresa empresa) {
            EmpresaDTO empresaDTOAux = EmpresaConverter.paraDTO(empresa);

            if (empresa.getIdGrupoEconomico() != null) {
                GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(empresa.getIdGrupoEconomico());
                empresaDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
            }

            if (empresa.getEmpresaModeloNegocioList() != null && !empresa.getEmpresaModeloNegocioList().isEmpty()) {
                empresaDTOAux.setEmpresaModeloNegocios(new ArrayList());
                empresa.getEmpresaModeloNegocioList().forEach(e -> {
                    EmpresaModeloNegocioDTO emnDTO = new EmpresaModeloNegocioDTO();
                    ModeloNegocio modelo = modeloRepository.findOne(e.getModeloNegocio().getIdModeloNegocio());
                    emnDTO.setIdEmpresa(e.getEmpresa().getIdEmpresa());
                    emnDTO.setIdModeloNegocio(e.getModeloNegocio().getIdModeloNegocio());
                    emnDTO.setModeloNegocio(modelo.getNome());
                    empresaDTOAux.getEmpresaModeloNegocios().add(emnDTO);
                });
            }

            return empresaDTOAux;
        }
    };


    @Override
    public EmpresaDTO validar(EmpresaDTO empresaDTO) {
        try {
            ValidarCNPJ.validarCNPJ(empresaDTO.getCnpj());
        }catch (InvalidStateException e){
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(Mensagem.get("msg.cnpj.invalido"));
        }
        return empresaDTO;
    }

    @Override
    public EmpresaDTO criar(EmpresaDTO empresaDTO) throws Exception {
        for (DadosBancariosDTO dadosBancarios : empresaDTO.getDadosBancarios()) {
            if (dadosBancarios.getConta() == null || dadosBancarios.getAgencia() == null
                    || dadosBancarios.getIdBanco() == null || dadosBancarios.getDv() == null) {
                throw new BadRequestResponseException("Favor preencher os dados bancários corretamente");
            }
        }

        if (empresaDTO.getCnpj() != null && !empresaDTO.getCnpj().isEmpty()) {
            ValidarCNPJ.validarCNPJ(empresaDTO.getCnpj());

            if (empresaRepository.existsByCnpj(empresaDTO.getCnpj())) {
                throw new BadRequestResponseException("CNPJ " + empresaDTO.getCnpj() + " já existe");
            }
        }

        Empresa empresa = EmpresaConverter.paraEntidade(empresaDTO);
        empresa.setDataCriacao(Calendar.getInstance().getTime());

        EmpresaDTO empresaCriada = EmpresaConverter.paraDTO(empresaRepository.save(empresa));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{"Empresa", empresaCriada.getRazaoSocial()}), null);

        if (empresaDTO.getEmpresaModeloNegocios() != null && !empresaDTO.getEmpresaModeloNegocios().isEmpty()) {
            List<EmpresaModeloNegocio> emnList = new ArrayList<>();
            for (EmpresaModeloNegocioDTO emnDTO : empresaDTO.getEmpresaModeloNegocios()) {
                EmpresaModeloNegocio emn = new EmpresaModeloNegocio(empresaCriada.getIdEmpresa(), emnDTO.getIdModeloNegocio());
                emn.setIdUsuarioCriacao(empresa.getIdUsuarioCriacao());
                emn.setDataCriacao(Calendar.getInstance().getTime());
                emnList.add(emn);
            }
            empresaModeloNegocioRepository.save(emnList);
        }

        for (DadosBancariosDTO dadosBancarios : empresaDTO.getDadosBancarios()) {
            dadosBancarios.setIdEmpresa(empresaCriada.getIdEmpresa());
            dadosBancarios.setDataCriacao(Calendar.getInstance().getTime());
            dadosBancariosRepository.save(DadosBancariosConverter.paraEntidade(dadosBancarios));
        }

        return empresaCriada;
    }

    @Override
    public EmpresaDTO alterar(EmpresaDTO empresaDTO) throws Exception {
        for (DadosBancariosDTO dadosBancarios : empresaDTO.getDadosBancarios()) {
            if (dadosBancarios.getConta() == null || dadosBancarios.getAgencia() == null
                    || dadosBancarios.getIdBanco() == null || dadosBancarios.getDv() == null) {
                throw new BadRequestResponseException("Favor preencher os dados bancários corretamente");
            }
        }

        Empresa empresa = empresaRepository.findOne(empresaDTO.getIdEmpresa());
        empresaDTO.setDataCriacao(empresa.getDataCriacao());

        BeanUtils.copyProperties(empresaDTO, empresa);
        empresa.setIdUsuarioAlteracao(empresaDTO.getIdUsuarioAlteracao());
        empresa.setDataAlteracao(Calendar.getInstance().getTime());

        EmpresaDTO empresaAlterada = EmpresaConverter.paraDTO(empresaRepository.save(empresa));
        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{"Empresa", empresaAlterada.getRazaoSocial()}), null);

        if (empresaDTO.getEmpresaModeloNegocios() != null && !empresaDTO.getEmpresaModeloNegocios().isEmpty()) {
            List<EmpresaModeloNegocio> emnList = new ArrayList<>();
            for (EmpresaModeloNegocioDTO emnDTO : empresaDTO.getEmpresaModeloNegocios()) {
                EmpresaModeloNegocio emn = new EmpresaModeloNegocio(empresaAlterada.getIdEmpresa(), emnDTO.getIdModeloNegocio());
                emn.setIdUsuarioCriacao(empresa.getIdUsuarioCriacao());
                emn.setDataCriacao(empresa.getDataCriacao());
                emn.setIdUsuarioAlteracao(empresa.getIdUsuarioAlteracao());
                emn.setDataAlteracao(empresa.getDataAlteracao());
                emnList.add(emn);
            }
            empresaModeloNegocioRepository.delete(empresa.getEmpresaModeloNegocioList());
            empresaModeloNegocioRepository.save(emnList);
        }

        for (DadosBancariosDTO dadosBancarios : empresaDTO.getDadosBancarios()) {
            dadosBancarios.setIdEmpresa(empresaAlterada.getIdEmpresa());
            dadosBancarios.setDataCriacao(Calendar.getInstance().getTime());
            String descAdicional[] = dadosBancarios.getDescricao().split("-");
            if( descAdicional != null && descAdicional.length > 1){
                Integer i = Integer.parseInt(descAdicional[0]);
                dadosBancarios.setIdBanco(i.longValue());
            }
            dadosBancariosRepository.save(DadosBancariosConverter.paraEntidade(dadosBancarios));
        }

        return empresaAlterada;
    }

    @Override
    public EmpresaDTO pesquisar(Long idEmpresa) throws Exception {
        Empresa empresa = empresaRepository.findOne(idEmpresa);
        EmpresaDTO dto;
        String idBanco;
        List<DadosBancarios> dadosBancarios;
        List<DadosBancariosDTO> dadosBancariosDTO = new ArrayList<>();
        if (empresa != null) {

            dto = converter.convert(empresa);

            dadosBancarios = dadosBancariosRepository.findAllByIdEmpresa(idEmpresa);

            if (null != dadosBancarios) {
                for (DadosBancarios dadosBancario : dadosBancarios) {
                    DadosBancariosDTO dadoBancarioDto = DadosBancariosConverter.paraDTO(dadosBancario);

                    if (dadoBancarioDto.getIdBanco() != null) {
                        idBanco = Format.insereZerosEsquerda(dadoBancarioDto.getIdBanco().toString(), 3);
                        ListaBanco listaBanco = bancoRepository.findOneByCodigoBanco(idBanco);
                        if (listaBanco != null) {
                            dadoBancarioDto.setDescricao(listaBanco.getDescricao());
                        }
                    }

                    dadosBancariosDTO.add(dadoBancarioDto);
                }

                dto.setDadosBancarios(dadosBancariosDTO);
            }
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Empresa"}));
        }
        return dto;
    }

    @Override
    public Page<EmpresaDTO> listarCriterios(EmpresaDTO empresaDTO, Pageable pageable) throws Exception {

        Page<EmpresaDTO> lstEmpresa;
        List<Long> idsGrupos = new ArrayList<>();
        List<Long> idEmpresas = new ArrayList<>();

        if (!PerfilEnum.ADMINISTRADOR.get().equalsIgnoreCase(getPerfil())) {
            idEmpresas = empresasPorUsuarioLogado().stream().map(empresa -> empresa.getIdEmpresa()).collect(Collectors.toList());
        }

        Specification<Empresa> specs = EmpresaSpecification.byCriterio(EmpresaConverter.paraEntidade(empresaDTO),
                idsGrupos, idEmpresas, Optional.ofNullable(null));

        lstEmpresa = empresaRepository.findAll(specs, pageable).map(converter);

        return lstEmpresa;

    }

    @Override
    public List<EmpresaDTO> listarCriterios(EmpresaDTO empresaDTO) throws Exception {

        List<EmpresaDTO> lstEmpresa = new ArrayList<>();
        List<Long> idsGrupos = new ArrayList<>();
        List<Long> idEmpresas = new ArrayList<>();

        if (!PerfilEnum.ADMINISTRADOR.get().equalsIgnoreCase(getPerfil()) && !PerfilEnum.BPO.get().equalsIgnoreCase(getPerfil())) {
            idsGrupos = empresasPorUsuarioLogado().stream().map(empresa -> empresa.getIdGrupoEconomico()).collect(Collectors.toList());
        }

        if (PerfilEnum.BPO.get().equalsIgnoreCase(getPerfil()) 
        		|| PerfilEnum.MASTER_CLIENTE.get().equalsIgnoreCase(getPerfil())
                || PerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get().equalsIgnoreCase(getPerfil())
        		|| PerfilEnum.MASTER_CLIENTE_CARTAO.get().equalsIgnoreCase(getPerfil()) 
        		|| PerfilEnum.MASTER_CLIENTE_NUMERARIO.get().equalsIgnoreCase(getPerfil())) {
            Usuario usuario = usuarioRepository.findOne(getIdUsuario());
            idEmpresas = usuario.getEmpresaList().stream().map(empresa -> empresa.getIdEmpresa()).collect(Collectors.toList());
        }

        Specification<Empresa> specs = EmpresaSpecification.byCriterio(EmpresaConverter.paraEntidade(empresaDTO),
                idsGrupos, idEmpresas, Optional.ofNullable(null));

        for (Empresa empresa : empresaRepository.findAll(specs)) {

            EmpresaDTO empresaDTOAux = EmpresaConverter.paraDTO(empresa);

            if (empresa.getIdGrupoEconomico() != null) {
                GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(empresa.getIdGrupoEconomico());
                empresaDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
                lstEmpresa.add(empresaDTOAux);
            } else {
                empresa.setIdGrupoEconomico(getIdUsuario());
            }

        }
        return lstEmpresa;

    }

    @Override
    public boolean excluir(Long idEmpresaDTO) throws Exception {
        if (empresaRepository.exists(idEmpresaDTO)) {
            empresaRepository.delete(idEmpresaDTO);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Empresa"}));
        }

        return true;
    }

    @Override
    public List<EmpresaDTO> listarCriterios(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception {

        List<EmpresaDTO> lstEmpresa = new ArrayList<>();
        List<Long> listaIdsGrupos = new ArrayList<>();
        Usuario usuario = null;

        if (CodigoPerfilEnum.MASTER_TRANSPORTADORA.equals(UsuarioAutenticado.getPerfilEnum())) {
            usuario = usuarioRepository.findOne(getIdUsuario());
        }

        if (listaGrpEconFiltroDTO != null) {

            if (!listaGrpEconFiltroDTO.isEmpty()) {
                listaGrpEconFiltroDTO.forEach((grpEcon) -> {
                    listaIdsGrupos.add(grpEcon.getIdGrupoEconomico());
                });

                Specification<Empresa> specs = EmpresaSpecification.byCriterio(new Empresa(), listaIdsGrupos, Collections.emptyList(), Optional.ofNullable(usuario));

                for (Empresa empresa : empresaRepository.findAll(specs)) {

                    EmpresaDTO empresaDTOAux = EmpresaConverter.paraDTO(empresa);

                    if (empresa.getIdGrupoEconomico() != null) {
                        GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(empresa.getIdGrupoEconomico());
                        empresaDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
                    }

                    lstEmpresa.add(empresaDTOAux);
                }
            }
        }

        return lstEmpresa;
    }

    @Override
    public List<EmpresaDTO> listarEmpresasD1(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception {

        List<EmpresaDTO> empresaDtos = null;

        if (getIdPerfil() == ADMINISTRADOR.get()
                || getIdPerfil() == MASTER_TRANSPORTADORA.get()
                || getIdPerfil() == OPERADOR_TRANSPORTADORA.get()
                || getIdPerfil() == MASTER_CLIENTE.get()
                || getIdPerfil() == MASTER_CLIENTE_VENDA_NUMERARIO.get()
                || getIdPerfil() == MASTER_CLIENTE_CARTAO.get()
                || getIdPerfil() == MASTER_CLIENTE_NUMERARIO.get()
                || getIdPerfil() == BPO.get()) {

            empresaDtos = new ArrayList<>();

            TipoCredito tipoD1 = tipoCreditoRepository.findOne(CodigoTipoCreditoEnum.CREDITOD1.getId());
            List<ModeloNegocio> modelosD1 = modeloRepository.findAllByidTipoCredito(tipoD1.getIdTipoCredito());

            List<Long> idsGrupos = new ArrayList<>();
            listaGrpEconFiltroDTO.forEach((grupo) -> {
                idsGrupos.add(grupo.getIdGrupoEconomico());
            });

            List<Long> idsModelos = new ArrayList<>();
            modelosD1.forEach((modelo) -> {
                idsModelos.add(modelo.getIdModeloNegocio());
            });

            List<Empresa> empresasD1 = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(idsGrupos,
                    idsModelos);

            for (Empresa entidade : empresasD1) {
                empresaDtos.add(EmpresaConverter.paraDTO(entidade));
            }

        } else if (getIdPerfil() == OPERADOR_CLIENTE.get()
                || getIdPerfil() == OPERADOR_CLIENTE_VENDA_NUMERARIO.get()
        		|| getIdPerfil() == OPERADOR_CLIENTE_CARTAO.get()
        		|| getIdPerfil() == OPERADOR_CLIENTE_NUMERARIO.get()) {
            empresaDtos = new ArrayList<>();
            List<Empresa> empresas = empresasPorUsuarioLogado();
            for (Empresa empresa : empresas) {
                if (StatusAtivoInativo.ATIVO.getTexto().equals(empresa.getStatus())) {
                    empresaDtos.add(EmpresaConverter.paraDTO(empresa));
                }
            }
        }
        return empresaDtos;
    }

    @Override
    public List<EmpresaDTO> listarEmpresasD0(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception {
        List<EmpresaDTO> empresaDtos = null;
        Usuario usuario = usuarioRepository.findOne(getIdUsuario());

        if (listaGrpEconFiltroDTO.isEmpty()) {
            GrupoEconomicoDTO dto;
            listaGrpEconFiltroDTO = new ArrayList<>();
            for (Empresa empresa : empresasPorUsuarioLogado()) {
                dto = new GrupoEconomicoDTO();
                dto.setIdGrupoEconomico(empresa.getIdGrupoEconomico());
                if (!listaGrpEconFiltroDTO.contains(dto)) {
                    listaGrpEconFiltroDTO.add(dto);
                }
            }
        }

        if (getIdPerfil() == ADMINISTRADOR.get()
                || getIdPerfil() == MASTER_TRANSPORTADORA.get()
                || getIdPerfil() == OPERADOR_TRANSPORTADORA.get()
                || getIdPerfil() == MASTER_CLIENTE.get()
                || getIdPerfil() == MASTER_CLIENTE_VENDA_NUMERARIO.get()
                || getIdPerfil() == MASTER_CLIENTE_CARTAO.get()
                || getIdPerfil() == MASTER_CLIENTE_NUMERARIO.get()
                || getIdPerfil() == BPO.get()) {

            empresaDtos = new ArrayList<>();

            TipoCredito tipoD0 = tipoCreditoRepository.findOne(CodigoTipoCreditoEnum.CREDITOD0.getId());
            List<ModeloNegocio> modelosD0 = new LinkedList<>();
            modelosD0 = modeloRepository.findAllByidTipoCredito(tipoD0.getIdTipoCredito());

            List<Long> idsGrupos = new ArrayList<>();
            listaGrpEconFiltroDTO.forEach((grupo) -> {
                idsGrupos.add(grupo.getIdGrupoEconomico());
            });

            List<Long> idsModelos = new ArrayList<>();
            modelosD0.forEach((modelo) -> {
                idsModelos.add(modelo.getIdModeloNegocio());
            });

            List<Empresa> empresasD0 = empresaRepository.findDistinctByIdGrupoEconomicoInAndStatusOrderByRazaoSocial(idsGrupos,StatusAtivoInativo.ATIVO.getTexto());
//            List<Empresa> empresasD0 = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioInAndStatusOrderByRazaoSocial(
//                    idsGrupos, idsModelos, StatusAtivoInativo.ATIVO.getTexto());

            for (Empresa entidade : empresasD0) {
                empresaDtos.add(EmpresaConverter.paraDTO(entidade));
            }

        } else if (getIdPerfil() == OPERADOR_CLIENTE.get()
                || getIdPerfil() == OPERADOR_CLIENTE_VENDA_NUMERARIO.get()
        		|| getIdPerfil() == OPERADOR_CLIENTE_CARTAO.get()
        		|| getIdPerfil() == OPERADOR_CLIENTE_NUMERARIO.get()) {
            empresaDtos = new ArrayList<>();
            List<Empresa> empresas = empresasPorUsuarioLogado();
            for (Empresa empresa : empresas) {
                if (StatusAtivoInativo.ATIVO.getTexto().equals(empresa.getStatus())) {
                    empresaDtos.add(EmpresaConverter.paraDTO(empresa));
                }
            }
        } else {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }
        return empresaDtos;
    }

    @Override
    public List<TableauFilterDTO> listarEmpresasCNPJ() throws Exception {

        List<EmpresaDTO> empresaOutros = new ArrayList<>();

        if (getIdPerfil() == ADMINISTRADOR.get()) {
            return Collections.singletonList(new TableauFilterDTO("0"));
        } else {
            List<Empresa> empresas = empresasPorUsuarioLogado();
            for (Empresa empresa : empresas) {
                if (StatusAtivoInativo.ATIVO.getTexto().equals(empresa.getStatus())) {
                    empresaOutros.add(EmpresaConverter.paraDTO(empresa));
                }
            }
        }

        List<TableauFilterDTO> dtos = empresaOutros.stream().map(EmpresaDTO::getCnpj).distinct()
                .map(TableauFilterDTO::new).collect(Collectors.toList());

        if (dtos.isEmpty()) {
            dtos = Collections.singletonList(new TableauFilterDTO("0"));
        }

        return dtos;
    }

    @Override
    public List<EmpresaDTO> listaEmpresasPorIdsModeloNegocio(List<Long> idsModeloNegocio) throws Exception {
        List<EmpresaDTO> empresaDtos = new ArrayList<>();

        List<Empresa> empresas = empresasPorUsuarioLogado();
        for (Empresa empresa : empresas) {
            for (EmpresaModeloNegocio emn : empresa.getEmpresaModeloNegocioList()) {
                if (idsModeloNegocio.contains(emn.getEmpresaModeloNegocioPK().getIdModeloNegocio())) {
                    empresaDtos.add(EmpresaConverter.paraDTO(empresa));
                    break;
                }
            }
        }

        return empresaDtos;
    }

    @Override
    public void download(HttpServletResponse response) throws Exception {

        String fileName = EMPRESAS + "-" + System.currentTimeMillis() + ".csv";

        StringBuilder sb = new StringBuilder();
        sb.append("razao_social").append(SEMICOLON)
                .append("cnpj").append(SEMICOLON)
                .append("endereco").append(SEMICOLON)
                .append("cidade").append(SEMICOLON)
                .append("estado").append(SEMICOLON)
                .append("cep").append(SEMICOLON)
                .append("sigla_loja").append(SEMICOLON)
                .append("id_empresa_segmento").append(SEMICOLON)
                .append("status").append(StringUtils.LF);

        byte[] bytes = sb.toString().getBytes();
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType("text/plain; charset=ISO-8859-1");
        response.setCharacterEncoding("ISO-8859-1");

        response.getOutputStream().write(bytes);
        response.flushBuffer();

    }

    @Override
    public void upload(MultipartFile multipart, UploadEmpresaDTO uploadEmpresaDTO) throws Exception {

        List<EmpresaDTO> empresas = new ArrayList<>();

        String fileName = multipart.getOriginalFilename();

        if (!fileName.contains(".csv")) {
            throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.extensao.csv", new Object[]{fileName}));
        }

        if (multipart.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.vazio", new Object[]{fileName}));
        }

        String content = new String(multipart.getBytes(), "UTF-8");

        if (!content.contains(SEMICOLON)) {
            throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.invalido", new Object[]{fileName}));
        }

        //necessario criar um dado bancario fixo para poder criar empresa
        DadosBancariosDTO dadosBancario = new DadosBancariosDTO(1L, 1L, 1L, 1L);
        List<DadosBancariosDTO> dadosBancarios = Arrays.asList(dadosBancario);

        BufferedReader br = new BufferedReader(new InputStreamReader(multipart.getInputStream(), "UTF-8"));
        //necessario para ler a primeira linha do arquivo
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] empresa = line.split(SEMICOLON);
            if (COLUMN_SIZE != empresa.length) {
                throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.invalido", new Object[]{fileName}));
            }
            int i = 0;
            EmpresaDTO dto = new EmpresaDTO();
            dto.setRazaoSocial(empresa[i++]);
            dto.setCnpj(empresa[i++]);
            dto.setEndereco(empresa[i++]);
            dto.setCidade(empresa[i++]);
            dto.setEstado(empresa[i++]);
            dto.setCep(empresa[i++]);
            dto.setSiglaLoja(empresa[i++]);
            String idEmpresaSegmento = empresa[i++];
            if (StringUtils.isNumeric(idEmpresaSegmento)) {
                dto.setIdEmpresaSegmento(Long.parseLong(idEmpresaSegmento));
            }
            dto.setStatus(empresa[i++]);
            dto.setIdUsuarioCriacao(getIdUsuario());
            dto.setDadosBancarios(dadosBancarios);

            if (uploadEmpresaDTO != null && !uploadEmpresaDTO.getIdModeloNegocio().isEmpty()) {
                dto.setIdGrupoEconomico(uploadEmpresaDTO.getIdGrupoEconomico());
                List<EmpresaModeloNegocioDTO> l = new ArrayList<>();
                uploadEmpresaDTO.getIdModeloNegocio().forEach(e -> {
                    EmpresaModeloNegocioDTO emn = new EmpresaModeloNegocioDTO();
                    emn.setIdModeloNegocio(e);
                    l.add(emn);
                });
                dto.setEmpresaModeloNegocios(l);
            }

            empresas.add(dto);
        }
        if (empresas.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.invalido", new Object[]{fileName}));
        }
        //cria as empresas recebidas no arquivo
        for (EmpresaDTO empresa : empresas) {
            criar(empresa);
        }

    }

    @Override
    public List<EmpresaDTO> listaEmpresasPorGrupoEcon(List<Long> listaIdGrupoEcon) throws Exception {
        List<EmpresaDTO> empresaDtos = new ArrayList<>();
        List<Empresa> empresaList = empresaRepository.findAllByIdGrupoEconomicoIn(listaIdGrupoEcon);

        empresaList.forEach((entidade) -> {
            empresaDtos.add(EmpresaConverter.paraDTO(entidade));
        });
        return empresaDtos;
    }

    @Override
    public List<Empresa> empresasPorUsuario(Usuario usuario) {
        List<Empresa> empresas = new ArrayList<>();
        List<ModeloNegocio> modeloDeNegocioList = new ArrayList<>();

        try {

            Optional<CodigoPerfilEnum> codigoPerfilEnum = CodigoPerfilEnum.gerarEnum(usuario.getIdPerfil());

            if (codigoPerfilEnum.isPresent()) {
                switch (codigoPerfilEnum.get()) {
                    case ADMINISTRADOR:
                        empresas = empresaRepository.findAll();
                        break;
                    case MASTER_TRANSPORTADORA:
                    case OPERADOR_TRANSPORTADORA:

                        if (isNotEmpty(usuario.getTransportadoraList())) {
                            usuario.getTransportadoraList().stream().forEach(transportadora -> {
                                modeloDeNegocioList.addAll(modeloRepository.findByIdTransportadora(transportadora.getIdTransportadora()));

                            });
                        }
                        if (isNotEmpty(modeloDeNegocioList)) {
                            for (ModeloNegocio modeloNegocio : modeloDeNegocioList) {
                                for (EmpresaModeloNegocio empresaModeloNegocio : modeloNegocio.getEmpresaModeloNegocioCollection()) {
                                    empresas.add(empresaModeloNegocio.getEmpresa());
                                }
                            }

                        }
                        break;

                    case MASTER_CLIENTE:
                    case MASTER_CLIENTE_VENDA_NUMERARIO:
                    case MASTER_CLIENTE_CARTAO:
                    case MASTER_CLIENTE_NUMERARIO:
                    case OPERADOR_CLIENTE:
                    case OPERADOR_CLIENTE_VENDA_NUMERARIO:
                    case OPERADOR_CLIENTE_CARTAO:
                    case OPERADOR_CLIENTE_NUMERARIO:
                    case BPO:
                        empresas = usuario.getEmpresaList();
                        break;

                    default:
                        break;
                }

            } else {
                throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
            }
        } catch (Exception e) {
            LOGGER.error("ERRO ao buscar empresas do usuário logado");
            LOGGER.error(e.getMessage());

        }
        return empresas;
    }

    @Override
    public List<Empresa> empresasPorUsuarioLogado() {
        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

        return empresasPorUsuario(usuario);
    }

    @Override
    public List<EmpresaDTO> listaEmpresasPorUsuarioLogado() throws Exception {
        List<EmpresaDTO> dtos = new ArrayList<>();
        List<Long> idsTransportadora = new ArrayList<>();
        List<Long> idsModeloNegocio = new ArrayList<>();
        List<Long> idsEmpresa = new ArrayList<>();

        try {
            Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

            Optional<PerfilEnum> perfilEnum = PerfilEnum.getByDescription(UsuarioAutenticado.getPerfil());

            if (perfilEnum.isPresent()) {
                switch (perfilEnum.get()) {
                    case ADMINISTRADOR:
                        List<Empresa> empresas = empresaRepository.findAll();

                        if (isNotEmpty(empresas)) {
                            empresas.forEach(empresa -> {
                                dtos.add(EmpresaConverter.paraDTO(empresa));
                            });
                        }
                        break;
                    case MASTER_TRANSPORTADORA:
                    case OPERADOR_TRANSPORTADORA:
                        if (isNotEmpty(usuario.getTransportadoraList())) {
                            usuario.getTransportadoraList().forEach(transportadora -> {
                                idsTransportadora.add(transportadora.getIdTransportadora());
                            });
                        }

                        if (isNotEmpty(idsTransportadora)) {
                            idsTransportadora.forEach(idTransportadora -> {
                                List<ModeloNegocio> modeloDeNegocioList = modeloRepository.findByIdTransportadora(idTransportadora);
                                modeloDeNegocioList.forEach(modeloNegocio -> {
                                    idsModeloNegocio.add(modeloNegocio.getIdModeloNegocio());
                                });

                            });
                        }

                        if (isNotEmpty(idsModeloNegocio)) {
                            dtos.addAll(listaEmpresasPorIdsModeloNegocio(idsModeloNegocio));
                            dtos.forEach(empresa -> {
                                idsEmpresa.add(empresa.getIdEmpresa());
                            });
                        }
                        break;

                    case MASTER_CLIENTE:
                    case MASTER_CLIENTE_VENDA_NUMERARIO:
                    case MASTER_CLIENTE_CARTAO:
                    case MASTER_CLIENTE_NUMERARIO:
                    case OPERADOR_CLIENTE:
                    case OPERADOR_CLIENTE_VENDA_NUMERARIO:
                    case OPERADOR_CLIENTE_CARTAO:
                    case OPERADOR_CLIENTE_NUMERARIO:
                    case BPO:
                        if (isNotEmpty(usuario.getEmpresaList())) {
                            usuario.getEmpresaList().forEach(empresa -> {
                                idsEmpresa.add(empresa.getIdEmpresa());
                            });
                        }
                        break;

                    default:
                        break;
                }

                if (dtos.isEmpty() && !idsEmpresa.isEmpty()) {
                    idsEmpresa.forEach(idEmpresa -> {
                        Empresa empresa = empresaRepository.findOne(idEmpresa);
                        dtos.add(EmpresaConverter.paraDTO(empresa));
                    });
                }
            } else {
                throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
            }
        } catch (Exception e) {
            LOGGER.error("ERRO ao buscar empresas do usuário logado");
            LOGGER.error(e.getMessage());

        }
        return dtos;
    }

}
