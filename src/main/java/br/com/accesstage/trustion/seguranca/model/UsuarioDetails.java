package br.com.accesstage.trustion.seguranca.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.enums.ModuloEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long idUsuario;
	private String login;
	private String senha;
	private String nome;
	private Long idPerfil;
	private String perfil;
	private boolean autenticado;
	private boolean enabled;
	private List<String> modulos;

	public UsuarioDetails() {
		this.modulos = new ArrayList<>();
		this.enabled = true;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authrorities = new ArrayList<>();
		authrorities.add(new SimpleGrantedAuthority(perfil));
		return authrorities;
	}

	public List<String> getModulos() {
		return modulos;
	}

	public void addModulo(ModuloEnum moduloEnum){
		modulos.add(moduloEnum.getValor());
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

	@Override
	public String getPassword() {
		return senha;
	}
	
	public void setPassword(String senha) {
		this.senha = senha;
	}

	@Override
	public String getUsername() {
		return login;
	}

	public void setUsername(String login) {
		this.login = login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
