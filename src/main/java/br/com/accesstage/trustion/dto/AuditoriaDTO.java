package br.com.accesstage.trustion.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
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
public class AuditoriaDTO {

    private Long idAuditoria;

    private Long idUsuario;

    private String usuario;

    private Long idGrupoEconomico;

    private String grupoEconomico;

    private Long idEmpresa;
    private String empresa;

    @NotNull
    private Date dataInicial;

    @NotNull
    private Date dataFinal;

    private String acao;

    @NotNull
    private Long idUsuarioConsulta;

    private Long idGrupoEconomicoUsuario;

    private String nroOcorrencia;
}