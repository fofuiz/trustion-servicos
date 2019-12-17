package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;

public class RelatorioAnaliticoTotalDTO {


    private StringBuilder serie = null;
    private BigDecimal total;

    public RelatorioAnaliticoTotalDTO() {
    }

    public StringBuilder getSerie() {
        return serie;
    }

    public void setSerie(StringBuilder serie) {
        this.serie = serie;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
