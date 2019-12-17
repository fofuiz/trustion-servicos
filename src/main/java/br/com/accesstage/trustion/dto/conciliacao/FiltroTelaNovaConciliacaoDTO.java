package br.com.accesstage.trustion.dto.conciliacao;

import java.util.List;

public class FiltroTelaNovaConciliacaoDTO {

    private String valorTotalConciliacao;
    private String descDetalheOperacao;
    private List<ResultadoConciliacaoCartaoDetalheDTO> listaMovExtrato;
    private List<ResultadoConciliacaoCartaoDetalheDTO> listaMovCartao;

    public String getValorTotalConciliacao() {
        return valorTotalConciliacao;
    }

    public void setValorTotalConciliacao(String valorTotalConciliacao) {
        this.valorTotalConciliacao = valorTotalConciliacao;
    }

    public String getDescDetalheOperacao() {
        return descDetalheOperacao;
    }

    public void setDescDetalheOperacao(String descDetalheOperacao) {
        this.descDetalheOperacao = descDetalheOperacao;
    }

    public List<ResultadoConciliacaoCartaoDetalheDTO> getListaMovExtrato() {
        return listaMovExtrato;
    }

    public void setListaMovExtrato(List<ResultadoConciliacaoCartaoDetalheDTO> listaMovExtrato) {
        this.listaMovExtrato = listaMovExtrato;
    }

    public List<ResultadoConciliacaoCartaoDetalheDTO> getListaMovCartao() {
        return listaMovCartao;
    }

    public void setListaMovCartao(List<ResultadoConciliacaoCartaoDetalheDTO> listaMovCartao) {
        this.listaMovCartao = listaMovCartao;
    }
    
}
