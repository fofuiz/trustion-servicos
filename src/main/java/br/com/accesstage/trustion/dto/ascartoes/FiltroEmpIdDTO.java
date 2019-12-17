package br.com.accesstage.trustion.dto.ascartoes;

import javax.validation.constraints.NotNull;

public class FiltroEmpIdDTO {

    @NotNull(message = "empId nao pode ser nulo")
    private Long empId;

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    @Override
    public String toString() {
        return "FiltroLojaCADTO{"
                + "empId='" + empId + '\''
                + '}';
    }

}
