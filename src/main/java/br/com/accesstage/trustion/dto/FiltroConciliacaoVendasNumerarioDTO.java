package br.com.accesstage.trustion.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroConciliacaoVendasNumerarioDTO {
	
	@NotNull
    private Date dataInicial;

    @NotNull
    private Date dataFinal;

    private Long gtv;
    
}
