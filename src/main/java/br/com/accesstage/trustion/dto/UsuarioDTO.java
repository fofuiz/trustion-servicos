package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;

    @NotNull
    @NotEmpty
    @NotBlank
    private String login;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nome;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    private String status;
    private String statusDescricao;

    private boolean primeiroAcesso;

    @NotNull
    private Long idPerfil;
    private String descricaoPerfil;

    private Long idGrupoEconomico;
    private String nomeGrupoEconomico;

    private Long idEmpresa;
    private String razaoSocial;

    @NotNull
    private Long idUsuarioCriacao;

    private Long idUsuarioAlteracao;

    private Date dataCriacao;
    private Date dataAlteracao;
    private Date datalogin;
    private String nroTelefone;

    private List<TransportadoraDTO> transportadoraList;

    private List<EmpresaDTO> empresaList;

    private List<GrupoEmpresaTransportadoraUsuarioDTO> empresasTransportadoras;
    
    private String usuarioTableau;

    private List<GrupoEconomicoDTO> grupoEconomicoList;

}
