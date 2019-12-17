package br.com.accesstage.trustion.util.brinks;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.brinks")
public class UrlsAPI {

    private String base;
    private String saldoCofre;
    private String selecionaColetas;
    private String selecionaConferencia;
    private String selecionaDepositosDet;
    private String selecionaFechamentoD0;

    private String selecionaFechamentoD0Det;
    private String statusCofre;


    public UrlsAPI() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
        
    }

    public String getSaldoCofre() {
        return base + saldoCofre;
    }

    public void setSaldoCofre(String saldoCofre) {
        this.saldoCofre = saldoCofre;
        
    }

    public String getSelecionaColetas() {
        return base + selecionaColetas;
    }

    public void setSelecionaColetas(String selecionaColetas) {
        this.selecionaColetas = selecionaColetas;
        
    }

    public String getSelecionaConferencia() {
        return base + selecionaConferencia;
    }

    public void setSelecionaConferencia(String selecionaConferencia) {
        this.selecionaConferencia = selecionaConferencia;
        
    }

    public String getSelecionaDepositosDet() {
        return base + selecionaDepositosDet;
    }

    public void setSelecionaDepositosDet(String selecionaDepositosDet) {
        this.selecionaDepositosDet = selecionaDepositosDet;
        
    }

    public String getSelecionaFechamentoD0() {
        return base + selecionaFechamentoD0;
    }

    public void setSelecionaFechamentoD0(String selecionaFechamentoD0) {
        this.selecionaFechamentoD0 = selecionaFechamentoD0;
    }

    public String getSelecionaFechamentoD0Det() {
        return base + selecionaFechamentoD0Det;
    }

    public void setSelecionaFechamentoD0Det(String selecionaFechamentoD0Det) {
        this.selecionaFechamentoD0Det = selecionaFechamentoD0Det;
    }

    public String getStatusCofre() {
        return base + statusCofre;
    }

    public void setStatusCofre(String statusCofre) {
        this.statusCofre = statusCofre;
   }
}
