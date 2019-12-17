package br.com.accesstage.trustion.dto.ascartoes;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class EmpresaCADTO {

    private Long id;
    private EmpresaCADTO matriz;
    private Set<EmpresaCADTO> filiais;
    private String cnpj;
    private String razaoSocial;
    private Integer staEmpresa;
    private Integer ultSeqArqBaixas;
    private Boolean staExportacao;
    private String senderExp;
    private String receiverExp;
    private Integer horaExec;
    private String docTypeExp;
    private Boolean staConciliaFlexData;
    private Integer nroDiasFlexData;
    private Integer nroHoraSaldo;
    private Boolean staSaldoAberto;
    private Boolean staImplantacao;
    private Integer codSemanalSaldoAberto;
    private Integer codMensalSaldoAberto;
    private Integer tpoPeriodoSaldoAberto;
    private String dscSenderSaldo;
    private String dscReceiverSaldo;
    private String dscDocTypeSaldo;
    private Integer tpoConciliacao;
    private Boolean staRetornoRemessa;
    private String senderRetRem;
    private String receiverRetRem;
    private String docTypeRetRem;
    private Boolean staExportVendas;
    private Integer nroHoraVendas;
    private String senderExpVendas;
    private Integer gerarHashVenda;
    private String receiverExpVendas;
    private String docTypeExpVendas;
    private Integer tpoRemessa;
    private Integer nroDiasRelCust;
    private Integer codigoSegmento;
    private String staConciliacao;
    private Boolean staVendaConciliadaManual;
    private Integer nroHoraVendaConciliadaManual;
    private String dscSenderVendaConciliadaManual;
    private String dscReceiverVendaConciliadaManual;
    private String dscDocTypeVendaConciliadaManual;
    private Boolean staExportAutoRelatVolumetriaVenda;
    private Integer nroHoraExportAutoRelatVolumetriaVenda;
    private String dscSenderExportAutoRelatVolumetriaVenda;
    private String dscReceiverExportAutoRelatVolumetriaVenda;
    private String dscDocTypeExportAutoRelatVolumetriaVenda;
}
