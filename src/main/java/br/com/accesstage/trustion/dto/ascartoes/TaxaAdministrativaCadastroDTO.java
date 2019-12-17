package br.com.accesstage.trustion.dto.ascartoes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.accesstage.trustion.enums.ascartoes.TaxaAdministrativaPlanoEnum;

public class TaxaAdministrativaCadastroDTO {

    private Long codTaxaAdministrativa;
    private Long codEmp;
    private Long codLoja;
    private Long codOperadora;
    private Long codProduto;
    private Long codPontoVenda;
    private String nmeLoja;
    private String nmePontoVenda;
    private String nmeOperadora;
    private String nmeProduto;
    private Long codUsuario;
    private BigDecimal nroPlano1;
    private BigDecimal nroPlano2;
    private BigDecimal nroPlano3;
    private BigDecimal nroPlano4;
    private BigDecimal nroPlano5;
    private BigDecimal nroPlano6;
    private BigDecimal nroPlano7;
    private BigDecimal nroPlano8;
    private BigDecimal nroPlano9;
    private BigDecimal nroPlano10;
    private BigDecimal nroPlano11;
    private BigDecimal nroPlano12;
    private List<TaxaAdministrativaPlanoEnum> listaSalvar;
    private Map<TaxaAdministrativaPlanoEnum, Long> mapaId;

    public Long getCodTaxaAdministrativa() {
        return codTaxaAdministrativa;
    }

    public void setCodTaxaAdministrativa(Long codTaxaAdministrativa) {
        this.codTaxaAdministrativa = codTaxaAdministrativa;
    }

    public Long getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(Long codEmp) {
        this.codEmp = codEmp;
    }

    public Long getCodLoja() {
        return codLoja;
    }

    public void setCodLoja(Long codLoja) {
        this.codLoja = codLoja;
    }

    public Long getCodOperadora() {
        return codOperadora;
    }

    public void setCodOperadora(Long codOperadora) {
        this.codOperadora = codOperadora;
    }

    public Long getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Long codProduto) {
        this.codProduto = codProduto;
    }

    public String getNmeLoja() {
        return nmeLoja;
    }

    public void setNmeLoja(String nmeLoja) {
        this.nmeLoja = nmeLoja;
    }

    public String getNmePontoVenda() {
        return nmePontoVenda;
    }

    public void setNmePontoVenda(String nmePontoVenda) {
        this.nmePontoVenda = nmePontoVenda;
    }

    public String getNmeOperadora() {
        return nmeOperadora;
    }

    public void setNmeOperadora(String nmeOperadora) {
        this.nmeOperadora = nmeOperadora;
    }

    public String getNmeProduto() {
        return nmeProduto;
    }

    public void setNmeProduto(String nmeProduto) {
        this.nmeProduto = nmeProduto;
    }

    public BigDecimal getNroPlano1() {
        return nroPlano1;
    }

    public void setNroPlano1(BigDecimal nroPlano1) {
        this.nroPlano1 = nroPlano1;
    }

    public BigDecimal getNroPlano2() {
        return nroPlano2;
    }

    public void setNroPlano2(BigDecimal nroPlano2) {
        this.nroPlano2 = nroPlano2;
    }

    public BigDecimal getNroPlano3() {
        return nroPlano3;
    }

    public void setNroPlano3(BigDecimal nroPlano3) {
        this.nroPlano3 = nroPlano3;
    }

    public BigDecimal getNroPlano4() {
        return nroPlano4;
    }

    public void setNroPlano4(BigDecimal nroPlano4) {
        this.nroPlano4 = nroPlano4;
    }

    public BigDecimal getNroPlano5() {
        return nroPlano5;
    }

    public void setNroPlano5(BigDecimal nroPlano5) {
        this.nroPlano5 = nroPlano5;
    }

    public BigDecimal getNroPlano6() {
        return nroPlano6;
    }

    public void setNroPlano6(BigDecimal nroPlano6) {
        this.nroPlano6 = nroPlano6;
    }

    public BigDecimal getNroPlano7() {
        return nroPlano7;
    }

    public void setNroPlano7(BigDecimal nroPlano7) {
        this.nroPlano7 = nroPlano7;
    }

    public BigDecimal getNroPlano8() {
        return nroPlano8;
    }

    public void setNroPlano8(BigDecimal nroPlano8) {
        this.nroPlano8 = nroPlano8;
    }

    public BigDecimal getNroPlano9() {
        return nroPlano9;
    }

    public void setNroPlano9(BigDecimal nroPlano9) {
        this.nroPlano9 = nroPlano9;
    }

    public BigDecimal getNroPlano10() {
        return nroPlano10;
    }

    public void setNroPlano10(BigDecimal nroPlano10) {
        this.nroPlano10 = nroPlano10;
    }

    public BigDecimal getNroPlano11() {
        return nroPlano11;
    }

    public void setNroPlano11(BigDecimal nroPlano11) {
        this.nroPlano11 = nroPlano11;
    }

    public BigDecimal getNroPlano12() {
        return nroPlano12;
    }

    public void setNroPlano12(BigDecimal nroPlano12) {
        this.nroPlano12 = nroPlano12;
    }

    public List<TaxaAdministrativaPlanoEnum> getListaSalvar() {
        if (listaSalvar == null) {
            listaSalvar = new ArrayList<>();
        }
        return listaSalvar;
    }

    public void setListaSalvar(List<TaxaAdministrativaPlanoEnum> listaSalvar) {
        this.listaSalvar = listaSalvar;
    }

    public Map<TaxaAdministrativaPlanoEnum, Long> getMapaId() {
        if (mapaId == null) {
            mapaId = new HashMap<>();
        }
        return mapaId;
    }

    public void setMapaId(Map<TaxaAdministrativaPlanoEnum, Long> mapaId) {
        this.mapaId = mapaId;
    }

    public Long getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Long codUsuario) {
        this.codUsuario = codUsuario;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TaxaAdministrativaCadastroDTO)) {
            return false;
        }
        final TaxaAdministrativaCadastroDTO other = (TaxaAdministrativaCadastroDTO) object;
        if (!(codLoja == null ? other.codLoja == null : codLoja.equals(other.codLoja))) {
            return false;
        }
        if (!(codOperadora == null ? other.codOperadora == null : codOperadora.equals(other.codOperadora))) {
            return false;
        }
        return codProduto == null ? other.codProduto == null : codProduto.equals(other.codProduto);
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + ((codLoja == null) ? 0 : codLoja.hashCode());
        result = prime * result + ((codOperadora == null) ? 0 : codOperadora.hashCode());
        result = prime * result + ((codProduto == null) ? 0 : codProduto.hashCode());
        return result;
    }

    public int compareTo(TaxaAdministrativaCadastroDTO o) {
        if (this.codLoja < o.codLoja) {
            return -1;
        }
        if (this.codLoja > o.codLoja) {
            return 1;
        }
        return 0;
    }

    public Long getCodPontoVenda() {
        return codPontoVenda;
    }

    public void setCodPontoVenda(Long codPontoVenda) {
        this.codPontoVenda = codPontoVenda;
    }
}
