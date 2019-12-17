package br.com.accesstage.trustion.dto.ascartoes;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class FiltroAluguelEquipamentosDTO implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataDe;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataAte;
    private Long empId;

    public Date getDataDe() {
        return dataDe;
    }

    public void setDataDe(Date dataDe) {
        this.dataDe = dataDe;
    }

    public Date getDataAte() {
        return dataAte;
    }

    public void setDataAte(Date dataAte) {
        this.dataAte = dataAte;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }
}
