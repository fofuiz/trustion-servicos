package br.com.accesstage.trustion.dto.conciliacao;

public class ResultadoModalidadeTarifaDTO {

    private String codigoModalidadeStr;
    private String tipoArrecadacao;
    private String descricaoModalidade;

    /**
     * @return the codigoModalidadeStr
     */
    public String getCodigoModalidadeStr() {
        return codigoModalidadeStr;
    }

    /**
     * @param codigoModalidadeStr the codigoModalidadeStr to set
     */
    public void setCodigoModalidadeStr(String codigoModalidadeStr) {
        this.codigoModalidadeStr = codigoModalidadeStr;
    }

    /**
     * @return the tipoArrecadacao
     */
    public String getTipoArrecadacao() {
        return tipoArrecadacao;
    }

    /**
     * @param tipoArrecadacao the tipoArrecadacao to set
     */
    public void setTipoArrecadacao(String tipoArrecadacao) {
        this.tipoArrecadacao = tipoArrecadacao;
    }

    /**
     * @return the descricaoModalidade
     */
    public String getDescricaoModalidade() {
        return descricaoModalidade;
    }

    /**
     * @param descricaoModalidade the descricaoModalidade to set
     */
    public void setDescricaoModalidade(String descricaoModalidade) {
        this.descricaoModalidade = descricaoModalidade;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((codigoModalidadeStr == null) ? 0 : codigoModalidadeStr
                                .hashCode());
        result = prime
                * result
                + ((descricaoModalidade == null) ? 0 : descricaoModalidade
                                .hashCode());
        result = prime * result
                + ((tipoArrecadacao == null) ? 0 : tipoArrecadacao.hashCode());
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
        ResultadoModalidadeTarifaDTO other = (ResultadoModalidadeTarifaDTO) obj;
        if (codigoModalidadeStr == null) {
            if (other.codigoModalidadeStr != null) {
                return false;
            }
        } else if (!codigoModalidadeStr.equals(other.codigoModalidadeStr)) {
            return false;
        }
        if (descricaoModalidade == null) {
            if (other.descricaoModalidade != null) {
                return false;
            }
        } else if (!descricaoModalidade.equals(other.descricaoModalidade)) {
            return false;
        }
        if (tipoArrecadacao == null) {
            if (other.tipoArrecadacao != null) {
                return false;
            }
        } else if (!tipoArrecadacao.equals(other.tipoArrecadacao)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultadoModalidadeTarifaVO [codigoModalidadeStr="
                + codigoModalidadeStr + ", tipoArrecadacao=" + tipoArrecadacao
                + ", descricaoModalidade=" + descricaoModalidade + "]";
    }
}
