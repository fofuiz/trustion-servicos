package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import br.com.accesstage.trustion.model.ConcilCartaoAdquirenteSkytefBkoffice;
import br.com.accesstage.trustion.model.SkytefDetalhe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConcilCartaoAdquirenteSkytefBkofficeDTO {

    private LocalDate dataConcil;
    private boolean status;
    private double somaAdq;
    private double somaBk;
    private double somaSky;
    private List<AdquirenteDetalheDTO> adquirenteDetalhes;
    private List<BkOfficeDetalheDTO> bkofficeDetalhes;
    private List<SkytefDetalheDTO> skytefDetalhes;

    public static Page<ConcilCartaoAdquirenteSkytefBkofficeDTO> converter(Page<ConcilCartaoAdquirenteSkytefBkoffice> concil) {
        return concil.map(ConcilCartaoAdquirenteSkytefBkofficeDTO::new);
    }

    public ConcilCartaoAdquirenteSkytefBkofficeDTO(ConcilCartaoAdquirenteSkytefBkoffice concil) {
        this.dataConcil = concil.getDataConcil();

        this.adquirenteDetalhes = new ArrayList<>();
        this.adquirenteDetalhes.addAll(concil.getAdquirenteDetalhes().stream()
                .map(AdquirenteDetalheDTO::new)
                .sorted((h1, h2) -> h1.getBandeira().compareTo(h2.getBandeira()))
                .collect(Collectors.toList()));
        this.somaAdq = this.adquirenteDetalhes.stream().mapToDouble(a -> a.getValor()).sum();

        this.bkofficeDetalhes = new ArrayList<>();
        this.bkofficeDetalhes.addAll(concil.getBkofficeDetalhes().stream()
                .map(BkOfficeDetalheDTO::new)
                .sorted((h1, h2) -> h1.getBandeira().compareTo(h2.getBandeira()))
                .collect(Collectors.toList()));
        this.somaBk = this.bkofficeDetalhes.stream().mapToDouble(b -> b.getValor()).sum();

        this.skytefDetalhes = new ArrayList<>();
        this.skytefDetalhes.addAll(concil.getSkytefDetalhes().stream()
                .map(SkytefDetalheDTO::new)
                .sorted((h1, h2) -> h1.getBandeira().compareTo(h2.getBandeira()))
                .collect(Collectors.toList()));
        this.somaSky = this.skytefDetalhes.stream().mapToDouble(s -> s.getValor()).sum();
    }

}
