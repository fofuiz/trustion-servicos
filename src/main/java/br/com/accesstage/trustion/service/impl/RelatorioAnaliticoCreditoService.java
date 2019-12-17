package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.service.interfaces.IGrupoEconomicoService;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IPeriodoResumoNumerarioRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.TipoCredito;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.PeriodoResumoNumerario;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoTotalDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.util.ValidarCNPJ;
import br.com.accesstage.trustion.converter.EmpresaConverter;
import br.com.accesstage.trustion.converter.PeriodoResumoNumerarioConverter;
import br.com.accesstage.trustion.converter.RelatorioAnaliticoCreditoConverter;
import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import br.com.accesstage.trustion.dto.PeriodoResumoNumerarioDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.repository.criteria.RelatorioAnaliticoCreditoSpecification;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class RelatorioAnaliticoCreditoService implements IRelatorioAnaliticoCreditoService {

    @Autowired
    private IRelatorioAnaliticoCreditoRepository relAnaliticoCredRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IModeloNegocioService modeloNegocioService;

    @Autowired
    private ITipoCreditoRepository tipoCreditoRepository;

    @Autowired
    private IGrupoEconomicoService grupoEconomicoService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IAtividadeRepository atividadeRepository;
    
    @Autowired
    private IPeriodoResumoNumerarioRepository periodoResumoNumerarioRepository;

    private static  BigDecimal defaultValorQuestionado = BigDecimal.ZERO;

    private Converter<RelatorioAnaliticoCredito, RelatorioAnaliticoCreditoDTO> converter = new Converter<RelatorioAnaliticoCredito, RelatorioAnaliticoCreditoDTO>() {

        @Override
        public RelatorioAnaliticoCreditoDTO convert(RelatorioAnaliticoCredito entidade) {

            RelatorioAnaliticoCreditoDTO relAnaliticoCredDTOAux = RelatorioAnaliticoCreditoConverter.paraDTO(entidade);

            GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(relAnaliticoCredDTOAux.getIdGrupoEconomico());
            relAnaliticoCredDTOAux.setGrupoEconomico(grupoEconomico.getNome());

            Transportadora transportadora = transportadoraRepository.findOne(relAnaliticoCredDTOAux.getIdTransportadora());
            relAnaliticoCredDTOAux.setTransportadora(transportadora.getRazaoSocial());

            Empresa empresa = empresaRepository.findOne(relAnaliticoCredDTOAux.getIdEmpresa());
            relAnaliticoCredDTOAux.setEmpresa(empresa.getRazaoSocial());
            relAnaliticoCredDTOAux.setSiglaLoja(empresa.getSiglaLoja());

            if (null != relAnaliticoCredDTOAux.getIdOcorrencia()) {
                List<Atividade> responsavel = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(relAnaliticoCredDTOAux.getIdOcorrencia());
                if (responsavel.isEmpty()) {
                    relAnaliticoCredDTOAux.setResponsavel("");
                } else {
                    relAnaliticoCredDTOAux.setResponsavel(responsavel.get(0).getResponsavel());
                }
            }

            if (relAnaliticoCredDTOAux.getValorCredito() != null) {

                if (relAnaliticoCredDTOAux.getValorTotal() != null) {
                    relAnaliticoCredDTOAux.setDiferencaValorQuestionado(relAnaliticoCredDTOAux.getValorCredito().subtract(relAnaliticoCredDTOAux.getValorTotal()));
                } else {
                    relAnaliticoCredDTOAux.setDiferencaValorQuestionado(relAnaliticoCredDTOAux.getValorCredito());
                }

            } else {
                relAnaliticoCredDTOAux.setDiferencaValorQuestionado(relAnaliticoCredDTOAux.getValorTotal() == null ? null : relAnaliticoCredDTOAux.getValorTotal().negate());
            }

            //Todos os perfils tem acesso ao modal de Detalhe Fechamento Credito Diario
            relAnaliticoCredDTOAux.setIsValorRegistradoLink(true);

            return relAnaliticoCredDTOAux;
        }
    };

    @Override
    public Page<RelatorioAnaliticoCreditoDTO> listarCriterios(RelatorioAnaliticoCreditoDTO relAnaliticoCredDTO, Pageable pageable) throws Exception {

        //Valida o CNPJ
        if (relAnaliticoCredDTO.getCnpj() != null && !relAnaliticoCredDTO.getCnpj().isEmpty()) {
            ValidarCNPJ.validarCNPJ(relAnaliticoCredDTO.getCnpj());
        }

        Page<RelatorioAnaliticoCreditoDTO> listaRelAnaliticoCredDTO = null;

        TipoCredito tipoCredito = tipoCreditoRepository.findOne(CodigoTipoCreditoEnum.CREDITOD0.getId());
        List<Long> modelosD0 = modeloNegocioService.listarIdsModeloNegocioPorTipoCredito(tipoCredito);

        //valida se o cnpj existe para global
        if (relAnaliticoCredDTO.getCnpj() != null && !relAnaliticoCredDTO.getCnpj().isEmpty()) {
            if (!empresaRepository.existsByCnpj(relAnaliticoCredDTO.getCnpj())) {
                throw new ResourceNotFoundException(Mensagem.get("msg.cnpj.nao.existe"));
            }
        }

        //valida se existe a ocorrência global
        if (relAnaliticoCredDTO.getIdOcorrencia() != null && relAnaliticoCredDTO.getIdOcorrencia() > 0) {
            if (!relAnaliticoCredRepository.existsByIdOcorrencia(relAnaliticoCredDTO.getIdOcorrencia())) {
                throw new ResourceNotFoundException(Mensagem.get("msg.id.ocorrencia.nao.existe"));
            }
        }

        if (relAnaliticoCredDTO.getOutrasEmpresas() != null && !relAnaliticoCredDTO.getOutrasEmpresas().isEmpty() || relAnaliticoCredDTO.getGrupo() != null && !relAnaliticoCredDTO.getGrupo().isEmpty()) {
            if (relAnaliticoCredDTO.getEmpresas() == null || relAnaliticoCredDTO.getEmpresas().isEmpty()) {

                List<Long> listaIds = new ArrayList<>();
                if(relAnaliticoCredDTO.getOutrasEmpresas() != null && !relAnaliticoCredDTO.getOutrasEmpresas().isEmpty()) {
                    for (GrupoEconomicoDTO grupoDTO : relAnaliticoCredDTO.getOutrasEmpresas()) {
                        listaIds.add(grupoDTO.getIdGrupoEconomico());
                    }
                }
                if(relAnaliticoCredDTO.getGrupo() != null && !relAnaliticoCredDTO.getGrupo().isEmpty()) {
                    for (GrupoEconomicoDTO grupoDTO : relAnaliticoCredDTO.getGrupo()) {
                        listaIds.add(grupoDTO.getIdGrupoEconomico());
                    }
                }

                if (relAnaliticoCredDTO.getEmpresas() == null) {
                    relAnaliticoCredDTO.setEmpresas(new ArrayList<>());
                }

                for (Empresa empresa : empresaRepository.findAllByIdGrupoEconomicoIn(listaIds)) {
                    relAnaliticoCredDTO.getEmpresas().add(EmpresaConverter.paraDTO(empresa));
                }
            }
        }

        //PERFIL ADMINITRADOR
        if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.ADMINISTRADOR.get()) {

            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctBySiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(relAnaliticoCredDTO.getSiglaLoja(), modelosD0);
            } else {
                empresas = empresaRepository.findDistinctByEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(modelosD0);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCredito> specs = RelatorioAnaliticoCreditoSpecification.byCriterio(relAnaliticoCredDTO, empresas, null);
                listaRelAnaliticoCredDTO = relAnaliticoCredRepository.findAll(specs, pageable).map(converter);
            }

            //PERFIL TRANSPORTADORA
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get()) {

            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();
            List<Long> lOutrasEmpresas = listaIdGrupoEconomicoPorUsuarioLogado(lGrupos);

            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndSiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, relAnaliticoCredDTO.getSiglaLoja(), modelosD0);
            } else {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, modelosD0);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCredito> specs = RelatorioAnaliticoCreditoSpecification.byCriterio(relAnaliticoCredDTO, empresas, lGrupos);
                listaRelAnaliticoCredDTO = relAnaliticoCredRepository.findAll(specs, pageable).map(converter);
            }

            //PERFIL MASTER CLIENTE OU BPO
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE.get()
                || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.BPO.get()) {

            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();
            List<Long> lOutrasEmpresas = listaIdGrupoEconomicoPorUsuarioLogado(lGrupos);

            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndSiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, relAnaliticoCredDTO.getSiglaLoja(), modelosD0);
            } else {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, modelosD0);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCredito> specs = RelatorioAnaliticoCreditoSpecification.byCriterio(relAnaliticoCredDTO, empresas, lGrupos);
                listaRelAnaliticoCredDTO = relAnaliticoCredRepository.findAll(specs, pageable).map(converter);
            }

            //PERFIL OPERADOR CLIENTE
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE.get()
                || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get()) {

            List<Empresa> lEmpresas = listaEmpresasPorUsuarioLogado();
            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();

            if (!lGrupos.isEmpty() && !lEmpresas.isEmpty()) {
                Specification<RelatorioAnaliticoCredito> specs = RelatorioAnaliticoCreditoSpecification.byCriterio(relAnaliticoCredDTO, lEmpresas, lGrupos);
                listaRelAnaliticoCredDTO = relAnaliticoCredRepository.findAll(specs, pageable).map(converter);
            }

        } else {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        return listaRelAnaliticoCredDTO;
    }

    @Override
    public RelatorioAnaliticoCreditoDTO pesquisar(Long id) throws Exception {
        RelatorioAnaliticoCreditoDTO dto = null;
        RelatorioAnaliticoCredito entidade = relAnaliticoCredRepository.findOne(id);
        if (entidade != null) {
            dto = RelatorioAnaliticoCreditoConverter.paraDTO(entidade);
            GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(dto.getIdGrupoEconomico());
            dto.setGrupoEconomico(grupoEconomico.getNome());
            Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
            dto.setEmpresa(empresa.getRazaoSocial());
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }
        return dto;
    }

    @Override
    public RelatorioAnaliticoCreditoDTO pesquisarPorIdConciliacao(Long idConciliacao) throws Exception {
        RelatorioAnaliticoCreditoDTO dto = null;
        RelatorioAnaliticoCredito relatorioAnaliticoCredito = relAnaliticoCredRepository.findOneByIdConciliacao(idConciliacao);
        if (relatorioAnaliticoCredito == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        dto = RelatorioAnaliticoCreditoConverter.paraDTO(relatorioAnaliticoCredito);
        GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(dto.getIdGrupoEconomico());
        dto.setGrupoEconomico(grupoEconomico.getNome());
        Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
        dto.setEmpresa(empresa.getRazaoSocial());

        return dto;
    }

    @Override
    public RelatorioAnaliticoCreditoDTO pesquisarPorIdOcorrencia(Long idOcorrencia) throws Exception {
        RelatorioAnaliticoCreditoDTO dto = null;
        RelatorioAnaliticoCredito relatorioAnaliticoCredito = relAnaliticoCredRepository.findOneByIdOcorrencia(idOcorrencia);
        if (relatorioAnaliticoCredito == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        dto = RelatorioAnaliticoCreditoConverter.paraDTO(relatorioAnaliticoCredito);
        GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(dto.getIdGrupoEconomico());
        dto.setGrupoEconomico(grupoEconomico.getNome());
        Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
        dto.setEmpresa(empresa.getRazaoSocial());

        return dto;
    }

    @Override
    public RelatorioAnaliticoCreditoDTO alterarStatusConciliacao(RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO, StatusConciliacaoEnum statusConciliacaoEnum) {
        RelatorioAnaliticoCreditoDTO dto = null;

        if (!relAnaliticoCredRepository.exists(relatorioAnaliticoCreditoDTO.getIdRelatorioAnalitico())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        if (null != relatorioAnaliticoCreditoDTO.getValorCredito() && null != relatorioAnaliticoCreditoDTO.getValorTotal()) {
            relatorioAnaliticoCreditoDTO.setValorQuestionado(relatorioAnaliticoCreditoDTO.getValorCredito()
                    .subtract(relatorioAnaliticoCreditoDTO.getValorTotal()));

        }

        switch (statusConciliacaoEnum) {
            case CONCILIADO:
                relatorioAnaliticoCreditoDTO.setStatusConciliacao(statusConciliacaoEnum.get());
                dto = RelatorioAnaliticoCreditoConverter.paraDTO(relAnaliticoCredRepository.save(RelatorioAnaliticoCreditoConverter.paraEntidade(relatorioAnaliticoCreditoDTO)));
                break;
            case NAO_CONCILIADO:
                relatorioAnaliticoCreditoDTO.setStatusConciliacao(statusConciliacaoEnum.get());
                relatorioAnaliticoCreditoDTO.setDataCredito(null);
                relatorioAnaliticoCreditoDTO.setValorCredito(null);
                relatorioAnaliticoCreditoDTO.setIdConciliacao(null);
                relatorioAnaliticoCreditoDTO.setValorQuestionado(defaultValorQuestionado);
                dto = RelatorioAnaliticoCreditoConverter.paraDTO(relAnaliticoCredRepository.save(RelatorioAnaliticoCreditoConverter.paraEntidade(relatorioAnaliticoCreditoDTO)));
                break;
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioAnaliticoTotalDTO calcularRelatorioTotal7Dias() throws Exception {
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.set(Calendar.DAY_OF_MONTH, dataInicial.get(Calendar.DAY_OF_MONTH) - 7);
        dataInicial.set(Calendar.YEAR, dataInicial.get(Calendar.YEAR) - 1);
        return calcularRelatorioTotalPeriodo(dataInicial.getTime(), Calendar.getInstance().getTime());
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodoUsuario() throws Exception {
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.set(Calendar.DAY_OF_MONTH, dataInicial.get(Calendar.DAY_OF_MONTH) - pesquisarPeriodo(UsuarioAutenticado.getIdUsuario()).getPeriodoCredito().intValue());
        //dataInicial.set(Calendar.YEAR, dataInicial.get(Calendar.YEAR) - 1);
        return calcularRelatorioTotalPeriodo(dataInicial.getTime(), Calendar.getInstance().getTime());
    }
    
    private PeriodoResumoNumerarioDTO pesquisarPeriodo(Long idUsuario) {

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(!periodoResumoNumerarioRepository.existsByIdUsuario(idUsuario))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"resumo"}));

        PeriodoResumoNumerario entidadePesquisado = periodoResumoNumerarioRepository.findOneByIdUsuario(idUsuario);

        return PeriodoResumoNumerarioConverter.paraDTO(entidadePesquisado);
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodo(Date dataInicial, Date dataFinal) throws Exception {

        RelatorioAnaliticoTotalDTO totalDTO = new RelatorioAnaliticoTotalDTO();

        if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.ADMINISTRADOR.get()) {
            totalDTO.setTotal(BigDecimal.ZERO);
            return totalDTO;
        }

        List<GrupoEconomicoDTO> GrupoOutros = grupoEconomicoService.listarGruposSpecsOutros(new GrupoEconomicoDTO());

        List<EmpresaDTO> empresaOutros = empresaService.listarCriterios(GrupoOutros);

        List<Long> idsEmpresas = new ArrayList<>();

        empresaOutros.forEach(empresaDTO -> idsEmpresas.add(empresaDTO.getIdEmpresa()));

        Calendar dataInicialMeiaNoite = Calendar.getInstance();
        dataInicialMeiaNoite.setTime(dataInicial);
        dataInicialMeiaNoite.set(Calendar.HOUR, 0);
        dataInicialMeiaNoite.set(Calendar.MINUTE, 0);
        dataInicialMeiaNoite.set(Calendar.SECOND, 0);

        Calendar dataFinalOnze59Minutos = Calendar.getInstance();
        dataFinalOnze59Minutos.setTime(dataFinal);
        dataInicialMeiaNoite.set(Calendar.HOUR, 23);
        dataInicialMeiaNoite.set(Calendar.MINUTE, 59);
        dataInicialMeiaNoite.set(Calendar.SECOND, 59);

        Stream<RelatorioAnaliticoCredito> stream = relAnaliticoCredRepository.findAllByIdEmpresaInAndAndDataCorteBetween(idsEmpresas, dataInicialMeiaNoite.getTime(), dataFinalOnze59Minutos.getTime());

        BigDecimal result = stream.filter(relatorioAnaliticoCredito -> relatorioAnaliticoCredito != null && relatorioAnaliticoCredito.getValorCredito() != null).map(RelatorioAnaliticoCredito::getValorCredito).reduce(BigDecimal.ZERO, BigDecimal::add);

        totalDTO.setTotal(result);
        return totalDTO;
    }

    /*
     * Metodo responsavel por retornar lista de <GrupoEcomomicos> do usuario logado
     *
     * return List<GrupoEconomico>
     */
    private List<GrupoEconomico> listaGrupoEconomicoPorUsuarioLogado() throws Exception {

        List<GrupoEconomico> lGrupos = new ArrayList<>();
        List<EmpresaDTO> listEmpresaDTO = empresaService.listaEmpresasPorUsuarioLogado();
        listEmpresaDTO.forEach(e -> {
            GrupoEconomico grupo = new GrupoEconomico();
            grupo.setIdGrupoEconomico(e.getIdGrupoEconomico());
            lGrupos.add(grupo);
        });

        return lGrupos;
    }

    /*
     * Metodo responsavel por retornar lista de id grupo ecomomicos das 
     * empresas associadas ao grupos do usuario logado
     *
     * return List<Long>
     */
    private List<Long> listaIdGrupoEconomicoPorUsuarioLogado(List<GrupoEconomico> lGrupo) throws Exception {

        List<Long> lIdGrupoEconomico = new ArrayList<>();
        lGrupo.forEach(g -> {
            GrupoEconomico grupoAux = grupoEconomicoRepository.findOne(g.getIdGrupoEconomico());
            grupoAux.getEmpresas().forEach(empresasAssoc -> {
                lIdGrupoEconomico.add(empresasAssoc.getIdGrupoEconomico());
            });

        });

        return lIdGrupoEconomico;

    }

    /*
     * Metodo responsavel por retornar lista de <Empresa> do usuario logado
     *
     * return List<Empresa>
     */
    private List<Empresa> listaEmpresasPorUsuarioLogado() throws Exception {

        List<Empresa> lEmpresas = new ArrayList<>();
        List<EmpresaDTO> listEmpresaDTO = empresaService.listaEmpresasPorUsuarioLogado();
        listEmpresaDTO.forEach(e -> {
            Empresa empresa = new Empresa();
            empresa.setIdEmpresa(e.getIdEmpresa());
            lEmpresas.add(empresa);
        });

        return lEmpresas;
    }

}
