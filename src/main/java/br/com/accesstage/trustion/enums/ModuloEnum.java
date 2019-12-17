package br.com.accesstage.trustion.enums;

public enum ModuloEnum {
    NUMERARIOS("numerarios"),
    CARTOES("cartoes"),
    CHEQUES("cheques");

    private String valor;

    ModuloEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
