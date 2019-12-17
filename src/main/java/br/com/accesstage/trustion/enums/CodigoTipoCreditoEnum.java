package br.com.accesstage.trustion.enums;

import java.util.Optional;

public enum CodigoTipoCreditoEnum {

    CREDITOD0(1L),
    CREDITOD1(2L);
    

    private final Long id;

    private CodigoTipoCreditoEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static Optional<CodigoTipoCreditoEnum> gerarEnum(long idTipoCredito) {
        for (CodigoTipoCreditoEnum e : CodigoTipoCreditoEnum.values()) {
            if (e.getId() == idTipoCredito) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
