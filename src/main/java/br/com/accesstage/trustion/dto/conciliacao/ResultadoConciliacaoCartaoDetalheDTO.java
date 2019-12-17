package br.com.accesstage.trustion.dto.conciliacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultadoConciliacaoCartaoDetalheDTO {

    private Long id;
    private Long numeroConciliado;
    private Long codEmpresa;
    private Long codOperadora;
    private Long codigoGrupoConciliacao;
    private Integer codigoMovimento;
    private String nomeOperadora;
    private String nomeProduto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_BR", timezone = "America/Sao_Paulo")
    private Date dataExtrPagamento;
    private Integer numeroBanco;
    private Integer numeroAgencia;
    private String numeroConta;
    private BigDecimal valor;
    private String tipo;
    private String status;
    private String nomeArquivoOrigem;
    private String hashValue;
    private String dscExtrHistorico;
    private String dscExtrDocumento;
    private Long nroGrupoConciliado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroConciliado() {
        return numeroConciliado;
    }

    public void setNumeroConciliado(Long numeroConciliado) {
        this.numeroConciliado = numeroConciliado;
    }

    public Long getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Long getCodOperadora() {
        return codOperadora;
    }

    public void setCodOperadora(Long codOperadora) {
        this.codOperadora = codOperadora;
    }

    public Long getCodigoGrupoConciliacao() {
        return codigoGrupoConciliacao;
    }

    public void setCodigoGrupoConciliacao(Long codigoGrupoConciliacao) {
        this.codigoGrupoConciliacao = codigoGrupoConciliacao;
    }

    public Integer getCodigoMovimento() {
        return codigoMovimento;
    }

    public void setCodigoMovimento(Integer codigoMovimento) {
        this.codigoMovimento = codigoMovimento;
    }

    public String getDescricaoMovimento() {
        if (codigoMovimento == null) {
            return "";
        } else if (codigoMovimento.equals(1)) {
            return "EXTRATO";
        } else if (codigoMovimento.equals(2)) {
            return "CARTAO";
        } else {
            return "";
        }
    }

    public void setDescricaoMovimento(String descricaoMovimento) {
        //do nothing
    }

    public String getNomeOperadora() {
        return nomeOperadora;
    }

    public void setNomeOperadora(String nomeOperadora) {
        this.nomeOperadora = nomeOperadora;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Date getDataExtrPagamento() {
        return dataExtrPagamento;
    }

    public void setDataExtrPagamento(Date dataExtrPagamento) {
        this.dataExtrPagamento = dataExtrPagamento;
    }

    public String getDataExtrPagamentoReferencia() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        if (dataExtrPagamento != null) {
            return dateFormat.format(dataExtrPagamento);
        } else {
            return "";
        }
    }

    public void setDataExtrPagamentoReferencia(String dataExtrPagamentoReferencia) {
        //do nothing
    }

    public Integer getNumeroBanco() {
        return numeroBanco;
    }

    public void setNumeroBanco(Integer numeroBanco) {
        this.numeroBanco = numeroBanco;
    }

    public Integer getNumeroAgencia() {
        return numeroAgencia;
    }

    public void setNumeroAgencia(Integer numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        try {
            this.numeroConta = String.valueOf(Long.parseLong(numeroConta));
        } catch (NumberFormatException ex) {
            this.numeroConta = numeroConta;
        }
    }

    public BigDecimal getValor() {
        if (valor != null) {
            return valor;
        } else {
            return new BigDecimal(0);
        }
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomeArquivoOrigem() {
        return nomeArquivoOrigem;
    }

    public void setNomeArquivoOrigem(String nomeArquivoOrigem) {
        this.nomeArquivoOrigem = nomeArquivoOrigem;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getDscExtrHistorico() {
        return dscExtrHistorico;
    }

    public void setDscExtrHistorico(String dscExtrHistorico) {
        this.dscExtrHistorico = dscExtrHistorico;
    }

    public String getDscExtrDocumento() {
        return dscExtrDocumento;
    }

    public void setDscExtrDocumento(String dscExtrDocumento) {
        this.dscExtrDocumento = dscExtrDocumento;
    }

    public Long getNroGrupoConciliado() {
        return nroGrupoConciliado;
    }

    public void setNroGrupoConciliado(Long nroGrupoConciliado) {
        this.nroGrupoConciliado = nroGrupoConciliado;
    }
}
