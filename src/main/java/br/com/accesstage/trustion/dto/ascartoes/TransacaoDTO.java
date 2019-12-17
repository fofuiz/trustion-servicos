package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TransacaoDTO implements Serializable {

    private static final long serialVersionUID = -5585121431309301864L;

    private Long nroNsu;
    private String codAutorizacao;
    private String codLoteBandeira;
    private String dscNumeroCartao;
    private String sglOperadora;
    private Long codOperadora;
    private String nmeProduto;
    private Long codProduto;
    private boolean flgEstorno;
    private boolean flgCashback;
    private boolean flgAntecipacao;
    private Double vlrBruto;
    private Double vlrLiquido;
    private Double vlrComissao;
    private Double vlrTaxaAntecipacao;
    private String dscStatus;
    private Integer codStatus;
    private String nmeOperadora;
    private String nmeExibicaoPortal;
    private Long codPontoVenda;
    private String nmeLoja;
    private Long nroParcela;
    private Long nroPlano;
    private Long codDataCredito;
    private Date dscDataCredFormatada;
    private Date dscDataVendaFormatada;
    private String totalPaginas;
    private Long dtaVendaLong;
    private Long nroBanco;
    private Long nroAgencia;
    private String dscNroConta;
    private String dscTid;
    private String dscNroLogicoTerminal;
    private String bgClass;
    private Integer totalRegistros;
    private Set<EmpresaCA> idEmpresas;
    private String codigoTidTransacao;

    public TransacaoDTO(Long nroNsu, String nmeProduto) {
        this.nroNsu = nroNsu;
        this.nmeProduto = nmeProduto;
    }

    /**
     * Construtor com parâmetros.
     *
     * @param dataVenda <code>Data da venda</code>
     * @param nroNsu <code>Número sequencial único</code>
     * @param autorizacao <code>Código da autorização</code>
     * @param idEmpresas
     * <code>Identificadores das filiais associadas à empresa do usuário logado</code>
     * @param codOperadora <code>Código da operadora</code>
     */
    public TransacaoDTO(Date dataVenda, Long nroNsu, String autorizacao, Set<EmpresaCA> idEmpresas, String codOperadora) {
        this.dscDataVendaFormatada = dataVenda;
        this.nroNsu = nroNsu;
        this.codAutorizacao = autorizacao;
        this.idEmpresas = idEmpresas;
        this.codOperadora = NumberUtils.isNumber(codOperadora) ? Long.parseLong(codOperadora) : null;
    }

    /**
     * Construtor com parâmetros.
     *
     * @param dataVenda <code>Data da venda</code>
     * @param nroNsu <code>Número sequencial único</code>
     * @param autorizacao <code>Código da autorização</code>
     * @param idEmpresas
     * <code>Identificadores das filiais associadas à empresa do usuário logado</code>
     * @param codOperadora <code>Código da operadora</code>
     * @param codPontoVenda <code>Código ponto venda</code>
     */
    public TransacaoDTO(Date dataVenda, Long nroNsu, String autorizacao, Set<EmpresaCA> idEmpresas, String codOperadora,
            Long codPontoVenda) {
        this.dscDataVendaFormatada = dataVenda;
        this.nroNsu = nroNsu;
        this.codAutorizacao = autorizacao;
        this.idEmpresas = idEmpresas;
        this.codOperadora = NumberUtils.isNumber(codOperadora) ? Long.parseLong(codOperadora) : null;
        this.codPontoVenda = codPontoVenda;
    }
}
