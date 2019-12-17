package br.com.accesstage.trustion.seguranca.controller;

import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.dto.ascartoes.UserEmpresaCADTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.seguranca.service.TrustStrategyService;
import br.com.accesstage.trustion.util.Utils;
import br.com.accesstage.trustion.configs.log.Log;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Set;


@RestController
public class UserController {

	private DefaultResponseErrorHandler handler = new DefaultResponseErrorHandler() {
		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			if(response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.erro.deposito.permissao"));
			}else{
				throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.deposito.indisponivel"));
			}
		}

	};

	@Autowired
	private TrustStrategyService trustStrategyService;

	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Autowired
	private IUsuarioService usuarioService;

	@Log
	private Logger LOG;

	@Autowired
	private Gson gson;


	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Authentication user() {
		LOG.info(Utils.getInicioMetodo());
		UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		usuarioDetails.setPassword("**confidential**");

		try {

			auditoriaService.registrar(Mensagem.get("msg.auditoria.login", new Object[] {UTF8.Usuario + " "}), null);
			usuarioService.registrarLoginDoUsuario(usuarioDetails.getIdUsuario());

		} catch (Exception e) {
			LOG.error("Exceção " + e.getMessage(), e);
			System.out.println(e.getMessage());
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(usuarioDetails, usuarioDetails.getPassword(), usuarioDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		LOG.info(Utils.getFimMetodo() + gson.toJson(SecurityContextHolder.getContext().getAuthentication()));
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@RequestMapping(value = "/user/empresaCa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<UserEmpresaCADTO>> getEmpresaCa() {

		LOG.info(Utils.getInicioMetodo());

		Set<UserEmpresaCADTO> response = usuarioService.getUserEmpresaCaList();

		LOG.info(Utils.getFimMetodo());
		return ResponseEntity.ok(response);
	}
}
