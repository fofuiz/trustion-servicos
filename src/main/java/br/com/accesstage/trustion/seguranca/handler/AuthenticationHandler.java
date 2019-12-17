package br.com.accesstage.trustion.seguranca.handler;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.exception.ResponseException;
import br.com.accesstage.trustion.util.Mensagem;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationHandler extends BasicAuthenticationEntryPoint {

    //sem Authentication(vazia também): InsufficientAuthenticationException
    //Credencial errada: BadCredentialsException
    //usuário inativo: DisabledException
    //Authentication vazia(Og==), e custom exceptions: InternalAuthenticationServiceException

    @Autowired
    private Gson gson;

    @Log
    private Logger LOG;

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(AuthenticationHandler.class.getName());
        super.afterPropertiesSet();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //super.commence(request, response, authException);
        invalidLogin(response, authException);

    }


    private void insuffientAuth(HttpServletResponse response, AuthenticationException e){
        ResponseException responseException = new ResponseException();
        responseException.setCodigo(HttpStatus.UNAUTHORIZED.value());
        responseException.setMensagem(Mensagem.get("msg.sem.auth"));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(gson.toJson(responseException));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void invalidLogin(HttpServletResponse response, AuthenticationException e){
        ResponseException responseException = new ResponseException();
        responseException.setCodigo(HttpStatus.UNAUTHORIZED.value());
        responseException.setMensagem(Mensagem.get("msg.usuario.senha.invalido"));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(gson.toJson(responseException));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
