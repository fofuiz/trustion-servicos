package br.com.accesstage.trustion.dto.ascartoes;

import br.com.accesstage.trustion.util.ascartoes.Utils;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
public class AluguelEquipamentosDTO implements Serializable {

    private BigDecimal dataCredito;
    private String bandeira;
    private BigDecimal valor;
    private String loja;
    private BigDecimal divisor = new BigDecimal(100);

    public AluguelEquipamentosDTO() {
        // do nothing
    }

    public AluguelEquipamentosDTO(BigDecimal dataCredito, String bandeira, BigDecimal valor, String loja, BigDecimal divisor) {
        this.dataCredito = dataCredito;
        this.bandeira = bandeira;
        this.valor = valor;
        this.loja = loja;
        this.divisor = new BigDecimal(100);
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public Date getDataCredito() throws ParseException {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            return (Date) formatter.parse(Utils.formatFields("####/##/##", dataCredito));
        } catch (ParseException ex) {
            throw ex;
        }
    }

    public void setDataCredito(BigDecimal dataCredito) {
        this.dataCredito = dataCredito;
    }

    public BigDecimal getValor() {
        return valor.divide(divisor);
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }
}
