package br.com.accesstage.trustion.dto.conciliacao;

public class FiltroTelaRegraClienteAdquirenteDTO {

    private String codigoBancoStr;
    private Integer codigoAquirenteStr;
    private Integer empId;

    /**
     * @return the codigoBancoStr
     */
    public String getCodigoBancoStr() {
        return codigoBancoStr;
    }

    /**
     * @param codigoBancoStr the codigoBancoStr to set
     */
    public void setCodigoBancoStr(String codigoBancoStr) {
        this.codigoBancoStr = codigoBancoStr;
    }

    public Integer getCodigoAquirenteStr() {
        return codigoAquirenteStr;
    }

    public void setCodigoAquirenteStr(Integer codigoAquirenteStr) {
        this.codigoAquirenteStr = codigoAquirenteStr;
    }

    /**
     * @return the empId
     */
    public Integer getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FiltroTelaRegraClienteAdquirenteDTO ["
                + "codigoBancoStr=" + codigoBancoStr + ", "
                + "codigoAquirenteStr=" + codigoAquirenteStr + ", "
                + "empId=" + empId + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((codigoBancoStr == null) ? 0 : codigoBancoStr.hashCode());
        result = prime
                * result
                + ((codigoAquirenteStr == null) ? 0 : codigoAquirenteStr.hashCode());
        result = prime * result
                + ((empId == null) ? 0 : empId.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FiltroTelaRegraClienteAdquirenteDTO other = (FiltroTelaRegraClienteAdquirenteDTO) obj;
        if (codigoBancoStr == null) {
            if (other.codigoBancoStr != null) {
                return false;
            }
        } else if (!codigoBancoStr.equals(other.codigoBancoStr)) {
            return false;
        }
        if (codigoAquirenteStr == null) {
            if (other.codigoAquirenteStr != null) {
                return false;
            }
        } else if (!codigoAquirenteStr.equals(other.codigoAquirenteStr)) {
            return false;
        }
        if (empId == null) {
            if (other.empId != null) {
                return false;
            }
        } else if (!empId.equals(other.empId)) {
            return false;
        }
        return true;
    }

}
