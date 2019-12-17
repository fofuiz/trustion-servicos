package br.com.accesstage.trustion.enums;

import java.util.Optional;

public enum CodigoPerfilEnum {

    ADMINISTRADOR(1L),
    MASTER_TRANSPORTADORA(2L),
    MASTER_CLIENTE(3L),
    OPERADOR_TRANSPORTADORA(4L),
    OPERADOR_CLIENTE(5L),
    BPO(6L),
    MASTER_CLIENTE_CARTAO(7L),
    MASTER_CLIENTE_NUMERARIO(8L),
	OPERADOR_CLIENTE_CARTAO(9L),
	OPERADOR_CLIENTE_NUMERARIO(10L),
    MASTER_CLIENTE_VENDA_NUMERARIO(11L),
    OPERADOR_CLIENTE_VENDA_NUMERARIO(12L);

    private final Long codigoPerfil;

    private CodigoPerfilEnum(Long codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public Long get() {
        return codigoPerfil;
    }

    public static Optional<CodigoPerfilEnum> gerarEnum(long idPerfil) {
        for (CodigoPerfilEnum e : CodigoPerfilEnum.values()) {
            if (e.get() == idPerfil) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
