package br.com.accesstage.trustion.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoDTO {
	
	private Long idNotificacao;
	
	@NotNull
	private Long idGrupoEconomico;
	@NotNull
	private Long idEmpresa;
	@NotNull
	private Long idTipoNotificacao;
	
	private Long idUsuario;
	
	private UsuarioDTO usuarioDTO;
	
	private String nomeGrupoEconomico;
	
	private String descTipoNotificacao;
	
	private String razaoSocial;
	
	private String login;
	
	private String email;
	
	private String nome;

	private String status;
	
	private Date dataCriacao;

	private Long idUsuarioCriacao;

	private Date dataAlteracao;

	private Long idUsuarioAlteracao;
    
    private Long idTransportadora;
	
}
