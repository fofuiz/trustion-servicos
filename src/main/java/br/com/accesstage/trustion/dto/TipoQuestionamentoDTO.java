package br.com.accesstage.trustion.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TipoQuestionamentoDTO {

    private Long idTipoQuestionamento;
    private String descricao;
    private Date dataCriacao;
    private Date dataAlteracao;
    private Long idUsuarioCriacao;
    private Long idUsuarioAlteracao;
    
    private String nomeUsuarioCriacao;
    private String nomeUsuarioAlteracao;
}