package br.com.accesstage.trustion.seguranca;

import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;
import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UsuarioAutenticado {

	public static Long getIdUsuario() {
		return getUsuarioAutenticado().getIdUsuario();
	}

	public static String getLogin() {
		return getUsuarioAutenticado().getUsername();
	}

	public static String getSenha() {
		return getUsuarioAutenticado().getPassword();
	}

	public static String getNome() {
		return getUsuarioAutenticado().getNome();
	}

	public static String getPerfil() {
		return getUsuarioAutenticado().getPerfil();
	}
	
	public static long getIdPerfil() {
		return getUsuarioAutenticado().getIdPerfil();
	}

	private static UsuarioDetails getUsuarioAutenticado() {

		UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return usuarioDetails;
	}

	public static  CodigoPerfilEnum getPerfilEnum(){
		Optional<CodigoPerfilEnum> perfilOp = CodigoPerfilEnum.gerarEnum(getIdPerfil());

		return perfilOp.orElseThrow(() ->new BadRequestResponseException(Mensagem.get("msg.usario.sem.perfil", new Object[]{getIdUsuario()})));
	}
}
