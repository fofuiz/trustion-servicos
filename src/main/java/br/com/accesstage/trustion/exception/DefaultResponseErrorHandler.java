package br.com.accesstage.trustion.exception;

import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class DefaultResponseErrorHandler extends org.springframework.web.client.DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.erro.deposito.permissao"));
        } else {
            throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.deposito.indisponivel"));
        }
    }

}
