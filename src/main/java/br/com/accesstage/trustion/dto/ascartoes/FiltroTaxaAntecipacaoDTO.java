package br.com.accesstage.trustion.dto.ascartoes;

import javax.validation.constraints.NotNull;

public class FiltroTaxaAntecipacaoDTO {

    @NotNull
    private Integer idsEmp;
    @NotNull
    private String dataInicial;
    @NotNull
    private String dataFinal;

    public Integer getIdsEmp() {
        return idsEmp;
    }

    public void setIdsEmp(Integer idsEmp) {
        this.idsEmp = idsEmp;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public String toString() {
        return "FiltroTaxaAntecipacaoDTO{"
                + "idsEmp='" + idsEmp + '\''
                + ", dataInicial='" + dataInicial + '\''
                + ", dataFinal='" + dataFinal + '\''
                + '}';
    }

}
