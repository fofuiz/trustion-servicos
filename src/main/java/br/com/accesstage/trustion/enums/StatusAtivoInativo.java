package br.com.accesstage.trustion.enums;

public enum StatusAtivoInativo {
    ATIVO("Ativo"), INATIVO("Inativo"), A("A"), I("I");

    private String texto;

    private StatusAtivoInativo(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
