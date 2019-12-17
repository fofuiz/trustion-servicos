package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.SkytefDetalhe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkytefDetalheDTO {

    private String bandeira;
    private double valor;

    public SkytefDetalheDTO(SkytefDetalhe sky) {
        this.bandeira = sky.getBandeira();
        this.valor = sky.getValor();
    }

}
