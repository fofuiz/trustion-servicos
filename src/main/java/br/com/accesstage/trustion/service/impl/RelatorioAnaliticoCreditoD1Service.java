package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.RelatorioAnaliticoCreditoD1Converter;
import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;
import br.com.accesstage.trustion.model.TipoCredito;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.repository.criteria.RelatorioAnaliticoCreditoD1Specification;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IModeloNegocioRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoD1Repository;
import br.com.accesstage.trustion.repository.interfaces.ITipoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IGrupoEconomicoService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoNumerarioService;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoD1Service;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.ValidarCNPJ;
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
public class RelatorioAnaliticoCreditoD1Service implements IRelatorioAnaliticoCreditoD1Service {

    @Autowired
    private IRelatorioAnaliticoCreditoD1Repository relatorioD1Repository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    @Autowired
    private ITipoCreditoRepository tipoCreditoRepository;

    @Autowired
    private IModeloNegocioRepository modeloRepository;

    @Autowired
    private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IGrupoEconomicoService grupoEconomicoService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IPeriodoResumoNumerarioService periodoResumoNumerarioService;

    @Autowired
    private IAtividadeRepository atividadeRepository;

    private static  BigDecimal defaultValorQuestionado = BigDecimal.ZERO;

    private final Converter<RelatorioAnaliticoCreditoD1, RelatorioAnaliticoCreditoD1DTO> converter = new Converter<RelatorioAnaliticoCreditoD1, RelatorioAnaliticoCreditoD1DTO>() {

        @Override
        public RelatorioAnaliticoCreditoD1DTO convert(RelatorioAnaliticoCreditoD1 entidade) {
            RelatorioAnaliticoCreditoD1DTO dto = RelatorioAnaliticoCreditoD1Converter.paraDTO(entidade);

            if (dto.getIdGrupoEconomico() != null) {
                GrupoEconomico grupo = grupoRepository.findOne(dto.getIdGrupoEconomico());
                if (grupo != null) {
                    dto.setGrupoEconomico(grupo.getNome());
                }
            }

            if (dto.getIdTransportadora() != null) {
                Transportadora transportadora = transportadoraRepository.findOne(dto.getIdTransportadora());
                if (transportadora != null) {
                    dto.setTransportadora(transportadora.getRazaoSocial());
                }
            }

            if (dto.getIdEmpresa() != null) {
                Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
                if (empresa != null) {
                    dto.setEmpresa(empresa.getRazaoSocial());
                    dto.setSiglaLoja(empresa.getSiglaLoja());
                }
            }

            dto.setResponsavel("");
            if (dto.getIdOcorrencia() != null) {
                List<Atividade> responsavel = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(dto.getIdOcorrencia());
                if (!responsavel.isEmpty()) {
                    dto.setResponsavel(responsavel.get(0).getResponsavel());
                }
            }

            if (dto.getValorCredito() != null) {
                if (dto.getValorColeta() != null) {
                    dto.setDiferencaValorQuestionado(dto.getValorCredito().subtract(dto.getValorColeta()));
                } else {
                    dto.setDiferencaValorQuestionado(dto.getValorCredito());
                }
            } else {
                dto.setDiferencaValorQuestionado(dto.getValorColeta() == null ? null : dto.getValorColeta().negate());
            }

            /*
            dto.setGtvLink(false);
            if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.ADMINISTRADOR.get()
                    || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_TRANSPORTADORA.get()
                    || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get()) {
                dto.setGtvLink(true);
            }
            */

            dto.setGtvLink(true);

            return dto;
        }
    };

    @Override
    public Page<RelatorioAnaliticoCreditoD1DTO> listarCriterios(RelatorioAnaliticoCreditoD1DTO relAnaliticoCredDTO, Pageable pageable) throws Exception {
        Page<RelatorioAnaliticoCreditoD1DTO> relAnaliticoCreditoD1DTO = null;

        TipoCredito tipoD1 = tipoCreditoRepository.findOne(CodigoTipoCreditoEnum.CREDITOD1.getId());

        //Valida o CNPJ
        if (isNotBlank(relAnaliticoCredDTO.getCnpj())) {
            ValidarCNPJ.validarCNPJ(relAnaliticoCredDTO.getCnpj());
        }

        List<Long> idsModelosOuter = new ArrayList<>();
        for (ModeloNegocio modelo : modeloRepository.findAllByidTipoCredito(tipoD1.getIdTipoCredito())) {
            idsModelosOuter.add(modelo.getIdModeloNegocio());
        }

        //valida se o cnpj existe para global
        if (isNotBlank(relAnaliticoCredDTO.getCnpj())) {
            List<Empresa> entidades = empresaRepository.findAllByCnpjAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(relAnaliticoCredDTO.getCnpj(), idsModelosOuter);
            if (entidades.isEmpty()) {
                throw new ResourceNotFoundException(Mensagem.get("msg.cnpj.nao.existe"));
            }
        }

        //valida se existe uma gtv global
        if (isNotBlank(relAnaliticoCredDTO.getGtv())) {
            if (!relatorioD1Repository.existsByGtv(relAnaliticoCredDTO.getGtv())) {
                throw new ResourceNotFoundException(Mensagem.get("msg.gtv.nao.existe"));
            }
        }

        //valida se existe a ocorrência global
        if (relAnaliticoCredDTO.getIdOcorrencia() != null && relAnaliticoCredDTO.getIdOcorrencia() > 0L) {
            if (!relatorioD1Repository.existsByIdOcorrencia(relAnaliticoCredDTO.getIdOcorrencia())) {
                throw new ResourceNotFoundException(Mensagem.get("msg.id.ocorrencia.nao.existe"));
            }
        }

        //PERFIL ADMINISTRADOR
        if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.ADMINISTRADOR.get()) {

            List<Long> idsModelos = new ArrayList<>(idsModelosOuter);
            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctBySiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(relAnaliticoCredDTO.getSiglaLoja(), idsModelos);
            } else {
                empresas = empresaRepository.findDistinctByEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(idsModelos);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCreditoD1> specs = RelatorioAnaliticoCreditoD1Specification.byCriterio(relAnaliticoCredDTO, empresas, null);
                relAnaliticoCreditoD1DTO = relatorioD1Repository.findAll(specs, pageable).map(converter);
            }

            //PERFIL TRANSPORTADORA
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get()) {

            List<Long> idsModelos = new ArrayList<>();
            for (ModeloNegocio modelo : modeloRepository.findAllByidTipoCredito(tipoD1.getIdTipoCredito())) {
                idsModelos.add(modelo.getIdModeloNegocio());
            }

            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();
            List<Long> lOutrasEmpresas = listaIdGrupoEconomicoPorUsuarioLogado(lGrupos);

            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndSiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, relAnaliticoCredDTO.getSiglaLoja(), idsModelos);
            } else {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, idsModelos);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCreditoD1> specs = RelatorioAnaliticoCreditoD1Specification.byCriterio(relAnaliticoCredDTO, empresas, lGrupos);
                relAnaliticoCreditoD1DTO = relatorioD1Repository.findAll(specs, pageable).map(converter);
            }

            //PERFIL MASTER CLIENTE OU BPO
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE.get()
                || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.BPO.get()) {

            List<Long> idsModelos = new ArrayList<>();
            for (ModeloNegocio modelo : modeloRepository.findAllByidTipoCredito(tipoD1.getIdTipoCredito())) {
                idsModelos.add(modelo.getIdModeloNegocio());
            }

            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();
            List<Long> lOutrasEmpresas = listaIdGrupoEconomicoPorUsuarioLogado(lGrupos);

            List<Empresa> empresas;
            if (isNotBlank(relAnaliticoCredDTO.getSiglaLoja())) {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndSiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, relAnaliticoCredDTO.getSiglaLoja(), idsModelos);
            } else {
                empresas = empresaRepository.findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(lOutrasEmpresas, idsModelos);
            }

            if (!empresas.isEmpty()) {
                Specification<RelatorioAnaliticoCreditoD1> specs = RelatorioAnaliticoCreditoD1Specification.byCriterio(relAnaliticoCredDTO, empresas, lGrupos);
                relAnaliticoCreditoD1DTO = relatorioD1Repository.findAll(specs, pageable).map(converter);
            }

            //PERFIL OPERADOR CLIENTE
        } else if (UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE.get()
                || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get()
        		|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get()) {

            List<Empresa> lEmpresas = listaEmpresasPorUsuarioLogado();
            List<GrupoEconomico> lGrupos = listaGrupoEconomicoPorUsuarioLogado();

            if (!lGrupos.isEmpty() && !lEmpresas.isEmpty()) {
                Specification<RelatorioAnaliticoCreditoD1> specs = RelatorioAnaliticoCreditoD1Specification.byCriterio(relAnaliticoCredDTO, lEmpresas, lGrupos);
                relAnaliticoCreditoD1DTO = relatorioD1Repository.findAll(specs, pageable).map(converter);
            }

        } else {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        return relAnaliticoCreditoD1DTO;
    }

    @Override
    public RelatorioAnaliticoCreditoD1DTO pesquisar(Long id) throws Exception {
        RelatorioAnaliticoCreditoD1DTO dto = null;
        RelatorioAnaliticoCreditoD1 entidade = relatorioD1Repository.findOne(id);
        if (entidade != null) {
            dto = RelatorioAnaliticoCreditoD1Converter.paraDTO(entidade);
            GrupoEconomico grupoEconomico = grupoRepository.findOne(dto.getIdGrupoEconomico());
            dto.setGrupoEconomico(grupoEconomico.getNome());
            Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
            dto.setEmpresa(empresa.getRazaoSocial());
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }
        return dto;
    }

    @Override
    public RelatorioAnaliticoCreditoD1DTO pesquisarPorIdConciliacao(Long idConciliacao) throws Exception {
        RelatorioAnaliticoCreditoD1DTO dto = null;
        RelatorioAnaliticoCreditoD1 entidade = relatorioD1Repository.findOneByIdConciliacao(idConciliacao);
        if (entidade != null) {
            dto = RelatorioAnaliticoCreditoD1Converter.paraDTO(entidade);
            GrupoEconomico grupoEconomico = grupoRepository.findOne(dto.getIdGrupoEconomico());
            dto.setGrupoEconomico(grupoEconomico.getNome());
            Empresa empresa = empresaRepository.findOne(dto.getIdEmpresa());
            dto.setEmpresa(empresa.getRazaoSocial());
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }
        return dto;
    }

    @Override
    public RelatorioAnaliticoCreditoD1DTO alterarStatusConciliacao(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO, StatusConciliacaoEnum statusConciliacaoEnum) {

        RelatorioAnaliticoCreditoD1DTO dto = null;

        if (!relatorioD1Repository.exists(relatorioAnaliticoCreditoD1DTO.getIdRelatorioAnalitico())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        if (relatorioAnaliticoCreditoD1DTO.getValorCredito() != null && null != relatorioAnaliticoCreditoD1DTO.getValorColeta()) {
            relatorioAnaliticoCreditoD1DTO.setValorQuestionado(relatorioAnaliticoCreditoD1DTO.getValorCredito()
                    .subtract(relatorioAnaliticoCreditoD1DTO.getValorConferido()));
        }

        switch (statusConciliacaoEnum) {
            case CONCILIADO:
                relatorioAnaliticoCreditoD1DTO.setStatusConciliacao(statusConciliacaoEnum.get());
                dto = RelatorioAnaliticoCreditoD1Converter.paraDTO(relatorioD1Repository.save(RelatorioAnaliticoCreditoD1Converter
                        .paraEntidade(relatorioAnaliticoCreditoD1DTO)));
                break;
            case NAO_CONCILIADO:
                relatorioAnaliticoCreditoD1DTO.setStatusConciliacao(statusConciliacaoEnum.get());
                relatorioAnaliticoCreditoD1DTO.setDataCredito(null);
                relatorioAnaliticoCreditoD1DTO.setValorCredito(null);
                relatorioAnaliticoCreditoD1DTO.setIdConciliacao(null);
                relatorioAnaliticoCreditoD1DTO.setValorQuestionado(defaultValorQuestionado);
                dto = RelatorioAnaliticoCreditoD1Converter.paraDTO(relatorioD1Repository.save(RelatorioAnaliticoCreditoD1Converter
                        .paraEntidade(relatorioAnaliticoCreditoD1DTO)));
                break;
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioAnaliticoTotalDTO calcularRelatorioTotal7Dias() throws Exception {
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.set(Calendar.DAY_OF_MONTH, dataInicial.get(Calendar.DAY_OF_MONTH) - 7);

        return calcularRelatorioTotalPeriodo(dataInicial.getTime(), Calendar.getInstance().getTime());
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodoUsuario() throws Exception {
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.set(Calendar.DAY_OF_MONTH, dataInicial.get(Calendar.DAY_OF_MONTH) - periodoResumoNumerarioService.pesquisar(UsuarioAutenticado.getIdUsuario()).getPeriodoCredito().intValue());

        return calcularRelatorioTotalPeriodo(dataInicial.getTime(), Calendar.getInstance().getTime());
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

        Stream<RelatorioAnaliticoCreditoD1> stream = relatorioD1Repository.findAllByIdEmpresaInAndDataConferenciaBetween(idsEmpresas, dataInicialMeiaNoite.getTime(), dataFinalOnze59Minutos.getTime());

        BigDecimal result = stream.filter(relatorioAnaliticoCredito -> relatorioAnaliticoCredito != null && relatorioAnaliticoCredito.getValorCredito() != null).map(RelatorioAnaliticoCreditoD1::getValorCredito).reduce(BigDecimal.ZERO, BigDecimal::add);

        totalDTO.setTotal(result);

        return totalDTO;
    }

    @Override
    public RelatorioAnaliticoCreditoD1DTO alterarStatusOcorrencia(OcorrenciaD1DTO dto) throws Exception {

        RelatorioAnaliticoCreditoD1 entidade = relatorioD1Repository.findOne(dto.getIdRelatorioAnalitico());
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setValorQuestionado(dto.getValorQuestionado());
        entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
        TipoStatusOcorrencia tipoStatusOcorrencia = tipoStatusOcorrenciaRepository.findOne(dto.getIdTipoStatusOcorrencia());
        entidade.setStatusOcorrencia(tipoStatusOcorrencia.getDescricao());

        RelatorioAnaliticoCreditoD1 entidadeCriado = relatorioD1Repository.save(entidade);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Relatorio + " " + UTF8.Analitico + " de " + UTF8.Creditos + " de Id ", entidadeCriado.getIdRelatorioAnalitico()}), dto.getIdOcorrencia());

        return RelatorioAnaliticoCreditoD1Converter.paraDTO(entidadeCriado);
    }

    @Override
    public RelatorioAnaliticoCreditoD1DTO atualizarStatusOcorrencia(OcorrenciaD1DTO dto) throws Exception {

        RelatorioAnaliticoCreditoD1 entidade = relatorioD1Repository.findOneByIdOcorrencia(dto.getIdOcorrencia());
        entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
        TipoStatusOcorrencia tipoStatusOcorrencia = tipoStatusOcorrenciaRepository.findOne(dto.getIdTipoStatusOcorrencia());
        entidade.setStatusOcorrencia(tipoStatusOcorrencia.getDescricao());

        RelatorioAnaliticoCreditoD1 entidadeCriado = relatorioD1Repository.save(entidade);


        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Relatorio + " " + UTF8.Analitico + " de " + UTF8.Creditos + " de Id ", entidadeCriado.getIdRelatorioAnalitico()}), dto.getIdOcorrencia());

        return RelatorioAnaliticoCreditoD1Converter.paraDTO(entidadeCriado);
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
            GrupoEconomico grupoAux = grupoRepository.findOne(g.getIdGrupoEconomico());
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
