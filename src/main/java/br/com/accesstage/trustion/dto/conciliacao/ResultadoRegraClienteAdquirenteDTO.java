package br.com.accesstage.trustion.dto.conciliacao;

public class ResultadoRegraClienteAdquirenteDTO {

    private String codBanco;
    private Integer codOperadora;
    private String nmeExibicaoPortal;
    private Integer empId;
    private String dscChavePrincipal;
    private String dscChaveSecundaria;
    private String staAtivo;

    /**
     * @return the codBanco
     */
    public String getCodBanco() {
        return codBanco;
    }

    /**
     * @param codBanco the codBanco to set
     */
    public void setCodBanco(String codBanco) {
        this.codBanco = codBanco;
    }

    /**
     * @return the codOperadora
     */
    public Integer getCodOperadora() {
        return codOperadora;
    }

    /**
     * @param codOperadora the codOperadora to set
     */
    public void setCodOperadora(Integer codOperadora) {
        this.codOperadora = codOperadora;
    }

    /**
     * @return the nmeExibicaoPortal
     */
    public String getNmeExibicaoPortal() {
        return nmeExibicaoPortal;
    }

    /**
     * @param nmeExibicaoPortal the nmeExibicaoPortal to set
     */
    public void setNmeExibicaoPortal(String nmeExibicaoPortal) {
        this.nmeExibicaoPortal = nmeExibicaoPortal;
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

    /**
     * @return the dscChavePrincipal
     */
    public String getDscChavePrincipal() {
        return dscChavePrincipal;
    }

    /**
     * @param dscChavePrincipal the dscChavePrincipal to set
     */
    public void setDscChavePrincipal(String dscChavePrincipal) {
        this.dscChavePrincipal = dscChavePrincipal;
    }

    /**
     * @return the dscChaveSecundaria
     */
    public String getDscChaveSecundaria() {
        return dscChaveSecundaria;
    }

    /**
     * @param dscChaveSecundaria the dscChaveSecundaria to set
     */
    public void setDscChaveSecundaria(String dscChaveSecundaria) {
        this.dscChaveSecundaria = dscChaveSecundaria;
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


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((codBanco == null) ? 0 : codBanco.hashCode());
        result = prime * result
                + ((codOperadora == null) ? 0 : codOperadora.hashCode());
        result = prime * result
                + ((nmeExibicaoPortal == null) ? 0 : nmeExibicaoPortal.hashCode());
        result = prime * result
                + ((empId == null) ? 0 : empId.hashCode());
        result = prime * result
                + ((dscChavePrincipal == null) ? 0 : dscChavePrincipal.hashCode());
        result = prime * result
                + ((dscChaveSecundaria == null) ? 0 : dscChaveSecundaria.hashCode());
        result = prime * result
                + ((staAtivo == null) ? 0 : staAtivo.hashCode());
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
        ResultadoRegraClienteAdquirenteDTO other = (ResultadoRegraClienteAdquirenteDTO) obj;
        if (codBanco == null) {
            if (other.codBanco != null) {
                return false;
            }
        } else if (!codBanco.equals(other.codBanco)) {
            return false;
        }
        if (codOperadora == null) {
            if (other.codOperadora != null) {
                return false;
            }
        } else if (!codOperadora.equals(other.codOperadora)) {
            return false;
        }
        if (nmeExibicaoPortal == null) {
            if (other.nmeExibicaoPortal != null) {
                return false;
            }
        } else if (!nmeExibicaoPortal.equals(other.nmeExibicaoPortal)) {
            return false;
        }
        if (empId == null) {
            if (other.empId != null) {
                return false;
            }
        } else if (!empId.equals(other.empId)) {
            return false;
        }
        if (dscChavePrincipal == null) {
            if (other.dscChavePrincipal != null) {
                return false;
            }
        } else if (!dscChavePrincipal.equals(other.dscChavePrincipal)) {
            return false;
        }
        if (dscChaveSecundaria == null) {
            if (other.dscChaveSecundaria != null) {
                return false;
            }
        } else if (!dscChaveSecundaria.equals(other.dscChaveSecundaria)) {
            return false;
        }
        if (staAtivo == null) {
            if (other.staAtivo != null) {
                return false;
            }
        } else if (!staAtivo.equals(other.staAtivo)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultadoRegraClienteAdquirenteDTO "
                + "[codBanco=" + codBanco + ", "
                + "codOperadora=" + codOperadora + ", "
                + "nmeExibicaoPortal=" + nmeExibicaoPortal + ", "
                + "empId=" + empId + ", "
                + "dscChavePrincipal=" + dscChavePrincipal + ", "
                + "dscChaveSecundaria=" + dscChaveSecundaria + ", "
                + "staAtivo=" + staAtivo
                + "]";
    }
}
