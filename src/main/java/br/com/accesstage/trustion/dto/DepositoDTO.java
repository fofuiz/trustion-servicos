package br.com.accesstage.trustion.dto;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepositoDTO {

    @SerializedName("ClienteID")
    private Long clienteID;
    @SerializedName("ClienteCompusafe")
    private String clienteCompusafe;
    @SerializedName("CnpjCliente")
    private String cnpjCliente;
    @SerializedName("EquipamentoID")
    private Long equipamentoID;
    @SerializedName("EquipamentoNumeroSerial")
    private String equipamentoNumeroSerial;
    @SerializedName("CodigoMovimento")
    private Long codigoMovimento;
    @SerializedName("Sequencia")
    private Long sequencia;
    @SerializedName("Depositante")
    private String depositante;
    @SerializedName("TipoDeposito")
    private String tipoDeposito;
    @SerializedName("DepositoDT")
    private String depositoDT;
    private String dataInicio;
    private String dataFinal;
    @SerializedName("TipoMoeda")
    private String tipoMoeda;
    @SerializedName("IDFechamento")
    private Long iDFechamento;
    @SerializedName("NOTAS_001")
    private Long notas_001;
    @SerializedName("NOTAS_002")
    private Long notas_002;
    @SerializedName("NOTAS_005")
    private Long notas_005;
    @SerializedName("NOTAS_010")
    private Long notas_010;
    @SerializedName("NOTAS_020")
    private Long notas_020;
    @SerializedName("NOTAS_050")
    private Long notas_050;
    @SerializedName("NOTAS_100")
    private Long notas_100;
    @SerializedName("ValorTotal")
    private BigDecimal valorTotal;
}
