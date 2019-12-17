package br.com.accesstage.trustion.dto.conciliacao;

public class AdquirenteDTO {

    private Long codOperadora;
    private String nomeOperadora;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultadoBancoVO [codBancoStr=" + codOperadora + ", nomeBanco="
                + nomeOperadora + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((codOperadora == null) ? 0 : codOperadora.hashCode());
        result = prime * result
                + ((nomeOperadora == null) ? 0 : nomeOperadora.hashCode());

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
        AdquirenteDTO other = (AdquirenteDTO) obj;
        if (codOperadora == null) {
            if (other.codOperadora != null) {
                return false;
            }
        } else if (!codOperadora.equals(other.codOperadora)) {
            return false;
        }
        if (nomeOperadora == null) {
            if (other.nomeOperadora != null) {
                return false;
            }
        } else if (!nomeOperadora.equals(other.nomeOperadora)) {
            return false;
        }

        return true;
    }

    /**
     * @return the codOperadora
     */
    public Long getCodOperadora() {
        return codOperadora;
    }

    /**
     * @param codOperadora
     *            the codOperadora to set
     */
    public void setCodOperadora(Long codOperadora) {
        this.codOperadora = codOperadora;
    }

    /**
     * @return the nomeOperadora
     */
    public String getNomeOperadora() {
        return nomeOperadora;
    }

    /**
     * @param nomeOperadora
     *            the nomeOperadora to set
     */
    public void setNomeOperadora(String nomeOperadora) {
        this.nomeOperadora = nomeOperadora;
    }
}
