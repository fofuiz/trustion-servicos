package br.com.accesstage.trustion.client.cartoes.dto.filtro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FiltroMultiplasDatasDTO {

    private List<String> cnpjs;

    private String dataInicialVendas;

    private String dataFinalVendas;

    private String dataInicialRecebimentos;

    private String dataFinalRecebimentos;

    private String dataInicialRecebimentosFuturos;

    private String dataFinalRecebimentosFuturos;

    public FiltroMultiplasDatasDTO() {
        cnpjs = null;
        dataInicialVendas = null;
        dataFinalVendas = null;
        dataInicialRecebimentos = null;
        dataFinalRecebimentos = null;
        dataInicialRecebimentosFuturos = null;
        dataFinalRecebimentosFuturos = null;
    }

    public FiltroMultiplasDatasDTO(List<String> cnpjs,
                                    String dataInicialVendas,
                                    String dataFinalVendas,
                                    String dataInicialRecebimentos,
                                    String dataFinalRecebimentos,
                                    String dataInicialRecebimentosFuturos,
                                    String dataFinalRecebimentosFuturos) {
        this.cnpjs = new ArrayList<>(cnpjs);
        this.dataInicialVendas = dataInicialVendas;
        this.dataFinalVendas = dataFinalVendas;
        this.dataInicialRecebimentos = dataInicialRecebimentos;
        this.dataFinalRecebimentos = dataFinalRecebimentos;
        this.dataInicialRecebimentosFuturos = dataInicialRecebimentosFuturos;
        this.dataFinalRecebimentosFuturos = dataFinalRecebimentosFuturos;
    }

    public List<String> getCnpjs() {
        return cnpjs;
    }

    public String getDataInicialVendas() {
        return dataInicialVendas;
    }

    public String getDataFinalVendas() {
        return dataFinalVendas;
    }

    public String getDataInicialRecebimentos() {
        return dataInicialRecebimentos;
    }

    public String getDataFinalRecebimentos() {
        return dataFinalRecebimentos;
    }

    public String getDataInicialRecebimentosFuturos() {
        return dataInicialRecebimentosFuturos;
    }

    public String getDataFinalRecebimentosFuturos() {
        return dataFinalRecebimentosFuturos;
    }
}
