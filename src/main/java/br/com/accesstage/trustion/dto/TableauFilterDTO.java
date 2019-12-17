package br.com.accesstage.trustion.dto;

public class TableauFilterDTO {

    private String cnpj;

    public TableauFilterDTO() {
    }

    public TableauFilterDTO(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
