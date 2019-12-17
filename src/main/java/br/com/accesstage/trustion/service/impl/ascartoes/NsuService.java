package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.DetalheBilheteDTO;
import br.com.accesstage.trustion.dto.ascartoes.DetalhesNsuDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.NsuRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.INsuService;
import br.com.accesstage.trustion.util.Funcoes;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NsuService implements INsuService {

    @Autowired
    private NsuRepository repository;

    private List<DetalhesNsuDTO> detalhes;
    private DetalhesNsuDTO detalhe;
    private DetalhesNsuDTO detalheRegVenda;
    private DetalhesNsuDTO detalheRegPagto;
    private DetalhesNsuDTO detalheRegAjuste;
    private int qtdFooter;

    public static final String STA_CONFIRMADO = "2";
    public static final String STA_LIQUIDADO = "3";
    public static final String STA_CANCELADO = "4";
    public static final String STA_LIQ_ACELERACAO = "6";
    public static final String STA_DESAGENDADO = "7";
    public static final String STA_ANTECIPADO = "8";

    @Override
    @Transactional(readOnly = true)
    public DetalheBilheteDTO pesquisarDetalhesDoBilhete(String idDscAreaCliente, String codArquivo) {
        return repository.pesquisarDetalhesDoBilhete(idDscAreaCliente, codArquivo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalhesNsuDTO> consultaDetalhesNsu(GestaoVendasDTO filtro, DetalheBilheteDTO detalheBilhete) {

        detalhes = repository.consultaDetalhesNsu(filtro, detalheBilhete);

        if (!detalhes.isEmpty()) {
            Collections.sort(detalhes, (DetalhesNsuDTO detalhesNsuVO, DetalhesNsuDTO detalhesNsuVO1) -> {
                if (detalhesNsuVO.getParcela() != null && detalhesNsuVO1.getParcela() != null) {
                    Integer parcela1 = Integer.valueOf(detalhesNsuVO.getParcela());
                    Integer parcela2 = Integer.valueOf(detalhesNsuVO1.getParcela());
                    return parcela1.compareTo(parcela2);
                }
                return 0;
            });
            DetalhesNsuDTO detalheAux = detalhes.get(0);
            detalhe = new DetalhesNsuDTO();
            detalhe.setAgencia(detalheAux.getAgencia());
            detalhe.setAntecipacao(detalheAux.isAntecipacao());
            detalhe.setAutorizacao(detalheAux.getAutorizacao());
            detalhe.setBanco(detalheAux.getBanco());
            detalhe.setCartao(detalheAux.getCartao());
            detalhe.setConta(detalheAux.getConta());
            detalhe.setDataVenda(detalheAux.getDataVenda());
            detalhe.setDataCredito(detalheAux.getDataCredito());
            detalhe.setDocumento(detalheAux.getDocumento());
            detalhe.setLoja(detalheAux.getLoja());
            detalhe.setLote(detalheAux.getLote());
            detalhe.setNroNsu(detalheAux.getNroNsu());
            detalhe.setNroPlano(detalheAux.getNroPlano());
            detalhe.setOperadora(detalheAux.getOperadora());
            detalhe.setParcelas(detalheAux.getParcelas());
            detalhe.setProduto(detalheAux.getProduto());
            detalhe.setCodStatus(detalheAux.getCodStatus());

            detalhe.setTaxaAntecipacao(0D);
            detalhe.setValorLiquido(0D);
            detalhe.setValorTxAdmin(0D);
            detalhe.setValorBruto(0D);

            detalhe.setBandeira(Funcoes.capitalize(detalheAux.getOperadora()));
            detalhe.setParcela(String.valueOf(detalhes.size()));
            detalhe.setPv(detalheAux.getPv());
            detalhe.setSglOperadora(detalheAux.getSglOperadora());
            detalhe.setTid(detalheAux.getTid());
            detalhe.setConciliada(detalheAux.getConciliada());
            detalhe.setNumLogico(detalheAux.getNumLogico());
            detalhe.setIdConciliacao(detalheAux.getIdConciliacao());
            detalhe.setDtaReagendamento(detalheAux.getDtaReagendamento());
            detalhe.setDscStatus(detalheAux.getDscStatus());
            detalhe.setNmeOperadoraExibicaoPortal(detalheAux.getNmeOperadoraExibicaoPortal());
            detalhe.setDetalheBilheteDTO(detalheBilhete);
            
            detalheRegVenda = new DetalhesNsuDTO();
            detalheRegPagto = new DetalhesNsuDTO();
            detalheRegAjuste = new DetalhesNsuDTO();

            carregarInformacoes();

            // detalhes.add(detalheRegVenda);
            // detalhes.add(detalheRegPagto);
            // detalhes.add(detalheRegAjuste);
        }
        return detalhes;
    }

    public void limpar() {
        detalheRegVenda.setValorBruto(0D);
        detalheRegPagto.setValorBruto(0D);
        detalheRegAjuste.setValorBruto(0D);
        detalheRegVenda.setValorLiquido(0D);
        detalheRegPagto.setValorLiquido(0D);
        detalheRegAjuste.setValorLiquido(0D);
        detalheRegVenda.setValorTxAdmin(0D);
        detalheRegPagto.setValorTxAdmin(0D);
        detalheRegAjuste.setValorTxAdmin(0D);
        detalheRegVenda.setTaxaAntecipacao(0D);
        detalheRegPagto.setTaxaAntecipacao(0D);
        detalheRegAjuste.setTaxaAntecipacao(0D);
    }

    public void carregarInformacoes() {

        limpar();

        qtdFooter = 0;
        int parcelaRegVenda = 0;
        int parcelaRegPagto = 0;
        int parcelaRegAjuste = 0;

        for (DetalhesNsuDTO d : this.detalhes) {

            detalhe.setValorBruto(detalhe.getValorBruto() + d.getValorBruto());
            detalhe.setTaxaAntecipacao(detalhe.getTaxaAntecipacao() + d.getTaxaAntecipacao());
            detalhe.setValorLiquido(detalhe.getValorLiquido() + d.getValorLiquido());
            detalhe.setValorTxAdmin(detalhe.getValorTxAdmin() + d.getValorTxAdmin());
            detalhe.setValorBruto(detalhe.getValorBruto() + d.getValorBruto());

            switch (d.getCodStatus()) {
                case STA_CONFIRMADO:
                    detalheRegVenda.setTaxaAntecipacao(detalheRegVenda.getTaxaAntecipacao() + d.getTaxaAntecipacao());
                    detalheRegVenda.setValorLiquido(detalheRegVenda.getValorLiquido() + d.getValorLiquido());
                    detalheRegVenda.setValorTxAdmin(detalheRegVenda.getValorTxAdmin() + d.getValorTxAdmin());
                    detalheRegVenda.setValorBruto(detalheRegVenda.getValorBruto() + d.getValorBruto());
                    detalheRegVenda.setDataCredito(d.getDataCredito());
                    detalheRegVenda.setDtaReagendamento(d.getDtaReagendamento());
                    detalheRegVenda.setDscStatus(d.getDscStatus());
                    detalheRegVenda.setCodStatus("Vendas");
                    parcelaRegVenda++;
                    break;
                case STA_LIQUIDADO:
                case STA_LIQ_ACELERACAO:
                case STA_ANTECIPADO:
                    detalheRegPagto.setTaxaAntecipacao(detalheRegPagto.getTaxaAntecipacao() + d.getTaxaAntecipacao());
                    detalheRegPagto.setValorLiquido(detalheRegPagto.getValorLiquido() + d.getValorLiquido());
                    detalheRegPagto.setValorTxAdmin(detalheRegPagto.getValorTxAdmin() + d.getValorTxAdmin());
                    detalheRegPagto.setValorBruto(detalheRegPagto.getValorBruto() + d.getValorBruto());
                    detalheRegPagto.setCodStatus("Pagamento");
                    parcelaRegPagto++;
                    break;
                case STA_CANCELADO:
                case STA_DESAGENDADO:
                    detalheRegAjuste.setTaxaAntecipacao(detalheRegAjuste.getTaxaAntecipacao() + d.getTaxaAntecipacao());
                    detalheRegAjuste.setValorLiquido(detalheRegAjuste.getValorLiquido() + d.getValorLiquido());
                    detalheRegAjuste.setValorTxAdmin(detalheRegAjuste.getValorTxAdmin() + d.getValorTxAdmin());
                    detalheRegAjuste.setValorBruto(detalheRegAjuste.getValorBruto() + d.getValorBruto());
                    detalheRegAjuste.setCodStatus("Ajuste");
                    parcelaRegAjuste++;
                    break;
                default:
                    break;
            }
        }

        if (parcelaRegVenda > 0) {
            qtdFooter++;
        }
        if (parcelaRegPagto > 0) {
            qtdFooter++;
        }
        if (parcelaRegAjuste > 0) {
            qtdFooter++;
        }

        detalheRegVenda.setParcela("" + parcelaRegVenda);
        detalheRegPagto.setParcela("" + parcelaRegPagto);
        detalheRegAjuste.setParcela("" + parcelaRegAjuste);
    }

}
