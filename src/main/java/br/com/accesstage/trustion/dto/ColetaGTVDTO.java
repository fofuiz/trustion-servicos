package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColetaGTVDTO {

    private Long idColetaGTV;
    private String numSerie;
    private Long idEquipamento;
    private String cnpjCliente;
    private Date periodoInicialDt;
    private Date coletaDt;
    private BigDecimal valorTotal;
    private Long numeroGVT;
    private LocalDateTime dataCriacao;
    private String nmeTransportadora;
    private String empresa;
    private String modeloNegocio;

}
