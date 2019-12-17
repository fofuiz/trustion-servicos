package br.com.accesstage.trustion.enums;

public enum CodigoFlagStringHistorico {

    REPROCESSAR("REPROCESSAR"), REPROCESSADO("REPROCESSADO");

    private String status;

    CodigoFlagStringHistorico(String string) {
        this.status = string;
    }

    public String get() {
        return status;
    }
}
