package br.com.accesstage.trustion.client.cartoes.dto;

import java.math.BigDecimal;

public class CategorizacaoStatusTransacaoDTO {

    private String tipo;

    private Long numero;

    private BigDecimal total;

    public CategorizacaoStatusTransacaoDTO() {
    }

    public CategorizacaoStatusTransacaoDTO(String tipo, Long numero, BigDecimal total) {
        this.tipo = tipo;
        this.numero = numero;
        this.total = total;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
