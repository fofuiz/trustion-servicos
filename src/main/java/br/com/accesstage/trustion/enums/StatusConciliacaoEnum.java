package br.com.accesstage.trustion.enums;

public enum StatusConciliacaoEnum {
    PENDENTE("PENDENTE"), NAO_CONCILIADO("NÃO CONCILIADO"), CONCILIADO("CONCILIADO"),DIVERGENTE("CONC DIVERGE"),AJUSTE("CONCIL. AJUSTE");

    private String status;

    StatusConciliacaoEnum(String status) {
        this.status = status;
    }

    public String get() {
        return status;
    }
}
