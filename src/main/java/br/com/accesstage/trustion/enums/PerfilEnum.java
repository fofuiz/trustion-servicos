package br.com.accesstage.trustion.enums;

import java.util.Optional;

public enum PerfilEnum {

    ADMINISTRADOR("Administrador"),
    MASTER_TRANSPORTADORA("Master Transportadora"),
    MASTER_CLIENTE("Master Cliente"),
    OPERADOR_TRANSPORTADORA("Operador Transportadora"),
    OPERADOR_CLIENTE("Operador Cliente"),
    BPO("BPO"),
    MASTER_CLIENTE_CARTAO("Master Cliente Cartao"),
    MASTER_CLIENTE_NUMERARIO("Master Cliente Numerario"),
    OPERADOR_CLIENTE_CARTAO("Operador Cliente Cartao"),
    OPERADOR_CLIENTE_NUMERARIO("Operador Cliente Numerario"),
    MASTER_CLIENTE_VENDA_NUMERARIO("Master Cliente Venda Numerario"),
    OPERADOR_CLIENTE_VENDA_NUMERARIO("Operador Cliente Venda Numerario");

    private String perfil;

    private PerfilEnum(String perfil) {
        this.perfil = perfil;
    }

    public String get() {
        return perfil;
    }

    public static Optional<PerfilEnum> getByDescription(String perfil) {
        for (PerfilEnum e: PerfilEnum.values()) {
            if (e.get().equals(perfil)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

}
