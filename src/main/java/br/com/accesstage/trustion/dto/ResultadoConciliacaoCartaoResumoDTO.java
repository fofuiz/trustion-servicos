package br.com.accesstage.trustion.dto;

public class ResultadoConciliacaoCartaoResumoDTO {

    private Long codOperadora;
    private String nomeOperadora;
    private String dataExtrPagamento;
    private Integer numeroBanco;
    private Integer numeroAgencia;
    private String numeroConta;
    private Double valorPagamentoConciliado = 0.0;
    private Double valorExtratoConciliado = 0.0;
    private Double valorPagamentoNaoConciliado = 0.0;
    private Double valorExtratoNaoConciliado = 0.0;
    private String statusConciliado;

    public Long getCodOperadora() {
        return codOperadora;
    }

    public void setCodOperadora(Long codOperadora) {
        this.codOperadora = codOperadora;
    }

    public String getNomeOperadora() {
        return nomeOperadora;
    }

    public void setNomeOperadora(String nomeOperadora) {
        this.nomeOperadora = nomeOperadora;
    }

    public String getDataExtrPagamento() {
        return dataExtrPagamento;
    }

    public void setDataExtrPagamento(String dataExtrPagamento) {
        this.dataExtrPagamento = dataExtrPagamento;
    }

    public String getDataExtrPagamentoReferencia() {
        return dataExtrPagamento;
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

    public Double getValorPagamentoTotal() {
        return getValorPagamentoConciliado() + getValorPagamentoNaoConciliado();
    }

    public Double getValorExtratoTotal() {
        return getValorExtratoConciliado() + getValorExtratoNaoConciliado();
    }

    public Double getValorPagamentoConciliado() {
        if (valorPagamentoConciliado != null) {
            return valorPagamentoConciliado / 100;
        } else {
            return 0.0;
        }
    }

    public void setValorPagamentoConciliado(Double valorPagamentoConciliado) {
        this.valorPagamentoConciliado = valorPagamentoConciliado;
    }

    public Double getValorExtratoConciliado() {
        if (valorExtratoConciliado != null) {
            return valorExtratoConciliado / 100;
        } else {
            return 0.0;
        }
    }

    public void setValorExtratoConciliado(Double valorExtratoConciliado) {
        this.valorExtratoConciliado = valorExtratoConciliado;
    }

    public Double getValorPagamentoNaoConciliado() {
        if (valorPagamentoNaoConciliado != null && valorPagamentoNaoConciliado > 0.0) {
            return valorPagamentoNaoConciliado / 100;
        } else {
            return 0.0;
        }
    }

    public void setValorPagamentoNaoConciliado(Double valorPagamentoNaoConciliado) {
        this.valorPagamentoNaoConciliado = valorPagamentoNaoConciliado;
    }

    public void addValorPagamentoNaoConciliado(Double valorPagamentoNaoConciliado) {
        this.valorPagamentoNaoConciliado += valorPagamentoNaoConciliado;
    }

    public Double getValorExtratoNaoConciliado() {
        if (valorExtratoNaoConciliado != null && valorExtratoNaoConciliado > 0.0) {
            return valorExtratoNaoConciliado / 100;
        } else {
            return 0.0;
        }
    }

    public void setValorExtratoNaoConciliado(Double valorExtratoNaoConciliado) {
        this.valorExtratoNaoConciliado = valorExtratoNaoConciliado;
    }

    public void addValorExtratoNaoConciliado(Double valorExtratoNaoConciliado) {
        this.valorExtratoNaoConciliado += valorExtratoNaoConciliado;
    }

    public Double getPorcentagemPagamentoConciliada() {
        if (getValorPagamentoConciliado() > 0.0) {
            return getValorPagamentoConciliado() / getValorPagamentoTotal() * 100;
        } else {
            return 0.0;
        }
    }

    public Double getPorcentagemExtratoConciliada() {
        if (getValorExtratoConciliado() > 0.0) {
            return getValorExtratoConciliado() / getValorExtratoTotal() * 100;
        } else {
            return 0.0;
        }
    }

    public Double getPorcentagemPagamentoNaoConciliada() {
        if (getValorPagamentoNaoConciliado() > 0.0) {
            return 100 - getPorcentagemPagamentoConciliada();
        } else {
            return 0.0;
        }
    }

    public Double getPorcentagemExtratoNaoConciliada() {
        if (getValorExtratoNaoConciliado() > 0.0) {
            return 100 - getPorcentagemExtratoConciliada();
        } else {
            return 0.0;
        }
    }

    public String getStatusConciliado() {
        return statusConciliado;
    }

    public void setStatusConciliado(String statusConciliado) {
        this.statusConciliado = statusConciliado;
    }
 
    @Override
    public String toString() {
        return "ResultadoConciliacaoCartaoResumoDTO {" +
                "codOperadora='" + codOperadora + '\'' +
                ", nomeOperadora='" + nomeOperadora + '\'' +
                ", dataExtrPagamento='" + dataExtrPagamento + '\'' +
                ", numeroBanco='" + numeroBanco + '\'' +
                ", numeroAgencia='" + numeroAgencia + '\'' +
                ", numeroConta='" + numeroConta + '\'' +
                ", valorPagamentoConciliado='" + valorPagamentoConciliado + '\'' +
                ", valorExtratoConciliado='" + valorExtratoConciliado + '\'' +
                ", valorPagamentoNaoConciliado='" + valorPagamentoNaoConciliado + '\'' +
                ", valorExtratoNaoConciliado='" + valorExtratoNaoConciliado + '\'' +
                ", statusConciliado='" + statusConciliado + '\'' +
                '}';
    }
}
