package br.com.accesstage.trustion.enums.ascartoes;

public enum TaxaAdministrativaPlanoEnum {

    PLANO_1("PLANO_1", 1L),
    PLANO_2("PLANO_2", 2L),
    PLANO_3("PLANO_3", 3L),
    PLANO_4("PLANO_4", 4L),
    PLANO_5("PLANO_5", 5L),
    PLANO_6("PLANO_6", 6L),
    PLANO_7("PLANO_7", 7L),
    PLANO_8("PLANO_8", 8L),
    PLANO_9("PLANO_9", 9L),
    PLANO_10("PLANO_10", 10L),
    PLANO_11("PLANO_11", 11L),
    PLANO_12("PLANO_12", 12L);

    private final Long numeroPlano;
    private final String nomePlano;

    TaxaAdministrativaPlanoEnum(String nomePlano, Long numeroPlano) {
        this.nomePlano = nomePlano;
        this.numeroPlano = numeroPlano;
    }

    public Long getNumeroPlano() {
        return numeroPlano;
    }

    public String getNomePlano() {
        return nomePlano;
    }

}
