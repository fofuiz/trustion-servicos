package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdquirenteDetalheDTO {

    private String bandeira;
    private double valor;

    public AdquirenteDetalheDTO(AdquirenteDetalhe adq) {
        this.bandeira = adq.getBandeira();
        this.valor = adq.getValor();
    }

}
