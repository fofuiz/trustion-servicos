package br.com.accesstage.trustion.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.DadosBancarios;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IDadosBancariosRepository;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.accesstage.trustion.dto.ExtratoElegivelDTO;
import br.com.accesstage.trustion.dto.ListaBancoDTO;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.ExtratoElegivelConverter;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.ExtratoElegivel;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.repository.criteria.ExtratoElegivelSpecification;
import br.com.accesstage.trustion.repository.interfaces.IExtratoElegivelRepository;
import br.com.accesstage.trustion.repository.interfaces.IOcorrenciaRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IConciliadoService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IExtratoElegivelService;
import br.com.accesstage.trustion.service.interfaces.IListaBancoService;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoD1Service;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@Service
public class ExtratoElegivelService implements IExtratoElegivelService {

    @Autowired
    private IExtratoElegivelRepository extratoElegivelRepository;
    
    @Autowired
    private IDadosBancariosRepository dadosBancariosRepository;

    @Autowired
    private IConciliadoService conciliadoService;

    @Autowired
    private IListaBancoService listaBancoService;

    @Autowired
    private IRelatorioAnaliticoCreditoD1Service relatorioAnaliticoCreditoD1Service;

    @Autowired
    private IRelatorioAnaliticoCreditoService relatorioAnaliticoCreditoService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IModeloNegocioService modeloNegocioService;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IOcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;
    
    @Log
    private Logger LOG;

    @Override
    public List<ExtratoElegivelDTO> listarPorIdRelatorioAnaliticoD1(Long idRelatorioAnaliticoD1) throws Exception {

        RelatorioAnaliticoCreditoD1DTO dto = relatorioAnaliticoCreditoD1Service.pesquisar(idRelatorioAnaliticoD1);

        ConciliadoDTO conciliadoDTO = conciliadoService.pesquisar(dto.getIdConciliacao());

        List<ExtratoElegivelDTO> dtosListados = new ArrayList<>();

        for (ExtratoElegivel extratoElegivel : extratoElegivelRepository.findAllByIdConciliacao(conciliadoDTO.getIdConciliado())) {

            ExtratoElegivelDTO extratoElegivelDTO = ExtratoElegivelConverter.paraDTO(extratoElegivel);

            ListaBancoDTO listaBancoDTO = listaBancoService.pesquisarPorCodigoBanco(extratoElegivelDTO.getCodigoBanco());

            extratoElegivelDTO.setBanco(listaBancoDTO.getDescricao());

            dtosListados.add(extratoElegivelDTO);
        }

        return dtosListados;
    }

    @Override
    public List<ExtratoElegivelDTO> listarPorIdRelatorioAnaliticoD0(Long idRelatorioAnaliticoD0) throws Exception {

    	LOG.info(">> idRelatorioAnaliticoD0: " + idRelatorioAnaliticoD0);
        RelatorioAnaliticoCreditoDTO dto = relatorioAnaliticoCreditoService.pesquisar(idRelatorioAnaliticoD0);
        
        if (null != dto) {
        	LOG.info(">> dto: " + dto);
        	LOG.info(">> dto.getIdConciliacao(): " + dto.getIdConciliacao());
        }

        ConciliadoDTO conciliadoDTO = conciliadoService.pesquisar(dto.getIdConciliacao());
        
        if (null != conciliadoDTO) {
        	LOG.info(">> conciliadoDTO: " + conciliadoDTO);
        	LOG.info(">> conciliadoDTO.toString(): " + conciliadoDTO.toString());
        }

        List<ExtratoElegivelDTO> dtosListados = new ArrayList<>();

        for (ExtratoElegivel extratoElegivel : extratoElegivelRepository.findAllByIdConciliacao(conciliadoDTO.getIdConciliado())) {
        	
            if (null != extratoElegivel) {
            	LOG.info(">> extratoElegivel.getIdExtElegivel(): " + extratoElegivel.getIdExtElegivel());
            }

            ExtratoElegivelDTO extratoElegivelDTO = ExtratoElegivelConverter.paraDTO(extratoElegivel);

            ListaBancoDTO listaBancoDTO = listaBancoService.pesquisarPorCodigoBanco(extratoElegivelDTO.getCodigoBanco());
            
            if (null != listaBancoDTO) {
            	LOG.info(">> listaBancoDTO: " + listaBancoDTO.getIdListaBanco());
            }

            extratoElegivelDTO.setBanco(listaBancoDTO.getDescricao());

            dtosListados.add(extratoElegivelDTO);
        }

        return dtosListados;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void desconciliar(Long idConciliacao) throws Exception {

        ConciliadoDTO conciliadoDTO = conciliadoService.pesquisar(idConciliacao);

        RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO = relatorioAnaliticoCreditoD1Service.pesquisarPorIdConciliacao(conciliadoDTO.getIdConciliado());

        List<ExtratoElegivel> extratoElegivel = extratoElegivelRepository.findAllByIdConciliacao(conciliadoDTO.getIdConciliado());

        List<ExtratoElegivel> extratoElegivelDeletado = new ArrayList<>();

        if (null != relatorioAnaliticoCreditoD1DTO.getIdOcorrencia()) {

            List<Ocorrencia> mesclas = ocorrenciaRepository.findAllByIdOcorrenciaMescla(relatorioAnaliticoCreditoD1DTO.getIdOcorrencia());

            if (!mesclas.isEmpty()) {
                throw new BadRequestResponseException(Mensagem.get("msg.nao.foi.possivel.desconciliar.mescla", new Object[]{""}));
            }
        }

        if (extratoElegivel.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"extrato"}));
        }

        relatorioAnaliticoCreditoD1Service.alterarStatusConciliacao(relatorioAnaliticoCreditoD1DTO, StatusConciliacaoEnum.NAO_CONCILIADO);

        extratoElegivel.forEach(extratoElegivel1 -> {
            extratoElegivel1.setIdConciliacao(null);
            extratoElegivelDeletado.add(extratoElegivel1);
        });

        extratoElegivelRepository.save(extratoElegivelDeletado);
        conciliadoService.deletar(conciliadoDTO.getIdConciliado());

        auditoriaService.registrar(Mensagem.get("msg.auditoria.nao.conciliado", new Object[]{relatorioAnaliticoCreditoD1DTO.getGtv()}), relatorioAnaliticoCreditoD1DTO.getIdOcorrencia());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void desconciliarD0(Long idConciliacao) throws Exception {

        ConciliadoDTO conciliadoDTO = conciliadoService.pesquisar(idConciliacao);

        RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO = relatorioAnaliticoCreditoService.pesquisarPorIdConciliacao(conciliadoDTO.getIdConciliado());

        List<ExtratoElegivel> extratoElegivel = extratoElegivelRepository.findAllByIdConciliacao(conciliadoDTO.getIdConciliado());

        List<ExtratoElegivel> extratoElegivelDeletado = new ArrayList<>();

        if (null != relatorioAnaliticoCreditoDTO.getIdOcorrencia()) {

            List<Ocorrencia> mesclas = ocorrenciaRepository.findAllByIdOcorrenciaMescla(relatorioAnaliticoCreditoDTO.getIdOcorrencia());

            if (!mesclas.isEmpty()) {
                throw new BadRequestResponseException(Mensagem.get("msg.nao.foi.possivel.desconciliar.mescla", new Object[]{""}));
            }
        }

        if (extratoElegivel.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.desconciliar", new Object[]{"extrato"}));
        }

        relatorioAnaliticoCreditoService.alterarStatusConciliacao(relatorioAnaliticoCreditoDTO, StatusConciliacaoEnum.NAO_CONCILIADO);

        extratoElegivel.forEach(extratoElegivel1 -> {
            extratoElegivel1.setIdConciliacao(null);
            extratoElegivelDeletado.add(extratoElegivel1);
        });

        extratoElegivelRepository.save(extratoElegivelDeletado);
        conciliadoService.deletar(conciliadoDTO.getIdConciliado());

        auditoriaService.registrar(Mensagem.get("msg.auditoria.nao.conciliado.d0", new Object[]{relatorioAnaliticoCreditoDTO.getCodigoFechamento()}), relatorioAnaliticoCreditoDTO.getIdOcorrencia());

    }

    @Override
    public List<ExtratoElegivelDTO> pesquisarExtratosParaConciliacao(Long idRelatorioAnaliticoD1) throws Exception {

        RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO = relatorioAnaliticoCreditoD1Service.pesquisar(idRelatorioAnaliticoD1);

        EmpresaDTO empresaDTO = empresaService.pesquisar(relatorioAnaliticoCreditoD1DTO.getIdEmpresa());

        ModeloNegocioDTO modeloNegocioDTO = modeloNegocioService.pesquisar(empresaDTO.getEmpresaModeloNegocios().get(0).getIdModeloNegocio());

        List<DadosBancarios> listaDadosBancarios = dadosBancariosRepository.findAllByIdEmpresa(empresaDTO.getIdEmpresa());

        Specification<ExtratoElegivel> specs1 = ExtratoElegivelSpecification.byCriterioComRangeValorD1(relatorioAnaliticoCreditoD1DTO, modeloNegocioDTO,listaDadosBancarios);

        List<ExtratoElegivel> entidadesPesquisadas = extratoElegivelRepository.findAll(specs1);

        if (entidadesPesquisadas.isEmpty()) {
            Specification<ExtratoElegivel> specs2 = ExtratoElegivelSpecification.byCriterioSemRangeValorD1(relatorioAnaliticoCreditoD1DTO, modeloNegocioDTO,listaDadosBancarios);
            entidadesPesquisadas = extratoElegivelRepository.findAll(specs2);
        }

        List<ExtratoElegivelDTO> dtosListados = new ArrayList<>();

        for (ExtratoElegivel extratoElegivel : entidadesPesquisadas) {

            ExtratoElegivelDTO extratoElegivelDTO = ExtratoElegivelConverter.paraDTO(extratoElegivel);

            ListaBancoDTO listaBancoDTO = listaBancoService.pesquisarPorCodigoBanco(extratoElegivelDTO.getCodigoBanco());

            extratoElegivelDTO.setBanco(listaBancoDTO.getDescricao());

            dtosListados.add(extratoElegivelDTO);
        }

        return dtosListados;
    }

    @Override
    public List<ExtratoElegivelDTO> pesquisarExtratosParaConciliacaoD0(Long idRelatorioAnalitcoD0) throws Exception {

        RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO = relatorioAnaliticoCreditoService.pesquisar(idRelatorioAnalitcoD0);

        EmpresaDTO empresaDTO = empresaService.pesquisar(relatorioAnaliticoCreditoDTO.getIdEmpresa());

        ModeloNegocioDTO modeloNegocioDTO = modeloNegocioService.pesquisar(empresaDTO.getEmpresaModeloNegocios().get(0).getIdModeloNegocio());

        if (!CodigoTipoCreditoEnum.CREDITOD0.getId().equals(modeloNegocioDTO.getIdTipoCredito())) {
            throw new BadRequestResponseException(Mensagem.get("msg.modelo.negocio.aceito", new Object[]{"D+0"}));
        }

        Specification<ExtratoElegivel> specs1 = ExtratoElegivelSpecification.byCriterioComRangeValorD0(relatorioAnaliticoCreditoDTO);

        List<ExtratoElegivel> entidadesPesquisadas = extratoElegivelRepository.findAll(specs1);

        if (entidadesPesquisadas.isEmpty()) {
            Specification<ExtratoElegivel> specs2 = ExtratoElegivelSpecification.byCriterioSemRangeValorD0(relatorioAnaliticoCreditoDTO);
            entidadesPesquisadas = extratoElegivelRepository.findAll(specs2);
        }

        List<ExtratoElegivelDTO> dtosListados = new ArrayList<>();

    	List<DadosBancarios> lstDadosBancarios = null;
		lstDadosBancarios = dadosBancariosRepository.findAllByIdEmpresa(relatorioAnaliticoCreditoDTO.getIdEmpresa());

		for (DadosBancarios dadosBancarios : lstDadosBancarios) {

			for (ExtratoElegivel extratoElegivel : entidadesPesquisadas) {

				if ((dadosBancarios.getAgencia().intValue() == extratoElegivel.getAgencia().intValue())
						&& (dadosBancarios.getConta().intValue() == extratoElegivel.getConta().intValue())
						&& (dadosBancarios.getConta().intValue() == extratoElegivel.getConta().intValue())) {

					ExtratoElegivelDTO extratoElegivelDTO = ExtratoElegivelConverter.paraDTO(extratoElegivel);

					ListaBancoDTO listaBancoDTO = listaBancoService.pesquisarPorCodigoBanco(extratoElegivelDTO.getCodigoBanco());

					extratoElegivelDTO.setBanco(listaBancoDTO.getDescricao());

					dtosListados.add(extratoElegivelDTO);

				}
			}

		}

        return dtosListados;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void conciliar(Long idRelatorioAnaliticoD1, List<ExtratoElegivelDTO> extratoElegivelDTOS) throws Exception {

        RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO = relatorioAnaliticoCreditoD1Service.pesquisar(idRelatorioAnaliticoD1);

        if (relatorioAnaliticoCreditoD1DTO.getIdConciliacao() != null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.conciliado", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        List<Long> idsExtratos = new ArrayList<>();

        extratoElegivelDTOS.forEach(extratoElegivelDTO -> idsExtratos.add(extratoElegivelDTO.getIdExtElegivel()));

        List<ExtratoElegivel> extratosElegiveis = extratoElegivelRepository.findAllByIdExtElegivelIn(idsExtratos);

        if (extratosElegiveis.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"extrato"}));
        }

        for (ExtratoElegivel extratoElegivel : extratosElegiveis) {
            if (extratoElegivel.getIdConciliacao() != null) {
                throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.conciliado", new Object[]{"extrato"}));
            }
        }

        ConciliadoDTO conciliadoDTO = new ConciliadoDTO();
        conciliadoDTO.setCnpj(relatorioAnaliticoCreditoD1DTO.getCnpj());
        conciliadoDTO.setDataLancamento(extratosElegiveis.get(extratosElegiveis.size() - 1).getDataLancamento());
        conciliadoDTO.setDataConciliacao(Calendar.getInstance().getTime());
        conciliadoDTO.setDataCriacao(Calendar.getInstance().getTime());
        conciliadoDTO.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());

        ConciliadoDTO conciliadoDTOCriado = conciliadoService.criar(conciliadoDTO);

        relatorioAnaliticoCreditoD1DTO.setIdConciliacao(conciliadoDTOCriado.getIdConciliado());

        BigDecimal total = new BigDecimal("0");

        for (int i = 0; i < extratosElegiveis.size(); i++) {
            extratosElegiveis.get(i).setIdConciliacao(conciliadoDTOCriado.getIdConciliado());
            total = total.add(extratosElegiveis.get(i).getValorLancamento());
        }

        relatorioAnaliticoCreditoD1DTO.setValorCredito(total);
        relatorioAnaliticoCreditoD1DTO.setDataCredito(extratosElegiveis.get(extratosElegiveis.size() - 1).getDataLancamento());

        relatorioAnaliticoCreditoD1Service.alterarStatusConciliacao(relatorioAnaliticoCreditoD1DTO, StatusConciliacaoEnum.CONCILIADO);

        extratoElegivelRepository.save(extratosElegiveis);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.conciliado", new Object[]{relatorioAnaliticoCreditoD1DTO.getGtv()}), relatorioAnaliticoCreditoD1DTO.getIdOcorrencia());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void conciliarD0(Long idRelatorioAnaliticoD0, List<ExtratoElegivelDTO> idExtratos) throws Exception {

        RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO = relatorioAnaliticoCreditoService.pesquisar(idRelatorioAnaliticoD0);

        if (relatorioAnaliticoCreditoDTO.getIdConciliacao() != null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.conciliado", new Object[]{UTF8.relatorio + " " + UTF8.analitico + " de " + UTF8.credito}));
        }

        List<Long> idsExtratos = new ArrayList<>();

        idExtratos.forEach(extratoElegivelDTO -> idsExtratos.add(extratoElegivelDTO.getIdExtElegivel()));

        List<ExtratoElegivel> extratosElegiveis = extratoElegivelRepository.findAllByIdExtElegivelIn(idsExtratos);

        if (extratosElegiveis.isEmpty()) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"extrato"}));
        }

        for (ExtratoElegivel extratoElegivel : extratosElegiveis) {
            if (extratoElegivel.getIdConciliacao() != null) {
                throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.conciliado", new Object[]{"extrato"}));
            }
        }

        Optional<ExtratoElegivel> ultimaData = extratosElegiveis
                .stream()
                .sorted((v1, v2) -> Long.compare(v2.getDataLancamento().getTime(), v1.getDataLancamento().getTime()))
                //.peek(extratoElegivel -> System.out.println(extratoElegivel.getDataLancamento()))
                .findFirst();

        ConciliadoDTO conciliadoDTO = new ConciliadoDTO();
        conciliadoDTO.setCnpj(relatorioAnaliticoCreditoDTO.getCnpj());
        ultimaData.ifPresent(v -> conciliadoDTO.setDataLancamento(v.getDataLancamento()));
        ultimaData.orElseThrow(() -> new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"extrato"})));
        conciliadoDTO.setDataConciliacao(Calendar.getInstance().getTime());
        conciliadoDTO.setDataCriacao(Calendar.getInstance().getTime());
        conciliadoDTO.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());

        ConciliadoDTO conciliadoDTOCriado = conciliadoService.criar(conciliadoDTO);

        relatorioAnaliticoCreditoDTO.setIdConciliacao(conciliadoDTOCriado.getIdConciliado());

        BigDecimal total = new BigDecimal("0");

        for (int i = 0; i < extratosElegiveis.size(); i++) {
            extratosElegiveis.get(i).setIdConciliacao(conciliadoDTOCriado.getIdConciliado());
            total = total.add(extratosElegiveis.get(i).getValorLancamento());
        }

        relatorioAnaliticoCreditoDTO.setValorCredito(total);
        ultimaData.ifPresent(extratoElegivel -> relatorioAnaliticoCreditoDTO.setDataCredito(extratoElegivel.getDataLancamento()));

        relatorioAnaliticoCreditoService.alterarStatusConciliacao(relatorioAnaliticoCreditoDTO, StatusConciliacaoEnum.CONCILIADO);

        extratoElegivelRepository.save(extratosElegiveis);
        auditoriaService.registrar(Mensagem.get("msg.auditoria.conciliado.d0", new Object[]{relatorioAnaliticoCreditoDTO.getCodigoFechamento()}), relatorioAnaliticoCreditoDTO.getIdOcorrencia());

    }
}
