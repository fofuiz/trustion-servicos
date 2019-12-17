package br.com.accesstage.trustion.client.cartoes.dto.filtro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FiltroDataDTO {

    private final List<String> cnpjs;

    private final String dataInicial;

    private final String dataFinal;

    public FiltroDataDTO() {
        cnpjs = null;
        dataInicial = null;
        dataFinal = null;
    }

    public FiltroDataDTO(List<String> cnpjs, String dataInicial, String dataFinal) {
        this.cnpjs = new ArrayList<>(cnpjs);
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public List<String> getCnpjs() {
        return cnpjs;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }
}
