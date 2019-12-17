package br.com.accesstage.trustion.dto.conciliacao;

/**
 * @author cristiano.silva
 *
 */
public class ResultadoDomicilioBancarioDTO {

    private String descContaBanco;
    private Integer nroBanco;
    private String nroContaCorrente;
    private Integer nroAgencia;
    private String staAtivo;
    private Integer empID;

    /**
     * @return the descContaBanco
     */
    public String getDescContaBanco() {
        return descContaBanco;
    }

    /**
     * @param descContaBanco the descContaBanco to set
     */
    public void setDescContaBanco(String descContaBanco) {
        this.descContaBanco = descContaBanco;
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
     * @return the nroContaCorrente
     */
    public String getNroContaCorrente() {
        return nroContaCorrente;
    }

    /**
     * @param nroContaCorrente the nroContaCorrente to set
     */
    public void setNroContaCorrente(String nroContaCorrente) {
        this.nroContaCorrente = nroContaCorrente;
    }

    /**
     * @return the nroAgencia
     */
    public Integer getNroAgencia() {
        return nroAgencia;
    }

    /**
     * @param nroAgencia the nroAgencia to set
     */
    public void setNroAgencia(Integer nroAgencia) {
        this.nroAgencia = nroAgencia;
    }

    /**
     * @return the staAtivo
     */
    public String getStaAtivo() {
        return staAtivo;
    }

    /**
     * @param staAtivo the staAtivo to set
     */
    public void setStaAtivo(String staAtivo) {
        this.staAtivo = staAtivo;
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
        return "BancoNovoEntity "
                + "[descContaBanco=" + descContaBanco + ", "
                + "nroBanco=" + nroBanco + ", "
                + "nroContaCorrente=" + nroContaCorrente + ", "
                + "nroAgencia=" + nroAgencia + ", "
                + "staAtivo=" + staAtivo + ", "
                + "empID=" + empID
                + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((descContaBanco == null) ? 0 : descContaBanco.hashCode());
        result = prime * result
                + ((nroBanco == null) ? 0 : nroBanco.hashCode());
        result = prime * result
                + ((nroContaCorrente == null) ? 0 : nroContaCorrente.hashCode());
        result = prime * result
                + ((nroAgencia == null) ? 0 : nroAgencia.hashCode());
        result = prime * result
                + ((staAtivo == null) ? 0 : staAtivo.hashCode());
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
        ResultadoDomicilioBancarioDTO other = (ResultadoDomicilioBancarioDTO) obj;
        if (descContaBanco == null) {
            if (other.descContaBanco != null) {
                return false;
            }
        } else if (!descContaBanco.equals(other.descContaBanco)) {
            return false;
        }
        if (nroBanco == null) {
            if (other.nroBanco != null) {
                return false;
            }
        } else if (!nroBanco.equals(other.nroBanco)) {
            return false;
        }
        if (nroContaCorrente == null) {
            if (other.nroContaCorrente != null) {
                return false;
            }
        } else if (!nroContaCorrente.equals(other.nroContaCorrente)) {
            return false;
        }
        if (nroAgencia == null) {
            if (other.nroAgencia != null) {
                return false;
            }
        } else if (!nroAgencia.equals(other.nroAgencia)) {
            return false;
        }
        if (staAtivo == null) {
            if (other.staAtivo != null) {
                return false;
            }
        } else if (!staAtivo.equals(other.staAtivo)) {
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
