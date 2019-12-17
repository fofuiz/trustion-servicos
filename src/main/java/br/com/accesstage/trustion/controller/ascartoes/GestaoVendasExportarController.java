package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.BaixaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroBaixaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IBaixaService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IEmpresaCaService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IGestaoVendasService;
import br.com.accesstage.trustion.util.Format;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import br.com.accesstage.trustion.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestaovendas/exportar")
public class GestaoVendasExportarController {

    private static final String SEMICOLON = ";";
    private static final Integer TPO_CONC_MULTI_ID = 2;

    private static final String MSG_ERRO_EXPORTACAO = "Erro ao exportar arquivo.";

    private final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private final DateFormat sdfHm = new SimpleDateFormat("yyyyMMddHHmm");
    private final DateFormat sdfBarra = new SimpleDateFormat("dd/MM/yyyy");

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Log
    private static Logger LOGGER;

    @Autowired
    private IEmpresaCaService empresaCaService;

    @Autowired
    private IBaixaService baixaService;

    @Autowired
    private IGestaoVendasService gestaoVendasService;

    /**
     * Exportação simples.
     *
     * @param filtro
     * @param response
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportar(FiltroBaixaDTO filtro, HttpServletResponse response) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        try {
            EmpresaCA empresa = getEmpresaCA(filtro.getListaEmpresas());
            filtro.setTpoConciliacao(empresa.getTpoConciliacao());

            Integer nroSeqArq = getArquivoSeq(empresa);
            String seqArq = Format.insereZerosEsquerda(nroSeqArq.toString(), 6);

            String fileNameDat = "ASCARTOES_CRD_" + seqArq + "_" + sdf.format(new Date()) + ".dat";

            String cnpj = Format.insereZerosEsquerda(empresa.getCnpj(), 14);
            String idCliente = cnpj.substring(0, 8) + cnpj.substring(12);

            List<BaixaDTO> baixaList;

            if (TPO_CONC_MULTI_ID.equals(filtro.getTpoConciliacao())) {
                baixaList = baixaService.consultaConcParcela(filtro);
            } else {
                baixaList = baixaService.consulta(filtro);
            }
            // Armazena o total de registros efetivamente incluído no arquivo
            Integer qtdeRegArquivo = 0;
            Integer somaRegistro;
            Long totalBruto = 0L;

            StringBuilder sb = new StringBuilder();

            // Cabecalho
            sb.append(0);
            sb.append("014");
            sb.append(sdf.format(new Date()));
            sb.append(sdf.format(filtro.getDataInicial()));
            sb.append(sdf.format(filtro.getDataFinal()));
            sb.append(Format.insereEspacosDireita("Veritas CRD", 13));
            sb.append(Format.insereEspacosDireita(StringUtils.EMPTY, 13));
            sb.append(Format.insereZerosEsquerda(nroSeqArq.toString(), 6));
            sb.append(Format.insereEspacosDireita(idCliente, 10));
            sb.append(Format.insereEspacosDireita(StringUtils.EMPTY, 170));

            for (BaixaDTO baixa : baixaList) {
                StringBuilder corpo = new StringBuilder(StringUtils.LF);

                // Inicializa a variável que identifica se registro entrou no arquivo de baixa
                somaRegistro = 0;

                if (baixa.getCodNatureza() == 1) {
                    // Parcela
                    // Verifica se registro foi conciliado
                    corpo.append(5);

                    if (baixa.getDtaVendaDt() != null) {
                        corpo.append(sdf.format(baixa.getDtaVendaDt()));
                    }
                    if (baixa.getDtaCreditoDt() != null) {
                        corpo.append(sdf.format(baixa.getDtaCreditoDt()));
                    }

                    corpo.append(Format.insereZerosEsquerda(baixa.getNroNsu().toString(), 12));
                    corpo.append(Format.insereEspacosDireita(baixa.getCodAutorizacao(), 12));
                    corpo.append(Format.insereZerosEsquerda(baixa.getNroPlano().toString(), 2));
                    corpo.append(Format.insereEspacosDireita(baixa.getIdtProduto(), 1));
                    corpo.append(Format.insereZerosEsquerda(baixa.getVlrBruto().toString(), 13));
                    corpo.append(Format.insereEspacosDireita(StringUtils.EMPTY, 61));

                    somaRegistro++;
                } else {
                    // Ajuste ou aluguel de POS
                    corpo.append(4);
                    corpo.append(baixa.getDtaCredito().toString());
                    corpo.append(Format.insereEspacosDireita(baixa.getIdtOperadora(), 1));

                    if (baixa.getCodNatureza() == 2) {
                        // Ajuste
                        corpo.append("AJU");
                    } else {
                        // Aluguel POS
                        corpo.append("CON");
                    }
                    corpo.append(Format.insereZerosEsquerda(baixa.getVlrBruto().toString(), 13));
                    // Inclusão de novos atributos para identificação da
                    // Transação (nsu,autorizacao,parcela,plano)
                    corpo.append(Format.insereZerosEsquerda(baixa.getNroNsu().toString(), 12));
                    corpo.append(Format.insereEspacosDireita(baixa.getCodAutorizacao(), 12));
                    // corpo.append(Format.insereEspacosDireita(baixa.getDdsCliente(), 50));
                    // Fim dos novos atributos
                    corpo.append(Format.insereEspacosDireita(StringUtils.EMPTY, 117));

                    somaRegistro++;
                }

                sb.append(corpo);

                // Soma totalizadores apenas se o registro foi incluído no arquivo
                if (somaRegistro == 1) {
                    totalBruto += baixa.getVlrBruto();
                    // totalComissao += baixa.getVlrComissao();
                    // totalTxAntecipacao += baixa.getVlrTxAntecipacao();
                    qtdeRegArquivo++;
                }
            }

            StringBuilder sbRodape = new StringBuilder(StringUtils.LF);
            sbRodape.append(9);
            sbRodape.append(Format.insereZerosEsquerda(qtdeRegArquivo.toString(), 6));
            sbRodape.append(Format.insereZerosEsquerda(totalBruto.toString(), 13));
            sbRodape.append(Format.insereEspacosDireita(StringUtils.EMPTY, 194));

            sb.append(sbRodape);

            carregarArquivo(response, sb, fileNameDat);

            // atualiza nro sequencial arquivo de baixas            
            empresa.setUltSeqArqBaixas(nroSeqArq);
            empresaCaService.merge(empresa);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_EXPORTACAO, e);
            e.printStackTrace(System.out);
        }
    }

    /**
     * Exportação de operadora.
     *
     * @param filtro
     * @param response
     */
    @GetMapping(value = "/operadora", produces = MediaType.TEXT_PLAIN_VALUE)
    public void exportarOperadora(FiltroGestaoVendaDTO filtro, HttpServletResponse response) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        String fileName = getNomeArquivoExportado(
                Mensagem.get("divergenciaview.exportar.divergencias"),
                filtro.getDataInicial(), filtro.getDataFinal()) + "-" + System.currentTimeMillis() + ".csv";
        try {
            EmpresaCA empresa = getEmpresaCA(filtro.getListaEmpresas());
            filtro.setStatusConciliacao(empresa.getTpoConciliacao());

            List<GestaoVendasDTO> divergencias = gestaoVendasService.consultaBandeira(filtro);

            /*
             * Cabecalho do arquivo
             */
            StringBuilder sb = new StringBuilder();
            sb.append(Mensagem.get("divergenciaview.table.data")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.loja")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.operadora")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.produto")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.plano")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.valor")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.nsu")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.autorizacaoSemAcento")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.id")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.status")).append(StringUtils.LF);

            for (GestaoVendasDTO gestaoVendasDTO : divergencias) {
                sb.append(String.format(Mensagem.get("formato.data.format"), gestaoVendasDTO.getData())).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getLoja() == null ? StringUtils.EMPTY : gestaoVendasDTO.getLoja()).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getDscOperadora()).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getProduto()).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getPlano()).append(SEMICOLON);
                sb.append(currencyFormat.format(gestaoVendasDTO.getValor())).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getNsu() == null ? StringUtils.EMPTY : gestaoVendasDTO.getNsu()).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getCodAutorizacao() == null ? StringUtils.EMPTY : gestaoVendasDTO.getCodAutorizacao()).append(SEMICOLON);
                sb.append(gestaoVendasDTO.getDscAreaCliente() == null ? StringUtils.EMPTY : gestaoVendasDTO.getDscAreaCliente()).append(SEMICOLON);

                String statusConciliado;
                if (gestaoVendasDTO.getStatusConciliacao() == 1) {
                    statusConciliado = Mensagem.get("gestao.vendas.conciliado");
                } else {
                    statusConciliado = Mensagem.get("gestao.vendas.nao.conciliado");
                }
                sb.append(statusConciliado == null ? StringUtils.EMPTY : statusConciliado).append(SEMICOLON);
                sb.append(StringUtils.LF);
            }

            carregarArquivo(response, sb, fileName);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception e) {
            String erro = Mensagem.get("divergenciaview.erroExportacao");
            LOGGER.error(erro);
            e.printStackTrace(System.out);
        }
    }

    /**
     * Exportação a partir do layout 2.
     *
     * @param filtro
     * @param response
     */
    @GetMapping(value = "/layout2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportarLayout2(FiltroBaixaDTO filtro, HttpServletResponse response) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        try {
            String fileName;

            EmpresaCA empresa = getEmpresaCA(filtro.getListaEmpresas());
            filtro.setTpoConciliacao(empresa.getTpoConciliacao());

            Integer nroSeqArq = getArquivoSeq(empresa);
            String seqArq = Format.insereZerosEsquerda(nroSeqArq.toString(), 6);

            fileName = "ASCARTOES_CRD_" + seqArq + "_" + sdf.format(new Date()) + ".dat";

            String cnpj = Format.insereZerosEsquerda(empresa.getCnpj(), 14);
            String idCliente = cnpj.substring(0, 8) + cnpj.substring(12);

            List<BaixaDTO> baixaList;

            if (TPO_CONC_MULTI_ID.compareTo(filtro.getTpoConciliacao()) == 0) {
                baixaList = baixaService.consultaLayout2ConcParcela(filtro);
            } else {
                baixaList = baixaService.consultaLayout2(filtro);
            }

            Integer nroSeqReg = 1;

            // Cabecalho
            StringBuilder sb = new StringBuilder("00");
            sb.append(Format.insereZerosEsquerda(nroSeqReg.toString(), 7));
            sb.append(Format.insereZerosEsquerda(idCliente, 15));
            sb.append(sdfHm.format(new Date()));
            sb.append(Format.insereEspacosDireita("Vendas Conciliadas", 25));
            sb.append("003");
            sb.append(Format.insereEspacosDireita(StringUtils.EMPTY, 285));

            for (BaixaDTO baixa : baixaList) {
                StringBuilder sbCorpo = new StringBuilder(StringUtils.LF);

                if (baixa.getCodNatureza() == 1) {
                    // Parcela
                    nroSeqReg++;

                    sbCorpo.append("05");
                    sbCorpo.append(Format.insereZerosEsquerda(nroSeqReg.toString(), 7));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroEstabelecimento(), 15));
                    sbCorpo.append(Format.insereEspacosDireita(baixa.getNmeLoja(), 30));
                    sbCorpo.append(Format.insereEspacosDireita(baixa.getDscLote(), 9));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroParcela().toString(), 2));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroPlano().toString(), 2));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroNsu().toString(), 15));
                    sbCorpo.append(Format.insereEspacosDireita(baixa.getCodAutorizacao(), 15));
                    sbCorpo.append(Format.insereEspacosDireita(baixa.getDscNumeroCartao(), 20));
                    sbCorpo.append(baixa.getDtaVenda().toString());
                    sbCorpo.append(baixa.getDtaCredito().toString());
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getVlrBruto().toString(), 15));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getVlrComissao().toString(), 15));
                    sbCorpo.append(Format.insereZerosEsquerda(StringUtils.EMPTY, 30));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getVlrLiquido().toString(), 15));
                    sbCorpo.append(Format.insereZerosEsquerda(StringUtils.EMPTY, 11));
                    sbCorpo.append("05");
                    sbCorpo.append("01");
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getDscProduto(), 3));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getDscOperadora(), 3));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroBanco().toString(), 3));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroAg().toString(), 6));
                    sbCorpo.append(Format.insereZerosEsquerda(baixa.getNroCC(), 20));
                    sbCorpo.append(Format.insereEspacosDireita(baixa.getIdConciliacao(), 50));
                    sbCorpo.append(Format.insereEspacosDireita(StringUtils.EMPTY, 41));
                }
                sb.append(sbCorpo);
            }

            StringBuilder sbRodape = new StringBuilder(StringUtils.LF);
            nroSeqReg++;
            sbRodape.append("99");
            sbRodape.append(Format.insereZerosEsquerda(nroSeqReg.toString(), 7));
            nroSeqReg++;
            sbRodape.append(Format.insereZerosEsquerda(nroSeqReg.toString(), 7));
            sbRodape.append(Format.insereEspacosDireita(StringUtils.EMPTY, 333));

            sb.append(sbRodape);

            carregarArquivo(response, sb, fileName);

            // atualiza nro sequencial arquivo de baixas
            empresa.setUltSeqArqBaixas(nroSeqArq);
            empresaCaService.merge(empresa);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_EXPORTACAO);
            e.printStackTrace(System.out);
        }
    }

    /**
     * Exportação CSV.
     *
     * @param filtro
     * @param response
     */
    @GetMapping(value = "/csv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportarCSV(FiltroBaixaDTO filtro, HttpServletResponse response) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        try {
            String fileName;

            EmpresaCA empresa = getEmpresaCA(filtro.getListaEmpresas());
            filtro.setTpoConciliacao(empresa.getTpoConciliacao());

            Integer nroSeqArq = getArquivoSeq(empresa);
            String seqArq = Format.insereZerosEsquerda(nroSeqArq.toString(), 6);

            fileName = "ASCARTOES_CRD_" + seqArq + "_" + sdf.format(new Date()) + ".csv";

            List<BaixaDTO> baixaList;

            if (TPO_CONC_MULTI_ID.compareTo(filtro.getTpoConciliacao()) == 0) {
                baixaList = baixaService.consultaLayoutCSVConcParcela(filtro);
            } else {
                baixaList = baixaService.consultaLayoutCSV(filtro);
            }

            // Header
            StringBuilder sb = new StringBuilder();
            sb.append("Nro_Estabelecimento;Nome_Estabelecimento;Nro_Lote;Parcela;Plano;NSU;Autorizacao;");
            sb.append("Data_Venda;Data_Credito;Valor_Bruto;Valor_Tx_Administrativa;Valor_Liquido;Produto;");
            sb.append("Operadora;Banco;Agencia;Conta_Corrente;Id_Conciliacao");
            sb.append(StringUtils.LF);

            for (BaixaDTO baixa : baixaList) {
                StringBuilder sbCorpo = new StringBuilder();

                if (baixa.getCodNatureza() == 1) {
                    // Parcela
                    // Verifica se o registro foi conciliado
                    append(sbCorpo, baixa.getNroEstabelecimento());
                    append(sbCorpo, baixa.getNmeLoja());
                    append(sbCorpo, baixa.getDscLote());
                    append(sbCorpo, baixa.getNroParcela());
                    append(sbCorpo, baixa.getNroPlano());
                    append(sbCorpo, baixa.getNroNsu());
                    append(sbCorpo, baixa.getCodAutorizacao());
                    append(sbCorpo, baixa.getDtaVendaDt());
                    append(sbCorpo, baixa.getDtaCreditoDt());
                    appendCurrency(sbCorpo, baixa.getVlrBrutoD());
                    appendCurrency(sbCorpo, baixa.getVlrComissaoD());
                    appendCurrency(sbCorpo, baixa.getVlrLiquidoD());
                    append(sbCorpo, baixa.getDscProduto());
                    append(sbCorpo, baixa.getDscOperadora());
                    append(sbCorpo, baixa.getNroBanco());
                    append(sbCorpo, baixa.getNroAg());
                    append(sbCorpo, baixa.getNroCC());
                    append(sbCorpo, baixa.getIdConciliacao());
                }
                sbCorpo.append(StringUtils.LF);
                sb.append(sbCorpo);
            }
            carregarArquivo(response, sb, fileName);

            // atualiza nro sequencial arquivo de baixas
            empresa.setUltSeqArqBaixas(nroSeqArq);
            empresaCaService.merge(empresa);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception e) {
            LOGGER.error(MSG_ERRO_EXPORTACAO, e);
            e.printStackTrace(System.out);
        }
    }

    /**
     * Exportação CSV (2).
     *
     * @param filtro
     * @param response
     */
    // Antigo metodo criarArqiovoCSV()
    @GetMapping(value = "/csv2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportarCSV2(FiltroGestaoVendaDTO filtro, HttpServletResponse response) {
        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        String fileName = getNomeArquivoExportado(Mensagem.get("divergenciaview.exportar.divergencias"),
                filtro.getDataInicial(), filtro.getDataFinal()) + "-" + System.currentTimeMillis() + ".csv";
        try {
            EmpresaCA empresa = getEmpresaCA(filtro.getListaEmpresas());

            List<GestaoVendasDTO> divergencias = gestaoVendasService.buscarDivergenciaLoja(filtro);
            /*
             * Cabecalho do arquivo
             */
            StringBuilder sb = new StringBuilder().append(Mensagem.get("divergenciaview.table.data"))
                    .append(SEMICOLON).append(Mensagem.get("divergenciaview.table.loja")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.operadora")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.produto")).append(SEMICOLON);
            if (empresa.getTpoConciliacao().equals(2)) {
                sb.append(Mensagem.get("divergenciaview.table.parcela")).append(SEMICOLON);
            }
            sb.append(Mensagem.get("divergenciaview.table.plano")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.valor")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.nsu")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.autorizacaoSemAcento")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.id")).append(SEMICOLON)
                    .append(Mensagem.get("divergenciaview.table.status")).append(StringUtils.LF);

            for (GestaoVendasDTO dto : divergencias) {
                sb.append(String.format(Mensagem.get("formato.data.format"), dto.getData())).append(SEMICOLON);
                sb.append(dto.getLoja() == null ? StringUtils.EMPTY : dto.getLoja()).append(SEMICOLON);
                sb.append(dto.getDscOperadora() == null ? StringUtils.EMPTY : dto.getDscOperadora()).append(SEMICOLON);
                sb.append(dto.getProduto()).append(SEMICOLON);
                
                if (empresa.getTpoConciliacao().equals(2)) {
                    sb.append(dto.getParcela()).append(SEMICOLON);
                }
                
                sb.append(dto.getPlano()).append(SEMICOLON);
                sb.append(currencyFormat.format(dto.getValor())).append(SEMICOLON);
                sb.append(dto.getNsu()).append(SEMICOLON);
                sb.append(dto.getCodAutorizacao() == null ? StringUtils.EMPTY : dto.getCodAutorizacao()).append(SEMICOLON);
                sb.append(dto.getDscAreaCliente() == null ? StringUtils.EMPTY : dto.getDscAreaCliente()).append(SEMICOLON);

                String statusConciliado;
                if (dto.getStatusConciliacao() == 1) {
                    statusConciliado = "Conciliado";
                } else {
                    statusConciliado = "Nao Conciliado";
                }
                sb.append(statusConciliado).append(SEMICOLON);
                sb.append(StringUtils.LF);
            }
            carregarArquivo(response, sb, fileName);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception e) {
            String erro = Mensagem.get("divergenciaview.erroExportacao");
            LOGGER.error(erro);
            e.printStackTrace(System.out);
        }
    }

    /**
     * Carrega o arquivo CSV a ser baixado pelo usuário a partir do conteúdo
     * parametrizado.
     */
    private void carregarArquivo(HttpServletResponse response, StringBuilder sb, String fileName) throws IOException {
        byte[] bytes = sb.toString().getBytes();

        response.addHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType("text/csv;charset=UTF-8");

        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    /**
     * Obtém o ID do primeiro registro da lista.
     *
     * @param empresas
     * @return
     */
    private EmpresaCA getEmpresaCA(List<String> empresas) {
        if (Utils.isNotEmpty(empresas)) {
            String empresa = empresas.get(0);
            if (StringUtils.isNotBlank(empresa)) {
                long id = Long.parseLong(empresa.trim());
                return empresaCaService.find(id);
            }
        }
        return null;
    }

    /**
     * Obtém o atributo ultSeqArqBaixas da empresa normalizado.
     *
     * @param empresa
     * @return
     */
    private Integer getArquivoSeq(EmpresaCA empresa) {
        Integer nroSeqArq = empresa.getUltSeqArqBaixas();
        return (nroSeqArq == null) ? 0 : nroSeqArq + 1;
    }

    /**
     * Concatena um objeto a partir de seu tipo em uma string CSV.
     *
     * @param sb
     * @param o
     */
    private void append(StringBuilder sb, Object o) {
        if (o != null) {
            if (o instanceof Date) {
                sb.append(sdfBarra.format((Date) o));

            } else if (o instanceof BigDecimal) {
                sb.append(currencyFormat.format((BigDecimal) o));

            } else if (!(o instanceof String) || StringUtils.isNotBlank((String) o)) {
                sb.append(o.toString());

            }
        }
        sb.append(SEMICOLON);
    }

    /**
     * Concatena um valor monetário em uma string CSV.
     *
     * @param sb
     * @param o
     */
    private void appendCurrency(StringBuilder sb, Object o) {
        if (o != null) {
            append(sb, new BigDecimal(o.toString()));
        }
    }
    
    /**
     * Monta nome do arquivo a ser exportado
     *
     * @param funcionalidade
     * @param dataInicio
     * @param dataFim
     */
    private String getNomeArquivoExportado(String funcionalidade, Date dataInicio, Date dataFim){
        String i = sdf.format(dataInicio);
        String f = sdf.format(dataFim);
        String d = sdfHm.format(new Date());
        return String.format("%s-%s-%s-%s", new Object[] {d, funcionalidade, i, f});
    }
}
