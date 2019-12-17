package br.com.accesstage.trustion.client.cartoes.dto;

import java.math.BigDecimal;

public class CategorizacaoTipoProdutoDTO {

    private String tipo;

    private String tipoTransacao;

    private BigDecimal valor;

    public CategorizacaoTipoProdutoDTO() {
    }

    public CategorizacaoTipoProdutoDTO(String tipo, String tipoTransacao, BigDecimal valor) {
        this.tipo = tipo;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
