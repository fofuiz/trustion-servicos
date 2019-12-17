package br.com.accesstage.trustion.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComposicaoDTO {

    private String numeroGVT;
    private String compartimento;
    private String selo;
    private Long notas_001;
    private Long notas_002;
    private Long notas_005;
    private Long notas_010;
    private Long notas_020;
    private Long notas_050;
    private Long notas_100;
    private LocalDateTime dataCriacao;
    private List<String> denominacao;
    private List<Long> quantidade;
    private List<Long> valor;

}
