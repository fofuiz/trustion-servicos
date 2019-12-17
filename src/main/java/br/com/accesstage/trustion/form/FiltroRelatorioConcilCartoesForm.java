package br.com.accesstage.trustion.form;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroRelatorioConcilCartoesForm {

	private Date dataInicial;
	private Date dataFinal;

}
