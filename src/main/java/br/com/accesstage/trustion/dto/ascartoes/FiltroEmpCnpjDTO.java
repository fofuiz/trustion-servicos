package br.com.accesstage.trustion.dto.ascartoes;

import javax.validation.constraints.NotNull;

public class FiltroEmpCnpjDTO {

    @NotNull(message = "cnpj nao pode ser nulo")
    private String cnpj;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "FiltroEmpCnpjDTO{"
                + "cnpj='" + cnpj + '\''
                + '}';
    }

}
