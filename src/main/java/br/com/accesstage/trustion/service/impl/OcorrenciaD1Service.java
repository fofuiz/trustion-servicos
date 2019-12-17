package br.com.accesstage.trustion.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.accesstage.trustion.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.OcorrenciaD1Converter;
import br.com.accesstage.trustion.converter.RelatorioOcorrenciaDnConverter;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoStatusOcorrencia;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;
import br.com.accesstage.trustion.model.RelatorioOcorrenciaDN;
import br.com.accesstage.trustion.model.SlaAtendimento;
import br.com.accesstage.trustion.model.TipoQuestionamento;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.RelatorioOcorrenciaDnSpecification;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoD1Repository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioOcorrenciaDnRepository;
import br.com.accesstage.trustion.repository.interfaces.ISlaAtendimentoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoQuestionamentoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.INotificacaoService;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaD1Service;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoD1Service;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OcorrenciaD1Service implements IOcorrenciaD1Service {

	@Autowired
	private IRelatorioAnaliticoCreditoD1Repository relatorioD1Repository;

	@Autowired
	private IOcorrenciaRepository ocorrenciaRepository;

	@Autowired
	private IRelatorioAnaliticoCreditoD1Service relatorioD1Service;

	@Autowired
	private IGrupoEconomicoRepository grupoEconomicoRepository;

	@Autowired
	private IEmpresaRepository empresaRepository;

	@Autowired
	private IAuditoriaService auditoriaService;

	@Autowired
	private INotificacaoService notificacaoService;

	@Autowired
	private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

	@Autowired
	private ITipoQuestionamentoRepository tipoQuestionamentoRepository;

	@Autowired
	private IAtividadeService atividadeService;

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private IAtividadeRepository atividadeRepository;

	@Autowired
	private IRelatorioOcorrenciaDnRepository relatorioOcorrenciaDnRepository;

	@Autowired
	private ISlaAtendimentoRepository slaAtendimentoRepository;
        
        @Autowired
        private ITransportadoraRepository transportadoraRepository;

	private static final String MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA = "msg.auditoria.atividade.na.ocorrencia";

	private Converter<RelatorioOcorrenciaDN, RelatorioOcorrenciaDNDTO> converter = new Converter<RelatorioOcorrenciaDN, RelatorioOcorrenciaDNDTO>() {

		@Override
		public RelatorioOcorrenciaDNDTO convert(RelatorioOcorrenciaDN source) {
			RelatorioOcorrenciaDNDTO dto = RelatorioOcorrenciaDnConverter.paraDTO(source);
			List<Atividade> atividade = atividadeRepository
					.findAllByIdOcorrenciaOrderByDataHorarioDesc(dto.getIdOcorrencia());

			dto.setDiasPendentes(!atividade.isEmpty()
					? ChronoUnit.DAYS.between(
							atividade.get(0).getDataHorario().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
							LocalDate.now())
					: ChronoUnit.DAYS.between(
							dto.getDataStatusOcorrencia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
							LocalDate.now()));
			dto.setDiasEmAberto(ChronoUnit.DAYS.between(
					dto.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()));

			if (tipoStatusOcorrenciaRepository.findByDescricao(dto.getStatusOcorrencia()).getIdTipoStatusOcorrencia()
					.intValue() == 4L) {

				dto.setDiasEmAberto(ChronoUnit.DAYS.between(
						dto.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
						atividade.get(0).getDataHorario().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
				dto.setDiasPendentes(null);
			} else if (null != dto.getId_sla_atendimento()) {

				SlaAtendimento sla = slaAtendimentoRepository.findOne(dto.getId_sla_atendimento());

				TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository
						.findByDescricao(dto.getStatusOcorrencia());

				if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 1) {

					dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseSolicitada());

					if (sla.isAnaliseSolDiaUtil()) {
						LocalDate dtStatusOcorrencia = dto.getDataStatusOcorrencia().toInstant()
								.atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate dateNow = LocalDate.now();
						List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
						List<LocalDate> diasUteis = new ArrayList<>();

						for (LocalDate localDate : datesBetween) {
							if (!localDate.getDayOfWeek().name().equals("SATURDAY")
									&& !localDate.getDayOfWeek().name().equals("SUNDAY"))
								diasUteis.add(localDate);
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
						LocalDate dtStatusOcorrencia = dto.getDataStatusOcorrencia().toInstant()
								.atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate dateNow = LocalDate.now();
						List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
						List<LocalDate> diasUteis = new ArrayList<>();

						for (LocalDate localDate : datesBetween) {
							if (!localDate.getDayOfWeek().name().equals("SATURDAY")
									&& !localDate.getDayOfWeek().name().equals("SUNDAY"))
								diasUteis.add(localDate);

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
						LocalDate dtStatusOcorrencia = dto.getDataStatusOcorrencia().toInstant()
								.atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate dateNow = LocalDate.now();
						List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
						List<LocalDate> diasUteis = new ArrayList<>();

						for (LocalDate localDate : datesBetween) {
							if (!localDate.getDayOfWeek().name().equals("SATURDAY")
									&& !localDate.getDayOfWeek().name().equals("SUNDAY"))
								diasUteis.add(localDate);
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
			}

			return dto;
		}

	};

	@Override
	public OcorrenciaD1DTO criar(OcorrenciaD1DTO dto) throws Exception {
		OcorrenciaD1DTO dtoCriado = null;
		Ocorrencia entidade = OcorrenciaD1Converter.paraEntidade(dto);
		entidade.setIdTipoStatusOcorrencia(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId());
		

		RelatorioAnaliticoCreditoD1 relatorioD1 = relatorioD1Repository.findOne(dto.getIdRelatorioAnalitico());

		if (relatorioD1.getValorCredito() != null) {

			if (relatorioD1.getValorColeta() != null) {
				entidade.setValorQuestionado(relatorioD1.getValorCredito().subtract(relatorioD1.getValorColeta()));
			} else {
				entidade.setValorQuestionado(relatorioD1.getValorCredito());
			}

		} else {
			entidade.setValorQuestionado(
					relatorioD1.getValorColeta() == null ? null : relatorioD1.getValorColeta().negate());
		}

		entidade.setDataStatusOcorrencia(Calendar.getInstance().getTime());
		entidade.setDataCriacao(Calendar.getInstance().getTime());
		entidade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
                entidade.setIdTransportadora(relatorioD1.getIdTransportadora());
                entidade.setIsOcorrenciaD1(true);

		Ocorrencia entidadeCriado = ocorrenciaRepository.save(entidade);

		if (entidadeCriado != null) {
			dtoCriado = OcorrenciaD1Converter.paraDTO(entidadeCriado);
			dtoCriado.setIdRelatorioAnalitico(dto.getIdRelatorioAnalitico());
			RelatorioAnaliticoCreditoD1DTO relatorioDTO = relatorioD1Service.alterarStatusOcorrencia(dtoCriado);
			dtoCriado.setIdRelatorioAnalitico(relatorioDTO.getIdRelatorioAnalitico());
			Empresa empresa = empresaRepository.findOne(relatorioDTO.getIdEmpresa());
			
			GrupoEconomico grupo = grupoEconomicoRepository.findOne(relatorioDTO.getIdGrupoEconomico());
			dtoCriado.setGrupoEconomico(grupo == null ? null : grupo.getNome());
			dtoCriado.setEmpresa(empresa == null ? null : empresa.getRazaoSocial());
			
			dtoCriado.setDataConferencia(relatorioDTO.getDataConferencia());
			dtoCriado.setValorConferido(relatorioDTO.getValorConferido());
			dtoCriado.setDataCredito(relatorioDTO.getDataCredito());
			dtoCriado.setValorCreditado(relatorioDTO.getValorCredito());

            auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{UTF8.Ocorrencia + " de Id ", dtoCriado.getIdOcorrencia()}), dtoCriado.getIdOcorrencia());
			notificacaoService.enviarNotificacaoAnaliseDivergencia(entidadeCriado.getIdOcorrencia(),
					relatorioDTO.getIdEmpresa());
			AtividadeDTO dtoAtividade = new AtividadeDTO();
			dtoAtividade.setAtividade(dto.getObservacao());
			dtoAtividade.setIdOcorrencia(dtoCriado.getIdOcorrencia());
			dtoAtividade.setIdTipoOcorrencia(dtoCriado.getIdTipoStatusOcorrencia());
            dtoAtividade.setIdEmpresa(relatorioD1.getIdEmpresa());
			dtoAtividade.setStatusOcorrencia(
					tipoStatusOcorrenciaRepository.findOne(dtoCriado.getIdTipoStatusOcorrencia()).getDescricao());
                        if(relatorioD1.getIdTransportadora() != null) {
                            Transportadora t = this.transportadoraRepository.findOne(relatorioD1.getIdTransportadora());
                            dtoAtividade.setResponsavel(t.getRazaoSocial());
                        }
			atividadeService.criar(dtoAtividade);
		}

		return dtoCriado;
	}

	@Override
	public OcorrenciaD1DTO alterar(OcorrenciaD1DTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OcorrenciaD1DTO pesquisar(Long idOcorrencia) throws Exception {

		OcorrenciaD1DTO dto = null;
		Ocorrencia entidade = ocorrenciaRepository.findOne(idOcorrencia);

		if (entidade != null) {
			dto = OcorrenciaD1Converter.paraDTO(entidade);

			RelatorioAnaliticoCreditoD1 relatorio = relatorioD1Repository
					.findOneByIdOcorrencia(entidade.getIdOcorrencia());

			dto.setIdRelatorioAnalitico(relatorio.getIdRelatorioAnalitico());
			dto.setDataConferencia(relatorio.getDataColeta());
			dto.setValorConferido(relatorio.getValorColeta());
			dto.setDataCredito(relatorio.getDataCredito());
			dto.setValorCreditado(relatorio.getValorCredito());
			dto.setStatusConciliacao(relatorio.getStatusConciliacao());
			dto.setValorDeclarado(relatorio.getValorColeta());

			TipoQuestionamento tipoQuestionamento = tipoQuestionamentoRepository.findOne(dto.getIdTipoQuestionamento());
			dto.setTipoQuestionamento(tipoQuestionamento == null ? null : tipoQuestionamento.getDescricao());

			TipoStatusOcorrencia tipoStatusOcorrencia = tipoStatusOcorrenciaRepository
					.findOne(dto.getIdTipoStatusOcorrencia());
			dto.setTipoStatusOcorrencia(tipoStatusOcorrencia == null ? null : tipoStatusOcorrencia.getDescricao());

			GrupoEconomico grupo = grupoEconomicoRepository.findOne(relatorio.getIdGrupoEconomico());
			dto.setGrupoEconomico(grupo == null ? null : grupo.getNome());

			Empresa empresa = empresaRepository.findOne(relatorio.getIdEmpresa());
			dto.setEmpresa(empresa == null ? null : empresa.getRazaoSocial());
                        
                        
			dto.setConcluido(false);

			if (Objects.equals(dto.getIdTipoStatusOcorrencia(), CodigoTipoStatusOcorrencia.CONCLUIDA.getId())) {

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

		} else {
			throw new ResourceNotFoundException(
					Mensagem.get("msg.registro.nao.existe", new Object[] { UTF8.ocorrencia }));
		}

		return dto;
	}

	@Override
	public List<OcorrenciaD1DTO> listarTodos() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OcorrenciaD1DTO alterarStatus(OcorrenciaD1DTO dto) throws Exception {

		OcorrenciaD1DTO dtoAlterado = null;

		Ocorrencia entidadeAltera = ocorrenciaRepository.findOne(dto.getIdOcorrencia());

		if (entidadeAltera != null) {

			TipoStatusOcorrencia statusAnterior = tipoStatusOcorrenciaRepository
					.findOne(entidadeAltera.getIdTipoStatusOcorrencia());

			entidadeAltera.setIdTipoStatusOcorrencia(dto.getIdTipoStatusOcorrencia());
			entidadeAltera.setDataStatusOcorrencia(Calendar.getInstance().getTime());
			entidadeAltera.setDataAlteracao(Calendar.getInstance().getTime());
			entidadeAltera.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());

			entidadeAltera = ocorrenciaRepository.save(entidadeAltera);

			RelatorioAnaliticoCreditoD1DTO relatorioDTO = relatorioD1Service
					.atualizarStatusOcorrencia(OcorrenciaD1Converter.paraDTO(entidadeAltera));

			TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository
					.findOne(entidadeAltera.getIdTipoStatusOcorrencia());

			AtividadeDTO atividade = new AtividadeDTO();
			atividade.setIdOcorrencia(entidadeAltera.getIdOcorrencia());
			atividade.setAtividade("Status alterado de " + statusAnterior.getDescricao() + " para "
					+ statusOcorrencia.getDescricao() + ", Observação: " + dto.getObservacao());
			atividade.setTipoAtividade("M");
			atividade.setIdTipoOcorrencia(dto.getIdTipoStatusOcorrencia());
			atividade.setIdEmpresa(relatorioDTO.getIdEmpresa());
			atividadeService.criar(atividade);
			dtoAlterado = OcorrenciaD1Converter.paraDTO(entidadeAltera);
			if (CodigoTipoStatusOcorrencia.CONCLUIDA.getId().equals(statusAnterior.getIdTipoStatusOcorrencia())) {
				if (CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId().equals(dtoAlterado.getIdTipoStatusOcorrencia())
				|| CodigoTipoStatusOcorrencia.ANALISE_ANDAMENTO.getId().equals(dtoAlterado.getIdTipoStatusOcorrencia())) {

					auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado",
							new Object[] { UTF8.Ocorrencia + " de Id ", dtoAlterado.getIdOcorrencia() }), dtoAlterado.getIdOcorrencia());
					notificacaoService.enviarNotificacaoAnaliseDivergencia(dtoAlterado.getIdOcorrencia(),
							relatorioDTO.getIdEmpresa());
				}
			}
		} else {
			throw new ResourceNotFoundException(
					Mensagem.get("msg.registro.nao.existe", new Object[] { UTF8.ocorrencia }));
		}

		return dtoAlterado;
	}
        

	@Override
	public Page<RelatorioOcorrenciaDNDTO> listarPorCriterio(RelatorioOcorrenciaDNDTO dto, Pageable pageable) {
		List<RelatorioOcorrenciaDN> listaRelatorioOcorrenciaDN = null;
		Page<RelatorioOcorrenciaDNDTO> listaRelatorioOcorrenciaDP = null;
		Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
		List<Long> ids = new ArrayList<>();

		if (usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_CLIENTE.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_CLIENTE.get())) {

			
                        dto.setIdGrupoEconomicoList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));

			if (usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_CLIENTE.get())) {
				dto.setCnpjClienteList(usuario.getEmpresaList().stream().map(Empresa::getCnpj).collect(Collectors.toList()));
                                dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
                                dto.setIdGrupoEconomico(null);
			}
			
			if (usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_CLIENTE.get())) {
                            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
                            dto.setIdGrupoEconomico(null);
			}

			Specification<RelatorioOcorrenciaDN> specs = RelatorioOcorrenciaDnSpecification.byCriterio(dto);
			listaRelatorioOcorrenciaDN = relatorioOcorrenciaDnRepository.findAll(specs);
			if (null != dto.getFarois() && !dto.getFarois().isEmpty()) {
				for (RelatorioOcorrenciaDN relatorioOcorrenciaDN : listaRelatorioOcorrenciaDN) {
					RelatorioOcorrenciaDNDTO dto2 = converter.convert(relatorioOcorrenciaDN);
					for (String f : dto.getFarois()) {
						if (f.equals(dto2.getFarol())) {
							ids.add(relatorioOcorrenciaDN.getRow_number());
						}
					}

				}
			}
			
			Specification<RelatorioOcorrenciaDN> specsF = RelatorioOcorrenciaDnSpecification.byCriterioIds(dto, ids);
			if (!ids.isEmpty()) {
				listaRelatorioOcorrenciaDP = relatorioOcorrenciaDnRepository.findAll(specsF, pageable).map(converter);
			} else {
				listaRelatorioOcorrenciaDP = relatorioOcorrenciaDnRepository.findAll(specs, pageable).map(converter);
			}

		}

		return listaRelatorioOcorrenciaDP;

	}

	@Override
	public List<RelatorioOcorrenciaDNDTO> listarPorCriterioExportar(RelatorioOcorrenciaDNDTO dto) {
		List<RelatorioOcorrenciaDNDTO> listaRelatorioOcorrenciaDNDTO = new ArrayList<>();
		List<RelatorioOcorrenciaDN> listaRelatorioOcorrencia = null;
		Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
		List<Long> ids = new ArrayList<>();

		if (usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_CLIENTE.get())
				|| usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_CLIENTE.get())) {
			
                        dto.setIdGrupoEconomicoList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));

			if (usuario.getIdPerfil().equals(CodigoPerfilEnum.OPERADOR_CLIENTE.get())) {
                            dto.setCnpjClienteList(usuario.getEmpresaList().stream().map(Empresa::getCnpj).collect(Collectors.toList()));
                            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
			}
			
			if (usuario.getIdPerfil().equals(CodigoPerfilEnum.MASTER_CLIENTE.get())) {
                            dto.setIdGrupoEconomicoOutrasEmpresasList(usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList()));
                            dto.setIdGrupoEconomico(null);
			}

			Specification<RelatorioOcorrenciaDN> specs = RelatorioOcorrenciaDnSpecification.byCriterio(dto);

			listaRelatorioOcorrencia = relatorioOcorrenciaDnRepository.findAll(specs);
			if (!dto.getFarois().isEmpty() || null != dto.getFarois()) {
				for (RelatorioOcorrenciaDN relatorioOcorrenciaDN : listaRelatorioOcorrencia) {

					RelatorioOcorrenciaDNDTO dto2 = converter.convert(relatorioOcorrenciaDN);

					for (String f : dto.getFarois()) {
						if (f.equals(dto2.getFarol())) {
							ids.add(relatorioOcorrenciaDN.getRow_number());
						}
					}
				}

			}
			
			Specification<RelatorioOcorrenciaDN> specsF = RelatorioOcorrenciaDnSpecification.byCriterioIds(dto, ids);

			
			if (!ids.isEmpty()) {
				listaRelatorioOcorrencia = relatorioOcorrenciaDnRepository.findAll(specsF);
			} else {
				listaRelatorioOcorrencia = relatorioOcorrenciaDnRepository.findAll(specs);
			}

			for (RelatorioOcorrenciaDN relatorioOcorrenciaDN : listaRelatorioOcorrencia) {
				listaRelatorioOcorrenciaDNDTO.add(converter.convert(relatorioOcorrenciaDN));
			}

		}

		return listaRelatorioOcorrenciaDNDTO;

	}

}
