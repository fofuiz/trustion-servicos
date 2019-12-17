package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.BkOfficeDetalhe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BkOfficeDetalheDTO {

    private String bandeira;
    private double valor;

    public BkOfficeDetalheDTO(BkOfficeDetalhe bk) {
        this.bandeira = bk.getBandeira();
        this.valor = bk.getValor();
    }

}
