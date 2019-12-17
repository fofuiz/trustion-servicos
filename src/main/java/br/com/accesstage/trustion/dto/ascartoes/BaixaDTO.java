package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Classe usada para comunicação com a camada DAO.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaixaDTO implements Serializable {

    private final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private Long id;
    private Long vlrBruto;
    private Double vlrBrutoD;
    private Long vlrLiquido;
    private Double vlrLiquidoD;
    private Long vlrComissao;
    private Double vlrComissaoD;
    private Long vlrTxAntecipacao;
    private Long nroNsu;
    private String codAutorizacao;
    private Integer nroParcela;
    private Integer nroPlano;
    private Integer codNatureza;
    private Integer dtaVenda;
    private Integer dtaCredito;
    private Date dtaVendaDt;
    private Date dtaCreditoDt;
    private Integer nroBanco;
    private String nroCC;
    private Integer nroAg;
    private String idtOperadora;
    private String nmeLoja;
    private String idtProduto;
    private String dscLote;
    private String ddsCliente;
    private String totalPaginas;
    private String dscNumeroCartao;
    private String nroEstabelecimento;
    private String dscProduto;
    private String dscOperadora;
    private String idConciliacao;
    private Long codPontoVenda;
}
