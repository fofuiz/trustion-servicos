package br.com.accesstage.trustion.configs;

import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.UnauthorizedException;
import br.com.accesstage.trustion.util.Mensagem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfiguration {

    @Value("${clients.cartoes.usuario}")
    private String usuario;

    @Value("${clients.cartoes.senha}")
    private String senha;

    private ObjectMapper objectMapper;

    @Autowired
    public RestTemplateConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean("restCartoes")
    public RestTemplate configRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(configResponseHandler());
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(usuario, senha));
        return restTemplate;
    }

    private DefaultResponseErrorHandler configResponseHandler(){
        return new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
                    throw new UnauthorizedException(Mensagem.get("msg.erro", new Object[]{Mensagem.get("msg.permissao.acesso")}));
                }else if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
                    JsonNode readTree = objectMapper.readTree(response.getBody());
                    JsonNode message = readTree.path("message");
                    throw new BadRequestResponseException(Mensagem.get("msg.erro", new Object[]{message == null || message.asText() == null ? response.getStatusText() : message.asText()}));
                }else{
                    throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.deposito.indisponivel"));
                }
            }
        };
    }
}
