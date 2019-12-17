package br.com.accesstage.trustion.service.impl;

import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.*;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.OcorrenciaConverter;
import br.com.accesstage.trustion.converter.RelatorioOcorrenciaD0Converter;
import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.dto.TransportadoraDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoStatusOcorrencia;
import br.com.accesstage.trustion.enums.PerfilEnum;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;
import br.com.accesstage.trustion.model.RelatorioOcorrenciaD0;
import br.com.accesstage.trustion.model.SlaAtendimento;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.RelatorioOcorrenciaD0Specification;
import br.com.accesstage.trustion.repository.impl.RelatorioOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IModeloNegocioRepository;
import br.com.accesstage.trustion.repository.interfaces.IOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoD1Repository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.ISlaAtendimentoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoQuestionamentoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaService;
import br.com.accesstage.trustion.service.interfaces.ITransportadoraService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;

@Service
public class OcorrenciaService implements IOcorrenciaService {
    
        @Log
    private static Logger LOGGER;

    @Autowired
    private IRelatorioAnaliticoCreditoRepository relatorioRepository;

    @Autowired
    private IRelatorioAnaliticoCreditoD1Repository relatorioD1Repository;
    
    
    @Autowired
    private IOcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private ITipoQuestionamentoRepository tipoQuestionamentoRepository;

    @Autowired
    private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IModeloNegocioRepository modeloRepository;

    @Autowired
    private IAtividadeRepository atividadeRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IAtividadeService atividadeService;

    @Autowired
    private IRelatorioOcorrenciaRepository relatorioOcorrenciaRepository;

    @Autowired
    private RelatorioOcorrenciaRepository relatorioOcRepository;

    @Autowired
    private ISlaAtendimentoRepository slaAtendimentoRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;
    
    @Autowired
    private ITransportadoraService transportadoraService;

    @Autowired
    private IModeloNegocioService modeloNegocioService;

    public static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final String MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA = "msg.auditoria.atividade.na.ocorrencia";

    public Converter<RelatorioOcorrenciaDTO, RelatorioOcorrenciaDTO> converter = new Converter<RelatorioOcorrenciaDTO, RelatorioOcorrenciaDTO>() {

        @Override
        public RelatorioOcorrenciaDTO convert(RelatorioOcorrenciaDTO dto) {

            List<Atividade> atividade = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(dto.getIdOcorrencia());

            dto.setDiasPendentes(!atividade.isEmpty() ? ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(atividade.get(0).getDataHorario()), LocalDate.now())
                    : ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia()), LocalDate.now()));
            dto.setDiasEmAberto(ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataCriacao()), LocalDate.now()));

            TipoStatusOcorrencia statusOcorrencia = new TipoStatusOcorrencia();
            statusOcorrencia.setIdTipoStatusOcorrencia(0L);
            if (null != dto.getStatusOcorrencia()) {
                statusOcorrencia = tipoStatusOcorrenciaRepository.findByDescricao(dto.getStatusOcorrencia());
            }

            if (null != dto.getId_sla_atendimento()) {

                SlaAtendimento sla = slaAtendimentoRepository.findOne(dto.getId_sla_atendimento());

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 1) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseSolicitada());

                    if (sla.isAnaliseSolDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }
                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));

                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }

                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 2) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseAndamento());

                    if (sla.isAnaliseAndDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }

                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }

                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 3) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseAguarde());

                    if (sla.isAnaliseAguarDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }
                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));

                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }
                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 4) {
                    dto.setDiasEmAberto(ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataCriacao()), convertToLocalDateViaSqlDate(atividade.get(0).getDataHorario())));
                    dto.setDiasPendentes(null);
                }

            }

            return dto;
        }

    };

    @Override
    public OcorrenciaDTO criar(OcorrenciaDTO dto) throws Exception {

        OcorrenciaDTO dtoCriado = null;
        Ocorrencia entidade = OcorrenciaConverter.paraEntidade(dto);
        entidade.setIdTipoStatusOcorrencia(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
        
        entidade.setDataStatusOcorrencia(Calendar.getInstance().getTime());
        entidade.setDataCriacao(Calendar.getInstance().getTime());
        entidade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOne(dto.getIdRelatorioAnalitico());
        entidade.setIdTransportadora(relatorioEntidade.getIdTransportadora());
        entidade.setIsOcorrenciaD1(false);
        

        Ocorrencia entidadeCriado = ocorrenciaRepository.save(entidade);

        if (entidadeCriado != null) {
            dtoCriado = OcorrenciaConverter.paraDTO(entidadeCriado);
            
            relatorioEntidade.setIdOcorrencia(entidadeCriado.getIdOcorrencia());
            relatorioEntidade.setDataStatusOcorrencia(entidadeCriado.getDataStatusOcorrencia());
            TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(dtoCriado.getIdTipoStatusOcorrencia());
            relatorioEntidade.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioEntidade = relatorioRepository.save(relatorioEntidade);
            dtoCriado.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
            dtoCriado.setDataCorte(relatorioEntidade.getDataCorte());
            dtoCriado.setValorRegistradoCofre(relatorioEntidade.getValorTotal());
            dtoCriado.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dtoCriado.getIdTipoQuestionamento()).getDescricao());
            dtoCriado.setTipoStatusOcorrencia(statusOcorrencia.getDescricao());
            dtoCriado.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
            Empresa empresa = empresaRepository.findOne(relatorioEntidade.getIdEmpresa());
            dtoCriado.setEmpresa(empresa.getRazaoSocial());
            if (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get())) {
                dtoCriado.setIsValorRegistradoCofreLink(true);
            }

           auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{UTF8.Ocorrencia + " de Id ", dtoCriado.getIdOcorrencia()}), dtoCriado.getIdOcorrencia());

            notificacaoService.enviarNotificacaoAnaliseDivergencia(entidadeCriado.getIdOcorrencia(), relatorioEntidade.getIdEmpresa());
            AtividadeDTO dtoAtividade = new AtividadeDTO();
            dtoAtividade.setAtividade(dto.getObservacao());
            dtoAtividade.setIdOcorrencia(dtoCriado.getIdOcorrencia());
            dtoAtividade.setIdTipoOcorrencia(dtoCriado.getIdTipoStatusOcorrencia());
            dtoAtividade.setIdEmpresa(relatorioEntidade.getIdEmpresa());
            dtoAtividade.setStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dtoCriado.getIdTipoStatusOcorrencia()).getDescricao());
            
            if(relatorioEntidade.getIdTransportadora() != null) {
                Transportadora t = this.transportadoraRepository.findOne(relatorioEntidade.getIdTransportadora());
                dtoAtividade.setResponsavel(t.getRazaoSocial());
            }
            
            atividadeService.criar(dtoAtividade);
        }

        return dtoCriado;
    }

    @Override
    public OcorrenciaDTO alterar(OcorrenciaDTO dto) throws Exception {

        OcorrenciaDTO dtoAlterado = null;
        Ocorrencia ocorrenciaPesquisa = ocorrenciaRepository.findOne(dto.getIdOcorrencia());

        if (ocorrenciaPesquisa != null) {
            Ocorrencia ocorrencia = OcorrenciaConverter.paraEntidade(dto);
            ocorrencia.setDataAlteracao(Calendar.getInstance().getTime());
            ocorrencia.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            dtoAlterado = OcorrenciaConverter.paraDTO(ocorrenciaRepository.save(ocorrencia));
            if (dtoAlterado != null) {
                RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOneByIdOcorrencia(dtoAlterado.getIdOcorrencia());
                dtoAlterado.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
                dtoAlterado.setDataCorte(relatorioEntidade.getDataCorte());
                dtoAlterado.setValorRegistradoCofre(relatorioEntidade.getValorTotal());
                dtoAlterado.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
                dtoAlterado.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dtoAlterado.getIdTipoQuestionamento()).getDescricao());
                dtoAlterado.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dtoAlterado.getIdTipoStatusOcorrencia()).getDescricao());
                Empresa empresa = empresaRepository.findOne(relatorioEntidade.getIdEmpresa());
                dtoAlterado.setEmpresa(empresa.getRazaoSocial());
                if (!ocorrenciaPesquisa.isIsOcorrenciaD1() && 
                        (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) 
                        || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                        || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get()))) {
                    dtoAlterado.setIsValorRegistradoCofreLink(true);
                }


                auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Ocorrencia + " de Id ", dtoAlterado.getIdOcorrencia()}), dtoAlterado.getIdOcorrencia());
            }
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.Ocorrencia}));
        }
        return dtoAlterado;
    }
    
    
    private OcorrenciaDTO setRelatorioAnaliticoCreditoDN(OcorrenciaDTO dto, Ocorrencia ocorrencia) {
        RelatorioAnaliticoCreditoD1 relatorio = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
        
        dto.setIdRelatorioAnalitico(relatorio.getIdRelatorioAnalitico());
        dto.setDataConferencia(relatorio.getDataColeta());
        dto.setValorConferido(relatorio.getValorColeta());
        dto.setValorCreditadoConta(relatorio.getValorCredito() == null ? ZERO : relatorio.getValorCredito());
        dto.setStatusConciliacao(relatorio.getStatusConciliacao());
        dto.setValorDeclarado(relatorio.getValorColeta());
        dto.setValorColeta(relatorio.getValorColeta());
        
        dto.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dto.getIdTipoQuestionamento()).getDescricao());
        dto.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dto.getIdTipoStatusOcorrencia()).getDescricao());
        dto.setGrupoEconomico(grupoRepository.findOne(relatorio.getIdGrupoEconomico()).getNome());
        dto.setDataCredito(relatorio.getDataCredito());
        dto.setValorQuestionado(relatorio.getValorQuestionado());
        dto.setIdEmpresa(relatorio.getIdEmpresa());
        dto.setValorQuestionado(ocorrencia.getValorQuestionado());

        dto.setIdGrupoEconomico(relatorio.getIdGrupoEconomico());

        Empresa empresa = empresaRepository.findOne(relatorio.getIdEmpresa());
        dto.setEmpresa(empresa.getRazaoSocial());

        Transportadora transportadora = transportadoraRepository.findOne(relatorio.getIdTransportadora());
        dto.setNmeTransportadora(transportadora.getRazaoSocial());
        
        if(dto.getNmeTransportadora() == null && relatorio.getIdTransportadora() != null) {
            dto.setNmeTransportadora(this.transportadoraRepository.findOne(relatorio.getIdTransportadora()).getRazaoSocial());
        }        
        
        if(relatorio.getIdModeloNegocio() != null) {
            ModeloNegocio mn = this.modeloRepository.findOne(relatorio.getIdModeloNegocio());
            dto.setModeloNegocio(mn.getNome());
        }
        
        
        return dto;
    }    
    
    private OcorrenciaDTO setRelatorioAnaliticoCreditoD0(OcorrenciaDTO dto, Ocorrencia ocorrencia) {
        RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
        
        dto.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
        dto.setDataCorte(relatorioEntidade.getDataCorte());
        dto.setValorRegistradoCofre(relatorioEntidade.getValorTotal() == null ? ZERO : relatorioEntidade.getValorTotal());
        dto.setValorCreditadoConta(relatorioEntidade.getValorCredito() == null ? ZERO : relatorioEntidade.getValorCredito());
        dto.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
        dto.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dto.getIdTipoQuestionamento()).getDescricao());
        dto.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dto.getIdTipoStatusOcorrencia()).getDescricao());
        dto.setGrupoEconomico(grupoRepository.findOne(relatorioEntidade.getIdGrupoEconomico()).getNome());
        dto.setDataCredito(relatorioEntidade.getDataCredito());
        dto.setValorQuestionado(relatorioEntidade.getValorQuestionado());
        dto.setIdEmpresa(relatorioEntidade.getIdEmpresa());
        dto.setValorQuestionado(ocorrencia.getValorQuestionado());

        dto.setIdGrupoEconomico(relatorioEntidade.getIdGrupoEconomico());

        Empresa empresa = empresaRepository.findOne(relatorioEntidade.getIdEmpresa());
        dto.setEmpresa(empresa.getRazaoSocial());
        
        if(dto.getNmeTransportadora() == null && relatorioEntidade.getIdTransportadora() != null) {
            dto.setNmeTransportadora(this.transportadoraRepository.findOne(relatorioEntidade.getIdTransportadora()).getRazaoSocial());
        }
        
        if(relatorioEntidade.getIdModeloNegocio() != null) {
            ModeloNegocio mn = this.modeloRepository.findOne(relatorioEntidade.getIdModeloNegocio());
            dto.setModeloNegocio(mn.getNome());
        }
        
        return dto;
    }

    @Override
    public OcorrenciaDTO pesquisar(Long idOcorrencia) throws Exception {

        OcorrenciaDTO dto = null;
        Ocorrencia entidade = ocorrenciaRepository.findOne(idOcorrencia);

        if (entidade != null) {
            dto = OcorrenciaConverter.paraDTO(entidade);

            if(entidade.isIsOcorrenciaD1()) {
                dto = setRelatorioAnaliticoCreditoDN(dto, entidade);
            } else {
                dto = setRelatorioAnaliticoCreditoD0(dto, entidade);
            }

            if (dto.getNmeTransportadora() == null && entidade.getIdTransportadora() != null) {
                Transportadora transportadora = transportadoraRepository.findOne(entidade.getIdTransportadora());
                if (transportadora != null) {
                    dto.setNmeTransportadora(transportadora.getRazaoSocial());
                }
            }

            if (!entidade.isIsOcorrenciaD1() &&
                    (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) 
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get()))) {

                dto.setIsValorRegistradoCofreLink(true);
            }

            if (dto.getIdTipoStatusOcorrencia() == 4L) {

                String nomeArquivo = UTF8.Ocorrencia + "-" + dto.getIdOcorrencia() + "-";

                Calendar dataStatusConcluido = Calendar.getInstance();
                dataStatusConcluido.setTime(dto.getDataStatusOcorrencia());

                String dia = String.format("%02d", dataStatusConcluido.get(Calendar.DAY_OF_MONTH));
                String mes = String.format("%02d", dataStatusConcluido.get(Calendar.MONTH) + 1);
                String ano = String.format("%04d", dataStatusConcluido.get(Calendar.YEAR));
                String segundo = String.format("%02d", dataStatusConcluido.get(Calendar.SECOND));
                String minuto = String.format("%02d", dataStatusConcluido.get(Calendar.MINUTE));
                String hora = String.format("%02d", dataStatusConcluido.get(Calendar.HOUR_OF_DAY));

                nomeArquivo = nomeArquivo + ano + mes + dia + hora + minuto + segundo + ".pdf";

                dto.setArquivoResumoOcorrencia(nomeArquivo);
                dto.setExibirArqResOcorrencia(true);
                dto.setConcluido(true);
            }
        }

        return dto;
    }

    @Override
    public List<OcorrenciaDTO> listarTodos() throws Exception {
        List<OcorrenciaDTO> dtos = new ArrayList<>();
        for (Ocorrencia entidade : ocorrenciaRepository.findAllByIsOcorrenciaD1(false)) {
            OcorrenciaDTO dtoInner = OcorrenciaConverter.paraDTO(entidade);
            RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOneByIdOcorrencia(dtoInner.getIdOcorrencia());
            dtoInner.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
            dtoInner.setDataCorte(relatorioEntidade.getDataCorte());
            dtoInner.setValorRegistradoCofre(relatorioEntidade.getValorTotal() == null ? ZERO : relatorioEntidade.getValorTotal());
            dtoInner.setValorCreditadoConta(relatorioEntidade.getValorCredito() == null ? ZERO : relatorioEntidade.getValorCredito());
            dtoInner.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
            dtoInner.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dtoInner.getIdTipoQuestionamento()).getDescricao());
            dtoInner.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dtoInner.getIdTipoStatusOcorrencia()).getDescricao());
            if (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get())) {
                dtoInner.setIsValorRegistradoCofreLink(true);
            }
            dtos.add(dtoInner);
        }
        return dtos;
    }
    
    

    @Override
    public List<OcorrenciaDTO> listarParaMescla(Long idOcorrencia) throws Exception {

        OcorrenciaDTO ocorrenciaDTO = this.pesquisar(idOcorrencia);
        List<OcorrenciaDTO> dtos = new ArrayList<>();
        Long empresaOcorrencia = null;
        Long grupoEconomicoOcorrencia = null;
        if(ocorrenciaDTO.getIsOcorrenciaD1()) {
            RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(idOcorrencia);
            if(relD1 != null) {
                empresaOcorrencia = relD1.getIdEmpresa();
                grupoEconomicoOcorrencia = relD1.getIdGrupoEconomico();
            }
        } else {
            RelatorioAnaliticoCredito relD0 = relatorioRepository.findOneByIdOcorrencia(idOcorrencia);
            if(relD0 != null) {
                empresaOcorrencia = relD0.getIdEmpresa();
                grupoEconomicoOcorrencia = relD0.getIdGrupoEconomico();
            }
        }

        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findOcorrencias2Mescla(
                ocorrenciaDTO.getIdOcorrencia(), 
                CodigoTipoStatusOcorrencia.CONCLUIDA.getId(), 
                CodigoTipoStatusOcorrencia.CONCLUIDA.getId(), 
                ocorrenciaDTO.getIsOcorrenciaD1(),
                ocorrenciaDTO.getIdOcorrencia(),
                ocorrenciaDTO.getIdTransportadora());
        for (Ocorrencia entidade : ocorrencias) {
            OcorrenciaDTO dtoInner = OcorrenciaConverter.paraDTO(entidade);
            if(dtoInner.getIsOcorrenciaD1()) {
                
                Optional<RelatorioAnaliticoCreditoD1> optional = relatorioD1Repository.findOneByIdOcorrenciaAndIdEmpresaAndIdGrupoEconomicoAndStatusConciliacaoNotAndIdTransportadora(
                        dtoInner.getIdOcorrencia(), 
                        empresaOcorrencia, 
                        grupoEconomicoOcorrencia, 
                        StatusConciliacaoEnum.PENDENTE.get(),
                        dtoInner.getIdTransportadora());
                if(!optional.isPresent()) {
                    continue;
                }
                RelatorioAnaliticoCreditoD1 relD1 = optional.get();   
                
                if (null == relD1.getValorCredito()) {
                    relD1.setValorCredito(ZERO);
                }

                if (null == relD1.getValorColeta()) {
                    relD1.setValorColeta(ZERO);
                }

                if (relD1.getValorCredito().doubleValue() - relD1.getValorColeta().doubleValue() >= 0L) {
                    continue;
                }
                
                dtoInner.setIdRelatorioAnalitico(relD1.getIdRelatorioAnalitico());
                dtoInner.setDataConferencia(relD1.getDataConferencia());
                dtoInner.setValorConferido(relD1.getValorConferido());
                dtoInner.setValorCreditadoConta(relD1.getValorCredito());
                dtoInner.setDataCredito(relD1.getDataCredito());
                dtoInner.setDataColeta(relD1.getDataColeta());
                dtoInner.setValorColeta(relD1.getValorColeta());
                dtoInner.setStatusConciliacao(relD1.getStatusConciliacao());
                
            } 
            else {
                Optional<RelatorioAnaliticoCredito> optional = relatorioRepository.findOneByIdOcorrenciaAndIdEmpresaAndIdGrupoEconomicoAndStatusConciliacaoNotAndIdTransportadora(
                        dtoInner.getIdOcorrencia(), 
                        empresaOcorrencia, 
                        grupoEconomicoOcorrencia, 
                        StatusConciliacaoEnum.PENDENTE.get(),
                        dtoInner.getIdTransportadora());
                if(!optional.isPresent()) {
                    continue;
                }
                RelatorioAnaliticoCredito relatorioEntidade = optional.get();

                if (null == relatorioEntidade.getValorCredito()) {
                    relatorioEntidade.setValorCredito(ZERO);
                }

                if (null == relatorioEntidade.getValorTotal()) {
                    relatorioEntidade.setValorTotal(ZERO);
                }

                if (relatorioEntidade.getValorCredito().doubleValue() - relatorioEntidade.getValorTotal().doubleValue() >= 0L) {
                    continue;
                }


                dtoInner.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
                dtoInner.setValorQuestionado(entidade.getValorQuestionado());
                dtoInner.setDataCorte(relatorioEntidade.getDataCorte());
                dtoInner.setValorRegistradoCofre(relatorioEntidade.getValorTotal() == null ? ZERO : relatorioEntidade.getValorTotal());
                dtoInner.setValorCreditadoConta(relatorioEntidade.getValorCredito() == null ? ZERO : relatorioEntidade.getValorCredito());
                dtoInner.setDataCredito(relatorioEntidade.getDataCredito());
                dtoInner.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
                
            }
            dtoInner.setValorAjuste(entidade.getValorAjuste());
            dtoInner.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dtoInner.getIdTipoQuestionamento()).getDescricao());
            dtoInner.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dtoInner.getIdTipoStatusOcorrencia()).getDescricao());

            if (null == entidade.getIdStatusMescla() && null != entidade.getIdOcorrenciaMescla()) {
                dtoInner.setIsOcorrenciaSelected(entidade.getIdOcorrenciaMescla().equals(ocorrenciaDTO.getIdOcorrencia()));
            }

            if (!dtoInner.getIsOcorrenciaD1() &&
                    (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) 
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get()))) {
                dtoInner.setIsValorRegistradoCofreLink(true);
            }
            
            dtos.add(dtoInner);

        }

        return dtos;
    }

    @Override
    public OcorrenciaDTO alterarStatus(OcorrenciaDTO dto) throws Exception {

        OcorrenciaDTO ocorrenciaAlteradoDTO = null;
        Ocorrencia ocorrenciaAltera = ocorrenciaRepository.findOne(dto.getIdOcorrencia());

        if (ocorrenciaAltera.getIdTipoStatusOcorrencia() == dto.getIdTipoStatusOcorrencia()) {
            return ocorrenciaAlteradoDTO;
        }

        TipoStatusOcorrencia ocorrenciaAnterior = tipoStatusOcorrenciaRepository.findOne(ocorrenciaAltera.getIdTipoStatusOcorrencia());

        ocorrenciaAltera.setIdTipoStatusOcorrencia(dto.getIdTipoStatusOcorrencia());
        ocorrenciaAltera.setDataStatusOcorrencia(Calendar.getInstance().getTime());
        ocorrenciaAltera.setDataAlteracao(Calendar.getInstance().getTime());
        ocorrenciaAltera.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());

        ocorrenciaAltera = ocorrenciaRepository.save(ocorrenciaAltera);
        ocorrenciaAlteradoDTO = OcorrenciaConverter.paraDTO(ocorrenciaAltera);

        RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOneByIdOcorrencia(ocorrenciaAltera.getIdOcorrencia());
        ocorrenciaAlteradoDTO.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
        ocorrenciaAlteradoDTO.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
        TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(ocorrenciaAltera.getIdTipoStatusOcorrencia());
        relatorioEntidade.setStatusOcorrencia(statusOcorrencia.getDescricao());
        relatorioEntidade.setDataStatusOcorrencia(ocorrenciaAltera.getDataStatusOcorrencia());
        relatorioRepository.save(relatorioEntidade);



        auditoriaService.registrar(
                Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Relatorio + " " + UTF8.Analitico + " de " + UTF8.Creditos + " de Id ", relatorioEntidade.getIdRelatorioAnalitico()}), relatorioEntidade.getIdOcorrencia());

        Atividade atividade = new Atividade();
        atividade.setIdOcorrencia(ocorrenciaAltera.getIdOcorrencia());
        atividade.setIdUsuario(UsuarioAutenticado.getIdUsuario());
        atividade.setDataHorario(Calendar.getInstance().getTime());
        atividade.setAtividade("Status alterado de " + ocorrenciaAnterior.getDescricao() + " para " + statusOcorrencia.getDescricao() + ", Observação: " + dto.getObservacao());
        atividade.setTipoAtividade("M");
        atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        atividade.setDataCriacao(Calendar.getInstance().getTime());
        atividade.setIdtTipoOcorrencia(ocorrenciaAltera.getIdTipoStatusOcorrencia());
        atividadeRepository.save(atividade);

        if (ocorrenciaAnterior.getIdTipoStatusOcorrencia() == CodigoTipoStatusOcorrencia.CONCLUIDA.getId()) {
            if (ocorrenciaAlteradoDTO.getIdTipoStatusOcorrencia() == CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId()
                    || ocorrenciaAlteradoDTO.getIdTipoStatusOcorrencia() == CodigoTipoStatusOcorrencia.ANALISE_ANDAMENTO.getId()) {
                auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Ocorrencia + " de Id ", ocorrenciaAlteradoDTO.getIdOcorrencia()}), ocorrenciaAlteradoDTO.getIdOcorrencia());
                notificacaoService.enviarNotificacaoAnaliseDivergencia(ocorrenciaAlteradoDTO.getIdOcorrencia(), relatorioEntidade.getIdEmpresa());
            }
        }

        if (!ocorrenciaAltera.isIsOcorrenciaD1() &&
                (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) 
                || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get()))) {
            ocorrenciaAlteradoDTO.setIsValorRegistradoCofreLink(true);
        }

        auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[] {atividade.getIdOcorrencia()}), atividade.getIdOcorrencia());

        return ocorrenciaAlteradoDTO;
    }
    
    
    
    @Override
    public String getResponsavel(Ocorrencia ocorrencia) {
        Usuario usuario = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());
        if(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuario.getIdPerfil()) 
                || CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get().equals(usuario.getIdPerfil())) {
            
            if(usuario.getTransportadoraList().size() == 1) {
                return usuario.getTransportadoraList().get(0).getRazaoSocial();
            }
            
            for(Transportadora t : usuario.getTransportadoraList()) {
                if(t.getIdTransportadora().equals(ocorrencia.getIdTransportadora())) {
                    return t.getRazaoSocial();
                }
            }
        }
        else {
            if(usuario.getEmpresaList().size() == 1) {
                return this.grupoRepository.findOne(usuario.getEmpresaList().get(0).getIdGrupoEconomico()).getNome();
            }
            
            if(ocorrencia.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
                for(Empresa empresa : usuario.getEmpresaList()) {
                    if(empresa.getIdEmpresa().equals(relD1.getIdEmpresa())) {
                        return grupoRepository.findOne(empresa.getIdGrupoEconomico()).getNome();
                    }
                }
            } else {
                RelatorioAnaliticoCredito relD0 = relatorioRepository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
                for(Empresa empresa : usuario.getEmpresaList()) {
                    if(empresa.getIdEmpresa().equals(relD0.getIdEmpresa())) {
                        return grupoRepository.findOne(empresa.getIdGrupoEconomico()).getNome();
                    }
                }
            }
        }
        
        return null;
    }
    
    @Override
    public String getResponsavelAprovacao(Ocorrencia ocorrencia) {
        Usuario usuario = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());
        String responsavel = "";
    
        if(CodigoPerfilEnum.MASTER_CLIENTE.get().equals(usuario.getIdPerfil())
        		|| CodigoPerfilEnum.OPERADOR_CLIENTE.get().equals(usuario.getIdPerfil())
                || CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuario.getIdPerfil())
                || CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(usuario.getIdPerfil())
        		|| CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get().equals(usuario.getIdPerfil())
        		|| CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get().equals(usuario.getIdPerfil())
        		|| CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get().equals(usuario.getIdPerfil())
        		|| CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get().equals(usuario.getIdPerfil())) {
            
            // Responsabilidade de aprovar será da Transportadora
            if(ocorrencia.getIdTransportadora() != null) {
                responsavel = transportadoraRepository.findOne(ocorrencia.getIdTransportadora()).getRazaoSocial();
            }
            
        } else {
            // Responsabilidade de aprovar será da Empresa
            if(ocorrencia.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());            
                if(relD1 != null && relD1.getIdGrupoEconomico() != null) {
                    responsavel = this.grupoRepository.findOne(relD1.getIdGrupoEconomico()).getNome();
                }
            } else {
                RelatorioAnaliticoCredito relD0 = relatorioRepository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
                if(relD0 != null && relD0.getIdGrupoEconomico() != null) {
                    responsavel = this.grupoRepository.findOne(relD0.getIdGrupoEconomico()).getNome();
                }
            }
        }

        return responsavel;
    }    
    

    @Override
    public OcorrenciaDTO mesclar(OcorrenciaDTO dto) throws Exception {
        Ocorrencia ocorrenciaMescla = OcorrenciaConverter.paraEntidade(dto);
        ocorrenciaMescla.setDataAlteracao(Calendar.getInstance().getTime());
        ocorrenciaMescla.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
        ocorrenciaMescla.setIdTipoStatusOcorrencia(CodigoTipoStatusOcorrencia.AGUARDANDO_APROVACAO.getId());
        ocorrenciaMescla.setIdOcorrenciaMescla(null);
        ocorrenciaMescla.setValorAjuste(dto.getValorAjuste());

        ocorrenciaRepository.save(ocorrenciaMescla);
        StringBuilder observacao = new StringBuilder("Ocorrência ")
                .append(dto.getIdOcorrencia())
                .append(" com os Ids das ocorrências envolvidas(");

        for (Ocorrencia mescla : dto.getMesclas()) {
            LOGGER.info("mescla: " +mescla.getIdOcorrencia() + " valorAjuste: " + mescla.getValorAjuste());
        }

        for (Ocorrencia mescla : dto.getMesclas()) {

            if(mescla.getIdOcorrenciaMescla() != null) {
                observacao.append(mescla.getIdOcorrencia()).append(",");
            }
                    

            mescla.setDataAlteracao(Calendar.getInstance().getTime());
            mescla.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            ocorrenciaRepository.save(mescla);
            
        }
        // Removendo ultima virgula
        if(observacao.charAt(observacao.length()-1) == ',') {
            observacao = new StringBuilder(observacao.substring(0, observacao.length()-1));
        }
        observacao.append(") ");

            
        Atividade atividade = new Atividade();
        atividade.setIdOcorrencia(ocorrenciaMescla.getIdOcorrencia());
        atividade.setIdUsuario(UsuarioAutenticado.getIdUsuario());
        atividade.setDataHorario(Calendar.getInstance().getTime());
        atividade.setAtividade(observacao.toString() + dto.getObservacao());
        atividade.setTipoAtividade("M");
        atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        atividade.setDataCriacao(Calendar.getInstance().getTime());
        atividade.setIdtTipoOcorrencia(ocorrenciaMescla.getIdTipoStatusOcorrencia());
        // Seta quem sera o responsavel pela aprovacao da Mescla
        atividade.setResponsavel(getResponsavelAprovacao(ocorrenciaMescla));
        
        atividade.setMescla(true);
        atividadeRepository.save(atividade);
        dto = OcorrenciaConverter.paraDTO(ocorrenciaMescla);

        TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(ocorrenciaMescla.getIdTipoStatusOcorrencia());
        ocorrenciaMescla.setIdTipoStatusOcorrencia(statusOcorrencia.getIdTipoStatusOcorrencia());
        ocorrenciaMescla.setDataStatusOcorrencia(Calendar.getInstance().getTime());
        ocorrenciaRepository.save(ocorrenciaMescla);

        AuditoriaDTO auditoriaDTO = new AuditoriaDTO();
        if(dto.getIsOcorrenciaD1()) {
            RelatorioAnaliticoCreditoD1 relatorioAnaliticoCreditoD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrenciaMescla.getIdOcorrencia());
            relatorioAnaliticoCreditoD1.setDataStatusOcorrencia(ocorrenciaMescla.getDataStatusOcorrencia());
            relatorioAnaliticoCreditoD1.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioD1Repository.save(relatorioAnaliticoCreditoD1);

            Empresa empresa = empresaRepository.findOne(relatorioAnaliticoCreditoD1.getIdEmpresa());

            auditoriaDTO.setIdEmpresa(relatorioAnaliticoCreditoD1.getIdEmpresa());
            auditoriaDTO.setEmpresa(empresa.getRazaoSocial());
            auditoriaDTO.setIdGrupoEconomico(empresa.getIdGrupoEconomico());

        }else {
            RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioRepository.findOneByIdOcorrencia(ocorrenciaMescla.getIdOcorrencia());
            relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrenciaMescla.getDataStatusOcorrencia());
            relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioRepository.save(relatorioAnaliticoCredito);

        }

        auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[] {atividade.getIdOcorrencia()}), atividade.getIdOcorrencia());

        return dto;
    }

    @Override
    public OcorrenciaDTO desmesclar(OcorrenciaDTO dto) throws Exception {

        Ocorrencia ocorrenciaMescla = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
        ocorrenciaMescla.setDataAlteracao(Calendar.getInstance().getTime());
        ocorrenciaMescla.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
        ocorrenciaMescla.setIdTipoStatusOcorrencia(3L);

        for (Ocorrencia mescla : dto.getMesclas()) {
            mescla = ocorrenciaRepository.findOne(mescla.getIdOcorrencia());
            mescla.setValorAjuste(mescla.getValorAjuste().subtract(mescla.getValorAjuste()));
            ocorrenciaMescla.getValorAjuste().add(mescla.getValorAjuste());
            mescla.setIdOcorrenciaMescla(null);
            mescla.setDataAlteracao(Calendar.getInstance().getTime());
            mescla.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            ocorrenciaRepository.save(mescla);
        }

        ocorrenciaRepository.save(ocorrenciaMescla);

        return OcorrenciaConverter.paraDTO(ocorrenciaMescla);
    }

    @Override
    public List<OcorrenciaDTO> listarMesclados(Long idOcorrencia) throws Exception {
        Ocorrencia ocorrenciaAberta = ocorrenciaRepository.findOne(idOcorrencia);
        List<OcorrenciaDTO> dtos = new ArrayList<>();
        for (Ocorrencia entidade : ocorrenciaAberta.getMesclas()) {
            OcorrenciaDTO dtoInner = OcorrenciaConverter.paraDTO(entidade);
            dtoInner.setValorQuestionado(entidade.getValorQuestionado());
            dtoInner.setValorAjuste(entidade.getValorAjuste());
            if(entidade.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relatorioEntidade = relatorioD1Repository.findOneByIdOcorrencia(dtoInner.getIdOcorrencia());
                dtoInner.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
                dtoInner.setDataConferencia(relatorioEntidade.getDataConferencia());
                dtoInner.setValorConferido(relatorioEntidade.getValorConferido());
                dtoInner.setValorCreditadoConta(relatorioEntidade.getValorCredito());
                dtoInner.setDataCredito(relatorioEntidade.getDataCredito());
                dtoInner.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
                dtoInner.setValorDeclarado(relatorioEntidade.getValorColeta());
                dtoInner.setValorColeta(relatorioEntidade.getValorColeta());
                dtoInner.setDataColeta(relatorioEntidade.getDataColeta());
                
            } else {
                RelatorioAnaliticoCredito relatorioEntidade = relatorioRepository.findOneByIdOcorrencia(dtoInner.getIdOcorrencia());
                dtoInner.setIdRelatorioAnalitico(relatorioEntidade.getIdRelatorioAnalitico());
                dtoInner.setDataCorte(relatorioEntidade.getDataCorte());
                dtoInner.setValorRegistradoCofre(relatorioEntidade.getValorTotal() == null ? ZERO : relatorioEntidade.getValorTotal());
                dtoInner.setValorCreditadoConta(relatorioEntidade.getValorCredito() == null ? ZERO : relatorioEntidade.getValorCredito());
                dtoInner.setStatusConciliacao(relatorioEntidade.getStatusConciliacao());
            }            
            
            
            dtoInner.setTipoQuestionamento(tipoQuestionamentoRepository.findOne(dtoInner.getIdTipoQuestionamento()).getDescricao());
            dtoInner.setTipoStatusOcorrencia(tipoStatusOcorrenciaRepository.findOne(dtoInner.getIdTipoStatusOcorrencia()).getDescricao());
            if (UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.ADMINISTRADOR.get()) || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.MASTER_TRANSPORTADORA.get())
                    || UsuarioAutenticado.getPerfil().equalsIgnoreCase(PerfilEnum.OPERADOR_TRANSPORTADORA.get())) {
                dtoInner.setIsValorRegistradoCofreLink(true);
            }

            if (null != entidade.getIdStatusMescla() && entidade.getIdStatusMescla() == 4L) {
                continue;
            }

            List<Atividade> observacao = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(ocorrenciaAberta.getIdOcorrencia());
            dtoInner.setObservacao(observacao.get(0).getAtividade());

            dtos.add(dtoInner);
        }
        return dtos;
    }
    
    
    @Override
    public boolean verificarPermissaoPorUsuario(Usuario usuarioSolicitante, Usuario usuarioLogado) {
        if(CodigoPerfilEnum.ADMINISTRADOR.get().equals(usuarioLogado.getIdPerfil())
                || CodigoPerfilEnum.BPO.get().equals(usuarioLogado.getIdPerfil())) {
            return true;
        }
        
        if(CodigoPerfilEnum.BPO.get().equals(usuarioSolicitante.getIdPerfil())) {

            return (CodigoPerfilEnum.MASTER_CLIENTE.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get().equals(usuarioLogado.getIdPerfil()));
        }        
        
        if(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuarioSolicitante.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get().equals(usuarioSolicitante.getIdPerfil())) {

            return (CodigoPerfilEnum.MASTER_CLIENTE.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil()));
        }
        
        if(CodigoPerfilEnum.MASTER_CLIENTE.get().equals(usuarioSolicitante.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE.get().equals(usuarioSolicitante.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get().equals(usuarioLogado.getIdPerfil())) {

            return (CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuarioLogado.getIdPerfil())
            || CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get().equals(usuarioLogado.getIdPerfil()));
        }
        
        return false;
        
    }


    @Override
    public OcorrenciaDTO aprovarMescla(Long idOcorrencia) {
        Ocorrencia ocorrenciaAberta = ocorrenciaRepository.findOne(idOcorrencia);

        Usuario usuarioSolicitante = usuarioRepository.findByIdUsuario(ocorrenciaAberta.getIdUsuarioAlteracao());
        Usuario usuarioAprovador = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());

      
        if (verificarPermissaoPorUsuario(usuarioSolicitante, usuarioAprovador)) {        

            for (Ocorrencia entidade : ocorrenciaAberta.getMesclas()) {
                if (null != entidade.getIdStatusMescla() && entidade.getIdStatusMescla() == 4L) {
                    continue;
                }
                entidade = ocorrenciaRepository.findOne(entidade.getIdOcorrencia());
                entidade.setIdStatusMescla(4L);
                ocorrenciaRepository.save(entidade);
            }

            Atividade atividade = new Atividade();
            atividade.setIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
            atividade.setIdUsuario(UsuarioAutenticado.getIdUsuario());
            atividade.setDataCriacao(Calendar.getInstance().getTime());
            atividade.setDataHorario(Calendar.getInstance().getTime());
            atividade.setTipoAtividade("M");
            atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
            List<Atividade> observacao = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(ocorrenciaAberta.getIdOcorrencia());
            atividade.setAtividade("Mescla Aprovada: " + observacao.get(0).getAtividade());
            atividade.setIdtTipoOcorrencia(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
            atividade.setResponsavel(getResponsavel(ocorrenciaAberta));
            atividadeRepository.save(atividade);

            TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
            ocorrenciaAberta.setIdTipoStatusOcorrencia(statusOcorrencia.getIdTipoStatusOcorrencia());
            ocorrenciaAberta.setDataStatusOcorrencia(Calendar.getInstance().getTime());
            ocorrenciaRepository.save(ocorrenciaAberta);
            
            if(ocorrenciaAberta.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
                relD1.setDataStatusOcorrencia(ocorrenciaAberta.getDataStatusOcorrencia());
                relD1.setStatusOcorrencia(statusOcorrencia.getDescricao());
                relatorioD1Repository.save(relD1);
                
            } else {
                RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioRepository.findOneByIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
                relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrenciaAberta.getDataStatusOcorrencia());
                relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
                relatorioRepository.save(relatorioAnaliticoCredito);
            }

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.nao.foi.possivel.aprovar.mescla", new Object[]{""}));
        }

        return OcorrenciaConverter.paraDTO(ocorrenciaAberta);

    }

    @Override
    public OcorrenciaDTO rejeitarMescla(Long idOcorrencia) {
        Ocorrencia ocorrenciaAberta = ocorrenciaRepository.findOne(idOcorrencia);

        Usuario usuarioSolicitante = usuarioRepository.findByIdUsuario(ocorrenciaAberta.getIdUsuarioAlteracao());
        Usuario usuarioAprovador = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());

     
        if (verificarPermissaoPorUsuario(usuarioSolicitante, usuarioAprovador)) {        

            for (Ocorrencia entidade : ocorrenciaAberta.getMesclas()) {
                entidade.setIdOcorrenciaMescla(null);
                ocorrenciaAberta.setValorAjuste(ocorrenciaAberta.getValorAjuste().add(entidade.getValorAjuste()));
                entidade.setValorAjuste(entidade.getValorAjuste().subtract(entidade.getValorAjuste()));
                ocorrenciaRepository.save(entidade);
            }

            ocorrenciaRepository.save(ocorrenciaAberta);

            Atividade atividade = new Atividade();
            atividade.setIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
            atividade.setIdUsuario(UsuarioAutenticado.getIdUsuario());
            atividade.setDataCriacao(Calendar.getInstance().getTime());
            atividade.setDataHorario(Calendar.getInstance().getTime());
            atividade.setTipoAtividade("M");
            atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
            List<Atividade> observacao = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(ocorrenciaAberta.getIdOcorrencia());
            atividade.setAtividade("Mescla Rejeitada: " + observacao.get(0).getAtividade());
            atividade.setIdtTipoOcorrencia(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
            atividade.setResponsavel(getResponsavel(ocorrenciaAberta));
            atividadeRepository.save(atividade);

            TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
            ocorrenciaAberta.setIdTipoStatusOcorrencia(statusOcorrencia.getIdTipoStatusOcorrencia());
            ocorrenciaAberta.setDataStatusOcorrencia(Calendar.getInstance().getTime());
            ocorrenciaRepository.save(ocorrenciaAberta);
            if(ocorrenciaAberta.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
                relD1.setDataStatusOcorrencia(ocorrenciaAberta.getDataStatusOcorrencia());
                relD1.setStatusOcorrencia(statusOcorrencia.getDescricao());
                relatorioD1Repository.save(relD1);               
                
            } else {
                RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioRepository.findOneByIdOcorrencia(ocorrenciaAberta.getIdOcorrencia());
                relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrenciaAberta.getDataStatusOcorrencia());
                relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
                relatorioRepository.save(relatorioAnaliticoCredito);
            }

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.nao.foi.possivel.aprovar.rejeitar", new Object[]{""}));
        }

        return OcorrenciaConverter.paraDTO(ocorrenciaAberta);

    }

    @Override
    public Page<RelatorioOcorrenciaDTO> listarPorCriterio(RelatorioOcorrenciaDTO dto, Pageable pageable) {
        Page<RelatorioOcorrenciaDTO> listaRelatorioOcorrencia;
        List<Long> ids = new ArrayList<>();
        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

        dto.setIdGrupoEconomicoList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));

        if (usuario.getIdPerfil().equals(OPERADOR_CLIENTE.get())
                || usuario.getIdPerfil().equals(OPERADOR_CLIENTE_VENDA_NUMERARIO.get())
        		|| usuario.getIdPerfil().equals(OPERADOR_CLIENTE_CARTAO.get())
        		|| usuario.getIdPerfil().equals(OPERADOR_CLIENTE_NUMERARIO.get())) {
            dto.setCnpjClienteList(usuario.getEmpresaList().stream().map(Empresa::getCnpj).collect(Collectors.toList()));
            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
            dto.setIdGrupoEconomico(null);
        }

        if (usuario.getIdPerfil().equals(MASTER_CLIENTE.get())
            || usuario.getIdPerfil().equals(MASTER_CLIENTE_VENDA_NUMERARIO.get())
        	|| usuario.getIdPerfil().equals(MASTER_CLIENTE_CARTAO.get())
        	|| usuario.getIdPerfil().equals(MASTER_CLIENTE_NUMERARIO.get())) {
            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
            dto.setIdGrupoEconomico(null);
        }
        
        if (!usuario.getIdPerfil().equals(ADMINISTRADOR.get()) && dto.getIdTransportadora() == null) {
            List<Long> idsTransportadoras = new ArrayList();
            try {
                transportadoraService.listarCriterios(new TransportadoraDTO()).stream().forEach(transportadora -> {
                    idsTransportadoras.add(transportadora.getIdTransportadora());
                });
                dto.setIdsTransportadoras(idsTransportadoras);
            } catch(Exception e) {
                LOGGER.error("Erro ao pesquisar Transportadora " + e.getMessage(), e);
            }
        }

        List<RelatorioOcorrenciaDTO> dtoList = relatorioOcRepository.consulta(dto);

        if (isNotEmpty(dto.getFarois())) {
            for (RelatorioOcorrenciaDTO relatorioOcorrenciaD0 : dtoList) {
                RelatorioOcorrenciaDTO dtoConvertido = converter.convert(relatorioOcorrenciaD0);
                
                for (String f : dto.getFarois()) {
                    if (f.equals(dtoConvertido.getFarol())) {
                        ids.add(relatorioOcorrenciaD0.getRowNumber());
                    }
                }
            }
        }

        LOGGER.info("======================> OcorrenciaService.listarPorCriterio(RelatorioOcorrenciaDTO dto, Pageable pageable).dto: " + dto.toString() + "==============");
        LOGGER.info("======================> OcorrenciaService.listarPorCriterio(RelatorioOcorrenciaDTO dto, Pageable pageable).ids: " + ids.toString() + "==============");
        listaRelatorioOcorrencia = relatorioOcRepository.consultaByIds(dto, ids, pageable);

        listaRelatorioOcorrencia.forEach(relatorioOcorrenciaDTO -> {
            ModeloNegocio modeloNegocio = modeloRepository.findOne(relatorioOcorrenciaDTO.getIdModeloNegocio());
            if (modeloNegocio != null) {
                relatorioOcorrenciaDTO.setNomeModeloNegocio(modeloNegocio.getNome());
            }
        });

        return listaRelatorioOcorrencia;
    }

    @Override
    public List<RelatorioOcorrenciaDTO> listarPorCriterioExportar(RelatorioOcorrenciaDTO dto) {

        List<RelatorioOcorrenciaDTO> listaRelatorioOcorrenciaDTO = new ArrayList<>();
        List<RelatorioOcorrenciaD0> listaRelatorioOcorrencia;
        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
        List<Long> ids = new ArrayList<>();

        dto.setIdGrupoEconomicoList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));

        if (usuario.getIdPerfil().equals(OPERADOR_CLIENTE.get())
                || usuario.getIdPerfil().equals(OPERADOR_CLIENTE_VENDA_NUMERARIO.get())
        		|| usuario.getIdPerfil().equals(OPERADOR_CLIENTE_CARTAO.get())
        		|| usuario.getIdPerfil().equals(OPERADOR_CLIENTE_NUMERARIO.get())) {
            dto.setCnpjClienteList(usuario.getEmpresaList().stream().map(Empresa::getCnpj).collect(Collectors.toList()));
            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
            dto.setIdGrupoEconomico(null);
        }

        if (usuario.getIdPerfil().equals(MASTER_CLIENTE.get())
                || usuario.getIdPerfil().equals(MASTER_CLIENTE_VENDA_NUMERARIO.get())
            	|| usuario.getIdPerfil().equals(MASTER_CLIENTE_CARTAO.get())
            	|| usuario.getIdPerfil().equals(MASTER_CLIENTE_NUMERARIO.get())) {
            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
            dto.setIdGrupoEconomico(null);
        }

        Specification<RelatorioOcorrenciaD0> specs = RelatorioOcorrenciaD0Specification.byCriterio(dto);

        listaRelatorioOcorrencia = relatorioOcorrenciaRepository.findAll(specs);

        if (!dto.getFarois().isEmpty() || null != dto.getFarois()) {

            for (RelatorioOcorrenciaD0 relatorioOcorrenciaD0 : listaRelatorioOcorrencia) {

                RelatorioOcorrenciaDTO relatorioDto = converter.convert(RelatorioOcorrenciaD0Converter.paraDTO(relatorioOcorrenciaD0));

                for (String f : dto.getFarois()) {
                    if (f.equals(relatorioDto.getFarol())) {
                        ids.add(relatorioOcorrenciaD0.getRow_number());
                    }
                }
            }

        }

        Specification<RelatorioOcorrenciaD0> specsF = RelatorioOcorrenciaD0Specification.byCriterioIds(dto, ids);
        listaRelatorioOcorrencia = relatorioOcorrenciaRepository.findAll(specsF);
        if (!ids.isEmpty()) {
            listaRelatorioOcorrencia = relatorioOcorrenciaRepository.findAll(specsF);
        } else {
            listaRelatorioOcorrencia = relatorioOcorrenciaRepository.findAll(specs);
        }

        for (RelatorioOcorrenciaD0 relatorioOcorrenciaD0 : listaRelatorioOcorrencia) {
            RelatorioOcorrenciaDTO relatorioOcorrenciaDTO = converter.convert(RelatorioOcorrenciaD0Converter.paraDTO(relatorioOcorrenciaD0));

            ModeloNegocio modeloNegocio = modeloRepository.findOne(relatorioOcorrenciaD0.getIdModeloNegocio());
            if (modeloNegocio != null) {
                relatorioOcorrenciaDTO.setNomeModeloNegocio(modeloNegocio.getNome());
            }

            listaRelatorioOcorrenciaDTO.add(relatorioOcorrenciaDTO);
        }

        return listaRelatorioOcorrenciaDTO;
    }

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

}
