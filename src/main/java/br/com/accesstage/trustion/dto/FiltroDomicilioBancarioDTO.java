package br.com.accesstage.trustion.dto;

/**
 * @author cristiano.silva
 *
 */
public class FiltroDomicilioBancarioDTO {

    private String codigoBancoStr;

    private Integer nroBanco;

    private Integer empID;

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

    /**
     * @return the nroBanco
     */
    public Integer getNroBanco() {
        return nroBanco;
    }

    /**
     * @param nroBanco the nroBanco to set
     */
    public void setNroBanco(Integer nroBanco) {
        this.nroBanco = nroBanco;
    }

    /**
     * @return the empID
     */
    public Integer getEmpID() {
        return empID;
    }

    /**
     * @param empID the empID to set
     */
    public void setEmpID(Integer empID) {
        this.empID = empID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FiltroTelaDomicilioBancarioVO ["
                + "codigoBancoStr=" + codigoBancoStr + ", "
                + "nroBanco=" + nroBanco + ", "
                + "empID=" + empID + "]";
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
                + ((nroBanco == null) ? 0 : nroBanco
                                .hashCode());
        result = prime * result
                + ((empID == null) ? 0 : empID.hashCode());
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
        FiltroDomicilioBancarioDTO other = (FiltroDomicilioBancarioDTO) obj;
        if (codigoBancoStr == null) {
            if (other.codigoBancoStr != null) {
                return false;
            }
        } else if (!codigoBancoStr.equals(other.codigoBancoStr)) {
            return false;
        }
        if (nroBanco == null) {
            if (other.nroBanco != null) {
                return false;
            }
        } else if (!nroBanco.equals(other.nroBanco)) {
            return false;
        }
        if (empID == null) {
            if (other.empID != null) {
                return false;
            }
        } else if (!empID.equals(other.empID)) {
            return false;
        }
        return true;
    }

}
