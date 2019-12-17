package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetalheBilheteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bilhete;
    private String bilheteGrupo;
    private BigDecimal entrada;
    private BigDecimal txEmbarque;
    private String agenteCode;
    private String nomePassageiro;

    public DetalheBilheteDTO() {

    }

    public DetalheBilheteDTO(String bilhete, String bilheteGrupo, BigDecimal entrada, BigDecimal txEmbarque,
            String agenteCode, String nomePassageiro) {
        this.bilhete = bilhete;
        this.bilheteGrupo = bilheteGrupo;
        this.entrada = entrada;
        this.txEmbarque = txEmbarque;
        this.agenteCode = agenteCode;
        this.nomePassageiro = nomePassageiro;

    }

    /**
     * Parâmetros CIA Áerea, dados do passageiro.
     */
    /**
     * @return the bilhete
     */
    public String getBilhete() {
        return bilhete;
    }

    /**
     * @param bilhete the bilhete to set
     */
    public void setBilhete(String bilhete) {
        this.bilhete = bilhete;
    }

    /**
     * @return the bilheteGrupo
     */
    public String getBilheteGrupo() {
        return bilheteGrupo;
    }

    /**
     * @param bilheteGrupo the bilheteGrupo to set
     */
    public void setBilheteGrupo(String bilheteGrupo) {
        this.bilheteGrupo = bilheteGrupo;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getTxEmbarque() {
        return txEmbarque;
    }

    public void setTxEmbarque(BigDecimal txEmbarque) {
        this.txEmbarque = txEmbarque;
    }

    /**
     * @return the agenteCode
     */
    public String getAgenteCode() {
        return agenteCode;
    }

    /**
     * @param agenteCode the agenteCode to set
     */
    public void setAgenteCode(String agenteCode) {
        this.agenteCode = agenteCode;
    }

    /**
     * @return the nomePassageiro
     */
    public String getNomePassageiro() {
        return nomePassageiro;
    }

    /**
     * @param nomePassageiro the nomePassageiro to set
     */
    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

}
