package br.com.accesstage.trustion.client.cartoes.dto;

import java.math.BigDecimal;

public class ProdutoStatusDTO {

    private Long codigoProduto;

    private Long numeroTransacoes;

    private String produto;

    private BigDecimal total;

    private String status;

    private ProdutoStatusDTO() {
    }

    public ProdutoStatusDTO(Long codigoProduto, Long numeroTransacoes, String produto, BigDecimal total, String status) {
        this.codigoProduto = codigoProduto;
        this.numeroTransacoes = numeroTransacoes;
        this.produto = produto;
        this.total = total;
        this.status = status;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public Long getNumeroTransacoes() {
        return numeroTransacoes;
    }

    public String getProduto() {
        return produto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public void setNumeroTransacoes(Long numeroTransacoes) {
        this.numeroTransacoes = numeroTransacoes;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
