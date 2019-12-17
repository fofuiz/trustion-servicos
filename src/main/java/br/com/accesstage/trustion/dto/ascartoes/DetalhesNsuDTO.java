package br.com.accesstage.trustion.dto.ascartoes;

import br.com.accesstage.trustion.util.Funcoes;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DetalhesNsuDTO implements Serializable {

    private static final long serialVersionUID = 9156611542678781439L;

    private Double valorBruto;
    private Double valorLiquido;
    private Double taxaAntecipacao;
    private String nroNsu;
    private String documento;
    private String autorizacao;
    private String parcela;
    private String nroPlano;
    private boolean antecipacao; // E_ANTECIPACAO
    private String operadora; // DIM_BANDEIRA
    private Date dataVenda;
    private String cartao;
    private Date dataCredito;
    private String agencia;
    private String banco;
    private String conta;
    private String lote; // LOTE_BANDEIRA
    private String produto;
    private String loja;
    private String pv;
    private String codStatus;
    private String sglOperadora;
    private String tid;
    private String conciliada;
    private String numLogico;
    private String idConciliacao;
    private Double valorTxAdmin;
    private Date dtaReagendamento;
    private String dscStatus;
    private String nmeOperadoraExibicaoPortal;
    private String bandeira;
    private List<ParcelaDTO> parcelas;
    private DetalheBilheteDTO detalheBilhete;

    public DetalhesNsuDTO() {
    }

    public DetalhesNsuDTO(DetalhesNsuDTO det) {
        this.cartao = det.getCartao();
        this.documento = det.getDocumento();
        this.dataVenda = det.getDataVenda();
        this.loja = Funcoes.capitalize(det.getLoja());

        // isto evita erro de index no caso da Goodcard que usa 9 digitos no Lote, pois eh = ao cod
        // autorizacao
        if (det.getLote().length() < 7) {
            this.lote = Funcoes.insereZerosEsquerda(det.getLote(), 7);
        }
        this.parcela = det.getParcela();
        this.banco = det.getBanco();
        this.agencia = det.getAgencia();
        if (det.getConta() != null) {
            this.conta = Funcoes.removeZerosEsquerda(det.getConta());
        }
        this.autorizacao = det.getAutorizacao();
        this.valorBruto = det.getValorBruto();
        this.valorLiquido = det.getValorLiquido();
        this.taxaAntecipacao = det.getTaxaAntecipacao();
        this.nroNsu = det.getNroNsu();
        this.nroPlano = det.getNroPlano();
        this.antecipacao = det.isAntecipacao();
        this.operadora = det.getOperadora();
        this.dataCredito = det.getDataCredito();
        this.produto = det.getProduto();
        this.codStatus = det.getCodStatus();
        this.parcelas = det.getParcelas();

        this.pv = det.getPv();
        this.sglOperadora = det.getSglOperadora();
        this.tid = det.getTid();
        this.conciliada = det.getConciliada();
        this.numLogico = det.getNumLogico();
        this.idConciliacao = det.getIdConciliacao();
        this.valorTxAdmin = det.getValorTxAdmin();
        this.dtaReagendamento = det.getDtaReagendamento();
        this.dscStatus = det.getDscStatus();
        this.detalheBilhete = det.getDetalheBilheteDTO();
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }

    public List<ParcelaDTO> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ParcelaDTO> parcelas) {
        this.parcelas = parcelas;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLote() {
        return lote;
    }

    public Double getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(Double valorBruto) {
        this.valorBruto = valorBruto;
    }

    public Double getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(Double valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public Double getTaxaAntecipacao() {
        return taxaAntecipacao;
    }

    public void setTaxaAntecipacao(Double taxaAntecipacao) {
        this.taxaAntecipacao = taxaAntecipacao;
    }

    public String getNroNsu() {
        return nroNsu;
    }

    public void setNroNsu(String nroNsu) {
        this.nroNsu = nroNsu;
    }

    public String getNroPlano() {
        return nroPlano;
    }

    public void setNroPlano(String nroPlano) {
        this.nroPlano = nroPlano;
    }

    public boolean isAntecipacao() {
        return antecipacao;
    }

    public void setAntecipacao(boolean antecipacao) {
        this.antecipacao = antecipacao;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public Date getDataCredito() {
        return dataCredito;
    }

    public void setDataCredito(Date dataCredito) {
        this.dataCredito = dataCredito;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getCodStatus() {
        return codStatus;
    }

    public void setCodStatus(String codStatus) {
        this.codStatus = codStatus;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getPv() {
        return pv;
    }

    public void setSglOperadora(String sglOperadora) {
        this.sglOperadora = sglOperadora;
    }

    public String getSglOperadora() {
        return sglOperadora;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTid() {
        return tid;
    }

    public void setConciliada(String conciliada) {
        this.conciliada = conciliada;
    }

    public String getConciliada() {
        return conciliada;
    }

    public void setNumLogico(String numLogico) {
        this.numLogico = numLogico;
    }

    public String getNumLogico() {
        return numLogico;
    }

    public void setIdConciliacao(String idConciliacao) {
        this.idConciliacao = idConciliacao;
    }

    public String getIdConciliacao() {
        return idConciliacao;
    }

    public void setValorTxAdmin(Double valorTxAdmin) {
        this.valorTxAdmin = valorTxAdmin;
    }

    public Double getValorTxAdmin() {
        return valorTxAdmin;
    }

    public void setDtaReagendamento(Date dtaReagendamento) {
        this.dtaReagendamento = dtaReagendamento;
    }

    public Date getDtaReagendamento() {
        return dtaReagendamento;
    }

    public void setDscStatus(String dscStatus) {
        this.dscStatus = dscStatus;
    }

    public String getDscStatus() {
        return dscStatus;
    }

    public String getNmeOperadoraExibicaoPortal() {
        return nmeOperadoraExibicaoPortal;
    }

    public void setNmeOperadoraExibicaoPortal(String nmeOperadoraExibicaoPortal) {
        this.nmeOperadoraExibicaoPortal = nmeOperadoraExibicaoPortal;
    }

    public DetalheBilheteDTO getDetalheBilheteDTO() {
        return detalheBilhete;
    }

    public void setDetalheBilheteDTO(DetalheBilheteDTO detalheBilhete) {
        this.detalheBilhete = detalheBilhete;
    }

}
