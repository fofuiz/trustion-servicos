package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.ModeloNegocioConverter;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.model.EmpresaModeloNegocio;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.SlaAtendimento;
import br.com.accesstage.trustion.model.TipoCredito;
import br.com.accesstage.trustion.repository.criteria.ModeloNegocioSpecification;
import br.com.accesstage.trustion.repository.interfaces.*;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdPerfil;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdUsuario;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

@Service
public class ModeloNegocioService implements IModeloNegocioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IModeloNegocioRepository modeloRepository;

    @Autowired
    private ISlaAtendimentoRepository slaAtendimentoRepository;

    @Autowired
    private ITipoCreditoRepository tipoCreditoRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private SlaAtendimentoService slaAtendimentoService;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaModeloNegocioRepository empresaModeloNegocioRepository;

    @Autowired
    private IModeloNegocioRepository modeloNegocioRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    private final Converter<ModeloNegocio, ModeloNegocioDTO> converter = (ModeloNegocio modelo) -> {
        ModeloNegocioDTO dto = ModeloNegocioConverter.paraDTO(modelo);
        setExtraFields(dto);
        return dto;
    };

    @Override
    public ModeloNegocioDTO criar(ModeloNegocioDTO dto) throws Exception {
        ModeloNegocio modelo = ModeloNegocioConverter.paraEntidade(dto);
        modelo.setDataCriacao(Calendar.getInstance().getTime());
        modelo.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());

        ModeloNegocioDTO modeloCriado = ModeloNegocioConverter.paraDTO(modeloRepository.save(modelo));
        modeloCriado.setTipoCredito(tipoCreditoRepository.findOne(modeloCriado.getIdTipoCredito()).getDescricao());


        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado",
                new Object[]{"Modelo de " + UTF8.Negocio, modeloCriado.getNome()}), null);

        SlaAtendimento sla = ModeloNegocioConverter.dtoParaSlaAtendimento(dto);
        sla.setIdModeloNegocio(modeloCriado.getIdModeloNegocio());
        slaAtendimentoService.criar(sla);

        return modeloCriado;
    }

    @Override
    public ModeloNegocioDTO alterar(ModeloNegocioDTO dto) throws Exception {
        ModeloNegocioDTO modeloAlterado = null;
        ModeloNegocio modeloPesquisa = modeloRepository.findOne(dto.getIdModeloNegocio());

        if (modeloPesquisa != null) {
            ModeloNegocio modelo = ModeloNegocioConverter.paraEntidade(dto);
            modelo.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            modelo.setDataAlteracao(Calendar.getInstance().getTime());

            modeloAlterado = ModeloNegocioConverter.paraDTO(modeloRepository.save(modelo));

            SlaAtendimento slaAtendimento = ModeloNegocioConverter.dtoParaSlaAtendimento(dto);
            slaAtendimentoService.alterar(slaAtendimento);

            if (modeloAlterado != null) {
                modeloAlterado.setTipoCredito(
                        tipoCreditoRepository.findOne(modeloAlterado.getIdTipoCredito()).getDescricao());
            }

            auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado",
                    new Object[]{"Modelo de " + UTF8.Negocio, modeloAlterado.getNome()}), null);
        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"modelo de " + UTF8.negocio}));
        }
        return modeloAlterado;
    }

    @Override
    public ModeloNegocioDTO pesquisar(Long idModeloNegocio) throws Exception {
        ModeloNegocioDTO dto = null;
        ModeloNegocio modelo = modeloRepository.findOne(idModeloNegocio);
        SlaAtendimento slaAtendimento = slaAtendimentoRepository.findByIdModeloNegocio(idModeloNegocio);

        if (modelo != null) {
            dto = ModeloNegocioConverter.paraDTO(modelo);
            setExtraFields(dto);

            if (slaAtendimento != null) {
                dto = ModeloNegocioConverter.slaAtendimentoParaDTO(dto, slaAtendimento);
            }

        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"modelo de " + UTF8.negocio}));
        }
        return dto;
    }

    @Override
    public List<ModeloNegocioDTO> listarCriterio(ModeloNegocioDTO dto) throws Exception {
        List<ModeloNegocioDTO> dtos = new ArrayList<>();
        Specification<ModeloNegocio> specs = ModeloNegocioSpecification
                .byCriterio(ModeloNegocioConverter.paraEntidade(dto));
        for (ModeloNegocio modelo : modeloRepository.findAll(specs)) {
            ModeloNegocioDTO dtoInner = ModeloNegocioConverter.paraDTO(modelo);
            dtos.add(setExtraFields(dtoInner));
        }
        return dtos;
    }

    @Override
    public boolean excluir(Long idModeloNegocio) throws Exception {
        if (modeloRepository.exists(idModeloNegocio)) {
            modeloRepository.delete(idModeloNegocio);
        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"modelo de " + UTF8.negocio}));
        }
        return true;
    }

    @Override
    public Page<ModeloNegocioDTO> listarCriterio(ModeloNegocioDTO dto, Pageable pageable) throws Exception {
        Specification<ModeloNegocio> specs = ModeloNegocioSpecification
                .byCriterio(ModeloNegocioConverter.paraEntidade(dto));
        Page<ModeloNegocioDTO> dtos = modeloRepository.findAll(specs, pageable).map(converter);
        return dtos;
    }

    @Override
    public List<Long> listarIdsModeloNegocioPorTipoCredito(TipoCredito tipoCredito) {
        List<Long> list = new ArrayList<>();
        if (tipoCredito != null && tipoCredito.getIdTipoCredito() != null) {
            List<ModeloNegocio> modelos = modeloRepository.findAllByidTipoCredito(tipoCredito.getIdTipoCredito());
            modelos.forEach((modeloNegocio) -> {
                list.add(modeloNegocio.getIdModeloNegocio());
            });
        }
        return list;
    }

    @Override
    public Set<ModeloNegocioDTO> listarPorIdEmpresa() throws Exception {

       Set<ModeloNegocioDTO> modeloNegocioDTOSet = new HashSet<>();
       List<EmpresaDTO> lstEmpresaDTO = empresaService.listaEmpresasPorUsuarioLogado();

       carregarModeloNegocio(modeloNegocioDTOSet, lstEmpresaDTO);

       return modeloNegocioDTOSet;
    }
    
    @Override
    public Set<ModeloNegocioDTO> listarPorIdEmpresa(Long idEmpresa) throws Exception {
        
        Set<ModeloNegocioDTO> modeloNegocioDTOSet = new HashSet<>();
        
        if (idEmpresa != null) {
            Empresa empresa = empresaRepository.findOne(idEmpresa);
            List<EmpresaModeloNegocio> empresaModeloNegocioList = empresaModeloNegocioRepository.findByEmpresa(empresa);
            empresaModeloNegocioList.forEach(emn -> {
                modeloNegocioDTOSet.add(ModeloNegocioConverter.paraDTO(emn.getModeloNegocio()));
            });
        }
        
        return modeloNegocioDTOSet;
    }    

    /**
     * Método responsável por verificar se o {@link ModeloNegocio} a ser carregado atende seguinte regra de negócio:
     * Primeiro é checado se o perfil o do {@link Usuario} é ADMINISTRADOR, caso seja é carregado todos os {@link ModeloNegocio}.
     * Caso seja qualquer outro perfil, é carregado apenas os {@link ModeloNegocio} que aquele {@link Usuario} tem acesso.
     * @param modeloNegocioDTOSet {@link Set} <{@link ModeloNegocioDTO}> que será preenchida
     * @param empresaDTOList {@link List}<{@link EmpresaDTO}> utilizada para pesquisar a lista de {@link EmpresaModeloNegocio}
     */
    private void carregarModeloNegocio(Set<ModeloNegocioDTO> modeloNegocioDTOSet, List<EmpresaDTO> empresaDTOList) {

        Optional<CodigoPerfilEnum> codigoPerfilEnum = CodigoPerfilEnum.gerarEnum(getIdPerfil());
        Usuario usuario = usuarioRepository.findOne(getIdUsuario());

        switch (codigoPerfilEnum.get()){
            case ADMINISTRADOR :
                List<ModeloNegocio> modeloNegocioList = modeloNegocioRepository.findAll();
                modeloNegocioList.stream().forEach(modeloNegocio -> {
                    ModeloNegocioDTO dtoInner = ModeloNegocioConverter.paraDTO(modeloNegocio);
                    modeloNegocioDTOSet.add(setExtraFields(dtoInner));
                });

                break;

            case MASTER_TRANSPORTADORA:
            case OPERADOR_TRANSPORTADORA:
                if (isNotEmpty(usuario.getTransportadoraList())) {
                    usuario.getTransportadoraList().stream().forEach(transportadora -> {
                        transportadora.getModeloNegocioList().stream().forEach(modeloNegocio -> {
                            ModeloNegocioDTO dtoInner = ModeloNegocioConverter.paraDTO(modeloNegocio);
                            modeloNegocioDTOSet.add(setExtraFields(dtoInner));
                        });

                    });
                }


                break;

            default:
                empresaDTOList.forEach(empresaDTO -> {
                    Empresa empresa = empresaRepository.findOne(empresaDTO.getIdEmpresa());
                    List<EmpresaModeloNegocio> empresaModeloNegocioList = empresaModeloNegocioRepository.findByEmpresa(empresa);

                    if (isNotEmpty(empresaModeloNegocioList)) {
                        empresaModeloNegocioList.forEach(empresaModeloNegocio -> {
                            ModeloNegocioDTO dtoInner = ModeloNegocioConverter.paraDTO(empresaModeloNegocio.getModeloNegocio());
                            modeloNegocioDTOSet.add(setExtraFields(dtoInner));
                        });
                    }
                });
                break;
        }

    }

    private ModeloNegocioDTO setExtraFields(ModeloNegocioDTO dto) {
        dto.setTipoCredito(tipoCreditoRepository.findOne(dto.getIdTipoCredito()).getDescricao());
        dto.setTransportadora(transportadoraRepository.findOne(dto.getIdTransportadora()).getRazaoSocial());
        return dto;
    }

    @Override
    public ModeloNegocio buscaModeloNegocioByEmpresaETipoCredito(Long idEmpresa, Long idTipoCredito) {
        List<ModeloNegocio> list = modeloNegocioRepository.findAllByEmpresaModeloNegocioCollection_empresa_idEmpresaAndIdTipoCredito(idEmpresa, idTipoCredito);
        if (isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ModeloNegocioDTO> listarPorIdGrupo(Long idGrupo) throws Exception {
        List<ModeloNegocioDTO> lstModeloNegocioDTO = new ArrayList<>();
        List<Long> lstIdEmpresa = new ArrayList<>();

        GrupoEconomico grupo = grupoRepository.findOne(idGrupo);

        if (grupo != null) {

            List<EmpresaDTO> lstEmpresaDTO = empresaService.listaEmpresasPorUsuarioLogado();
            lstEmpresaDTO.forEach(e -> {
                if (idGrupo.equals(e.getIdGrupoEconomico())) {
                    lstIdEmpresa.add(e.getIdEmpresa());
                }
            });

            if (lstIdEmpresa.isEmpty()) {
                grupo.getEmpresas().forEach(e -> {
                    lstIdEmpresa.add(e.getIdEmpresa());
                });
            }

            modeloNegocioRepository.findDistinctAllByEmpresaModeloNegocioCollection_empresa_idEmpresaIn(lstIdEmpresa).forEach(mn -> {
                ModeloNegocioDTO dto = ModeloNegocioConverter.paraDTO(mn);
                lstModeloNegocioDTO.add(dto);
            });

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"grupo"}));
        }
        return lstModeloNegocioDTO;
    }

}
