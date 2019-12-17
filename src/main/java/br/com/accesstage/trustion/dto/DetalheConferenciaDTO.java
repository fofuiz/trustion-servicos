package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalheConferenciaDTO {

    private Long idDetalheConferencia;
    private String numSerieEquipamento;
    private Long idEquipamento;
    private String cnpjCliente;
    private String numeroGVT;
    private BigDecimal valorDeclarado;
    private Date dataConferencia;
    private BigDecimal valorConferido;
    private BigDecimal diferenca;
    private Date dataCriacao;
    private Date dataRecolhimento;
    private String transportadora;
    private String empresa;
    private String modeloNegocio;

}
