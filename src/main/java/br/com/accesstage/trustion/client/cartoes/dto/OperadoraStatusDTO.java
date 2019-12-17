package br.com.accesstage.trustion.client.cartoes.dto;

import java.math.BigDecimal;

public class OperadoraStatusDTO {

    private Long codigoOperadora;

    private Long numeroTransacoes;

    private String operadora;

    private BigDecimal total;

    private String status;

    public OperadoraStatusDTO() {
    }

    public OperadoraStatusDTO(Long codigoOperadora, Long numeroTransacoes, String operadora, BigDecimal total, String status) {
        this.codigoOperadora = codigoOperadora;
        this.numeroTransacoes = numeroTransacoes;
        this.operadora = operadora;
        this.total = total;
        this.status = status;
    }

    public Long getNumeroTransacoes() {
        return numeroTransacoes;
    }

    public String getOperadora() {
        return operadora;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public Long getCodigoOperadora() {
        return codigoOperadora;
    }

    public void setNumeroTransacoes(Long numeroTransacoes) {
        this.numeroTransacoes = numeroTransacoes;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCodigoOperadora(Long codigoOperadora) {
        this.codigoOperadora = codigoOperadora;
    }
}
