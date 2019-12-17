package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.asconciliacao.model.DetalheConciliacaoCartaoEntity;
import br.com.accesstage.trustion.asconciliacao.model.ResumoConciliacaoCartaoEntity;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroTelasConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.ResultadoConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaNovaConciliacaoDTO;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelasConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.repository.impl.ConciliacaoCartaoRepository;
import br.com.accesstage.trustion.repository.mapper.DetalheConciliacaoCartaoEntityMapper;
import br.com.accesstage.trustion.service.interfaces.IConciliacaoCartaoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConciliacaoCartaoService implements IConciliacaoCartaoService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private ConciliacaoCartaoRepository conciliacaoCartaoRepository;

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheExtratoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {
        filtro.setStatusConciliado("N");
        return pesquisarRegistrosDetalheExtrato(filtro);
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheCartaoNaoConciliados(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {
        filtro.setStatusConciliado("N");
        return pesquisarRegistrosDetalheCartao(filtro);
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheExtrato(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {
        LOGGER.info(">>ConciliacaoCartaoService.pesquisarRegistrosDetalheExtrato");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = null;

        try {
            resultado = converterEntitysDetalheParaVOs(conciliacaoCartaoRepository.pesquisarRegistrosExtratoNaoConciliados(filtro));

        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoService.pesquisarRegistrosDetalheExtrato - Erro ao pesquisar registros.", e.getMessage(), e);
        }

        LOGGER.info("<<ConciliacaoCartaoService.pesquisarRegistrosDetalheExtrato");

        return resultado;
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalheCartao(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {
        LOGGER.info(">>ConciliacaoCartaoService.pesquisarRegistrosDetalheCartao");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = null;

        try {
            resultado = converterEntitysDetalheParaVOs(conciliacaoCartaoRepository.pesquisarRegistrosCartaoNaoConciliados(filtro));

        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoService.pesquisarRegistrosDetalheCartao - Erro ao pesquisar registros.");
            LOGGER.error(e.getMessage());
            throw e;
        }

        LOGGER.info("<<ConciliacaoCartaoService.pesquisarRegistrosDetalheCartao");

        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoConciliacaoCartaoResumoDTO> pesquisarRegistrosResumo(FiltroTelasConciliacaoCartaoResumoDTO filtro) throws Exception {

        LOGGER.info(">>ConciliacaoCartaoServiceImpl.pesquisarRegistrosResumo");
        List<ResultadoConciliacaoCartaoResumoDTO> resultado = null;

        List<ResumoConciliacaoCartaoEntity> resultadoConciliado = null;
        List<ResumoConciliacaoCartaoEntity> resultadoExtratoNaoConciliado = null;
        List<ResumoConciliacaoCartaoEntity> resultadoCartaoNaoConciliado = null;

        try {
            resultadoConciliado = conciliacaoCartaoRepository.pesquisarRegistrosResumoConciliados(filtro);

            filtro.setStatusConciliado("N");

            resultadoExtratoNaoConciliado = conciliacaoCartaoRepository.buscarValoresExtratoNaoConciliados(filtro);

            resultadoCartaoNaoConciliado = conciliacaoCartaoRepository.buscarValoresCartaoNaoConciliados(filtro);

            resultado = converterEntitysResumoParaVOs(resultadoConciliado, resultadoExtratoNaoConciliado, resultadoCartaoNaoConciliado);
        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoServiceImpl.pesquisarRegistrosResumo - Erro ao pesquisar registros.", e.getMessage(), e);
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.pesquisarRegistrosResumo");

        return resultado;
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> pesquisarRegistrosDetalhe(FiltroTelasConciliacaoCartaoDetalheDTO filtro) throws Exception {
        LOGGER.info(">>ConciliacaoCartaoServiceImpl.pesquisarRegistrosDetalhe");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = null;

        try {
            resultado = converterEntitysDetalheParaVOs(conciliacaoCartaoRepository.pesquisarRegistrosDetalhe(filtro));
        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoServiceImpl.pesquisarRegistrosDetalhe - Erro ao pesquisar registros.");
            LOGGER.error(e.getMessage());
            throw e;
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.pesquisarRegistrosDetalhe");

        return resultado;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void conciliar(FiltroTelaNovaConciliacaoDTO filtro) {
        LOGGER.info(">>ConciliacaoCartaoServiceImpl.conciliar");

        try {
            DetalheConciliacaoCartaoEntityMapper mapper = new DetalheConciliacaoCartaoEntityMapper();
            LOGGER.info(">>ConciliacaoCartaoServiceImpl.converterVOsDetalheParaEntitys");
            List<DetalheConciliacaoCartaoEntity> registrosCartoes = mapper.converterVOsDetalheParaEntitys(filtro.getListaMovCartao());
            List<DetalheConciliacaoCartaoEntity> registrosExtratos = mapper.converterVOsDetalheParaEntitys(filtro.getListaMovExtrato());

            LOGGER.info("<<ConciliacaoCartaoServiceImpl.converterVOsDetalheParaEntitys");
            if (registrosExtratos.isEmpty() || registrosCartoes.isEmpty()) {
                LOGGER.error(">>ConciliacaoCartaoServiceImpl.conciliar - Erro ao conciliar, a lista de extrato precisa conter um registro e ao menos existir um registro associado de cartao.");

                throw new Exception("Erro na conciliacao manual.");
            }

            for (DetalheConciliacaoCartaoEntity registro : registrosCartoes) {
                registro.setStatusConciliado("S");
                registro.setStatus("CONCILIADO");
                registro.setTipoConciliacao("MANUAL");
                registro.setDataConciliado(new Date());
                registro.setStatusPreProcessamento("S");

                if (registrosCartoes.get(0).getNroGrupoConciliado() == 0 || registrosCartoes.get(0).getNroGrupoConciliado() == null) {
                    registro.setNroGrupoConciliado(conciliacaoCartaoRepository.obterIdRegistrosConciliados());
                } else {
                    registro.setNroGrupoConciliado(registrosCartoes.get(0).getNroGrupoConciliado());
                }
                conciliacaoCartaoRepository.atualizarConciliacaoCartaoDetalhe(registro);
            }

            for (DetalheConciliacaoCartaoEntity registro : registrosExtratos) {
                registro.setStatusConciliado("S");
                registro.setStatus("CONCILIADO");
                registro.setTipoConciliacao("MANUAL");
                registro.setDataConciliado(new Date());
                registro.setStatusPreProcessamento("S");
                registro.setMotivoConciliacao(filtro.getDescDetalheOperacao());
                registro.setNroGrupoConciliado(registrosCartoes.get(0).getNroGrupoConciliado());

                conciliacaoCartaoRepository.atualizarConciliacaoExtratoDetalhe(registro);
            }
        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoServiceImpl.conciliar - Erro ao conciliar.", e.getMessage(), e);
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.conciliar");
    }

    /**
     * metodo para converter DetalheConciliacaoCartaoEntity para
     * ResultadoConciliacaoCartaoDetalheVO
     *
     * @param listaEntitys DetalheConciliacaoCartaoEntity
     * @return lista ResultadoConciliacaoCartaoDetalheVO.
     */
    List<ResultadoConciliacaoCartaoDetalheDTO> converterEntitysDetalheParaVOs(List<DetalheConciliacaoCartaoEntity> listaEntitys) {

        LOGGER.info(">>ConciliacaoCartaoServiceImpl.converterEntitysDetalheParaVOs");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = new ArrayList<ResultadoConciliacaoCartaoDetalheDTO>();

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);

        try {

            for (DetalheConciliacaoCartaoEntity entity : listaEntitys) {

                ResultadoConciliacaoCartaoDetalheDTO vo = new ResultadoConciliacaoCartaoDetalheDTO();

                vo.setId(entity.getId());
                vo.setCodOperadora(entity.getCodOperadora());
                vo.setCodEmpresa(entity.getCodEmpresa());
                vo.setCodigoGrupoConciliacao(entity.getCodGrupoOperacao());
                vo.setCodOperadora(entity.getCodOperadora());
                vo.setCodigoMovimento(entity.getCodMovimento());
                vo.setNomeOperadora(entity.getNomeOperadora());
                vo.setNomeProduto(entity.getNomeProduto());
                vo.setNumeroBanco(entity.getNumeroBanco());
                vo.setNumeroAgencia(entity.getNumeroAgencia());
                vo.setNumeroConta(entity.getNumeroConta());
                vo.setDataExtrPagamento(entity.getDataExtrPagamento());
                vo.setValor(entity.getValorPagamento());
                vo.setStatus(entity.getStatus());
                vo.setTipo(entity.getTipoConciliacao());
                vo.setNomeArquivoOrigem(entity.getNomeArquivoOrigem());
                vo.setHashValue(entity.getHashValue());
                vo.setNroGrupoConciliado(entity.getNroGrupoConciliado());
                vo.setDscExtrHistorico(entity.getDscExtrHistorico());
                vo.setDscExtrDocumento(entity.getDscExtrDocumento());

                resultado.add(vo);
            }
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoServiceImpl.converterEntitysDetalheParaVOs - Erro ao converter conciliacao cartao detalhe.", e.getMessage(), e);
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.converterEntitysDetalheParaVOs");

        return resultado;
    }

    /**
     * metodo para converter ResumoConciliacaoCartaoEntity para
     * ResultadoConciliacaoCartaoVO
     *
     * @param listaConciliados ResumoConciliacaoCartaoEntity
     * @return lista ResultadoConciliacaoCartaoResumoVO.
     */
    private List<ResultadoConciliacaoCartaoResumoDTO> converterEntitysResumoParaVOs(List<ResumoConciliacaoCartaoEntity> listaConciliados,
            List<ResumoConciliacaoCartaoEntity> listaExtratoNaoConciliados, List<ResumoConciliacaoCartaoEntity> listaCartaoNaoConciliados) throws Exception {

        LOGGER.info(">>ConciliacaoCartaoServiceImpl.converterEntitysResumoParaVOs");
        List<ResultadoConciliacaoCartaoResumoDTO> resultado = new ArrayList<ResultadoConciliacaoCartaoResumoDTO>();

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);

        try {
            for (ResumoConciliacaoCartaoEntity entity : listaConciliados) {
                ResultadoConciliacaoCartaoResumoDTO vo = new ResultadoConciliacaoCartaoResumoDTO();

                vo.setCodOperadora(entity.getCodOperadora());
                vo.setCodOperadora(entity.getCodOperadora());
                vo.setNomeOperadora(entity.getNomeOperadora());
                vo.setNumeroBanco(entity.getNumeroBanco());
                vo.setNumeroAgencia(entity.getNumeroAgencia());
                vo.setNumeroConta(entity.getNumeroConta());
                vo.setDataExtrPagamento(entity.getDataExtrPagamento());
                vo.setValorPagamentoConciliado(entity.getValorPagamento());
                vo.setValorExtratoConciliado(entity.getValorExtrato());
                vo.setStatusConciliado(entity.getStatusConciliado());

                resultado.add(vo);
            }

            for (ResumoConciliacaoCartaoEntity entity : listaExtratoNaoConciliados) {
                ResultadoConciliacaoCartaoResumoDTO voSelecionado = null;

                boolean registroEncontrado = false;

                for (ResultadoConciliacaoCartaoResumoDTO voProcura : resultado) {
                    registroEncontrado = voProcura.getNumeroBanco().equals(entity.getNumeroBanco())
                            && voProcura.getNumeroAgencia().equals(entity.getNumeroAgencia())
                            && voProcura.getNumeroConta().equals(entity.getNumeroConta())
                            && voProcura.getCodOperadora().equals(entity.getCodOperadora())
                            && voProcura.getDataExtrPagamento().equals(entity.getDataExtrPagamento());

                    if (registroEncontrado) {
                        voSelecionado = voProcura;

                        break;
                    }
                }

                if (registroEncontrado) {
                    if (voSelecionado.getValorExtratoNaoConciliado() != null && voSelecionado.getValorExtratoNaoConciliado() > 0.0) {
                        voSelecionado.addValorExtratoNaoConciliado(entity.getValorExtrato());
                    } else {
                        voSelecionado.setValorExtratoNaoConciliado(entity.getValorExtrato());
                    }
                } else {
                    voSelecionado = new ResultadoConciliacaoCartaoResumoDTO();

                    voSelecionado.setCodOperadora(entity.getCodOperadora());
                    voSelecionado.setCodOperadora(entity.getCodOperadora());
                    voSelecionado.setNomeOperadora(entity.getNomeOperadora());
                    voSelecionado.setNumeroBanco(entity.getNumeroBanco());
                    voSelecionado.setNumeroAgencia(entity.getNumeroAgencia());
                    voSelecionado.setNumeroConta(entity.getNumeroConta());
                    voSelecionado.setDataExtrPagamento(entity.getDataExtrPagamento());
                    voSelecionado.setValorPagamentoNaoConciliado(entity.getValorPagamento());
                    voSelecionado.setValorExtratoNaoConciliado(entity.getValorExtrato());

                    resultado.add(voSelecionado);
                }
            }

            for (ResumoConciliacaoCartaoEntity entity : listaCartaoNaoConciliados) {
                ResultadoConciliacaoCartaoResumoDTO voSelecionado = null;

                String contaSemDigito = "";

                if (entity.getNumeroConta() != null) {
                    contaSemDigito = entity.getNumeroConta();
                }

                boolean registroEncontrado = false;

                for (ResultadoConciliacaoCartaoResumoDTO voProcura : resultado) {
                    registroEncontrado = voProcura.getNumeroBanco().equals(entity.getNumeroBanco())
                            && voProcura.getNumeroAgencia().equals(entity.getNumeroAgencia())
                            && voProcura.getNumeroConta().equals(contaSemDigito)
                            && voProcura.getCodOperadora().equals(entity.getCodOperadora())
                            && voProcura.getDataExtrPagamento().equals(entity.getDataExtrPagamento());

                    if (registroEncontrado) {
                        voSelecionado = voProcura;

                        break;
                    }
                }

                if (registroEncontrado) {
                    if (voSelecionado.getValorPagamentoNaoConciliado() != null && voSelecionado.getValorPagamentoNaoConciliado() > 0.0) {
                        voSelecionado.addValorPagamentoNaoConciliado(entity.getValorPagamento());
                    } else {
                        voSelecionado.setValorPagamentoNaoConciliado(entity.getValorPagamento());
                    }
                } else {
                    voSelecionado = new ResultadoConciliacaoCartaoResumoDTO();

                    voSelecionado.setCodOperadora(entity.getCodOperadora());
                    voSelecionado.setCodOperadora(entity.getCodOperadora());
                    voSelecionado.setNomeOperadora(entity.getNomeOperadora());
                    voSelecionado.setNumeroBanco(entity.getNumeroBanco());
                    voSelecionado.setNumeroAgencia(entity.getNumeroAgencia());
                    voSelecionado.setNumeroConta(contaSemDigito);
                    voSelecionado.setDataExtrPagamento(entity.getDataExtrPagamento());
                    voSelecionado.setValorPagamentoNaoConciliado(entity.getValorPagamento());
                    voSelecionado.setValorExtratoNaoConciliado(entity.getValorExtrato());

                    resultado.add(voSelecionado);
                }
            }
        } catch (Exception e) {
            LOGGER.error("ConciliacaoCartaoServiceImpl.converterEntitysResumoParaVOs - Erro ao converter conciliacao cartao resumo.", e.getMessage(), e);
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.converterEntitysResumoParaVOs");

        return resultado;
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> obterRegistrosExtratoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro)
            throws Exception {

        LOGGER.info(">>ConciliacaoCartaoService.obterRegistrosExtratoPorId");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = null;

        try {
            resultado = converterEntitysDetalheParaVOs(conciliacaoCartaoRepository.buscarRegistroExtratoPorId(filtro));

        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoService.obterRegistrosExtratoPorId - Erro ao pesquisar registros.");
            LOGGER.error(e.getMessage());
            throw e;
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.obterRegistrosExtratoPorId");

        return resultado;
    }

    @Override
    public List<ResultadoConciliacaoCartaoDetalheDTO> obterRegistrosCartaoPorId(FiltroTelasConciliacaoCartaoDetalheDTO filtro)
            throws Exception {

        LOGGER.info(">>ConciliacaoCartaoService.obterRegistrosCartaoPorId");
        List<ResultadoConciliacaoCartaoDetalheDTO> resultado = null;

        try {
            resultado = converterEntitysDetalheParaVOs(conciliacaoCartaoRepository.buscarRegistroCartaoPorId(filtro));

        } catch (Exception e) {
            LOGGER.error(">>ConciliacaoCartaoService.obterRegistrosCartaoPorId - Erro ao obter registro.");
            LOGGER.error(e.getMessage());
            throw e;
        }

        LOGGER.info("<<ConciliacaoCartaoServiceImpl.obterRegistrosCartaoPorId");

        return resultado;
    }
}
