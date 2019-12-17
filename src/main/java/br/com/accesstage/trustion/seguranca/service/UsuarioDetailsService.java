package br.com.accesstage.trustion.seguranca.service;

import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.ModuloEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.UsuarioNaoExisteException;
import br.com.accesstage.trustion.exception.UsuarioSuspensoException;
import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.model.Perfil;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.IPerfilRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;

import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService{
	
	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	@Autowired
	private IPerfilRepository perfilRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario user = usuarioRepository.findOneByLogin(username);

		UsuarioDetails credential = new UsuarioDetails();

		if(user == null) {
			throw new UsuarioNaoExisteException(Mensagem.get("msg.usuario.senha.invalido"));

		} else if(user.getStatus().equals(StatusUsuarioEnum.INATIVO.get())) {
            credential.setEnabled(Boolean.FALSE);

		} else if(user.getStatus().equals(StatusUsuarioEnum.SUSPENSO.get()) && !user.isPrimeiroAcesso()) {
			throw new UsuarioSuspensoException(Mensagem.get("msg.usuario.senha.invalido"));
		}

		Perfil perfil = perfilRepository.findOne(user.getIdPerfil());

		if(perfil == null) {
			return null;
		}

		credential.setIdUsuario(user.getIdUsuario());
		credential.setNome(user.getNome());
		credential.setIdPerfil(perfil.getIdPerfil());
		credential.setPerfil(perfil.getDescricao());
		credential.setUsername(user.getLogin());
		credential.setPassword(user.getSenha());

		Optional<CodigoPerfilEnum> perfilEnum = CodigoPerfilEnum.gerarEnum(user.getIdPerfil());

		perfilEnum.ifPresent(codigoPerfilEnum -> {
			switch (codigoPerfilEnum){
				case ADMINISTRADOR:
				case MASTER_TRANSPORTADORA:
				case OPERADOR_TRANSPORTADORA:
					credential.addModulo(ModuloEnum.NUMERARIOS);
					break;
				case MASTER_CLIENTE:
				case OPERADOR_CLIENTE:
					credential.addModulo(ModuloEnum.NUMERARIOS);
					credential.addModulo(ModuloEnum.CARTOES);
					break;
				case MASTER_CLIENTE_VENDA_NUMERARIO:
				case OPERADOR_CLIENTE_VENDA_NUMERARIO:
					credential.addModulo(ModuloEnum.NUMERARIOS);
					credential.addModulo(ModuloEnum.CARTOES);
					break;
				case MASTER_CLIENTE_CARTAO:
				case OPERADOR_CLIENTE_CARTAO:
					credential.addModulo(ModuloEnum.CARTOES);
					break;
				case MASTER_CLIENTE_NUMERARIO:
				case OPERADOR_CLIENTE_NUMERARIO:
					credential.addModulo(ModuloEnum.NUMERARIOS);
					break;
			}
		});
		perfilEnum.orElseThrow(() -> new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"perfil"})));

		return credential;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
