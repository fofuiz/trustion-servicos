package br.com.accesstage.trustion.client.cartoes.enums;

public enum StatusTransacaoEnum {
    VENDAS("Vendas"), RECEBIMENTOS("Recebimentos"), RECEBIMENTOS_FUTUROS("Recebimentos Futuros");

    private String descricao;

    StatusTransacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
