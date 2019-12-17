package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.util.Date;

public class ParcelaDTO implements Serializable {

    private static final long serialVersionUID = -5113488272515337502L;

    private Long codigo;
    private Double valor;
    private Date data;

    public ParcelaDTO() {
    }

    public ParcelaDTO(Long codigo, Double valor, Date data) {
        this.codigo = codigo;
        this.valor = valor;
        this.data = data;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
