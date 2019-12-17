package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.TableauTokenDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.seguranca.service.TrustStrategyService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.Mensagem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class TableauTokenController {

    @Value("${tableau.base}")
    private String base_url;
    @Value("${tableau.token}")
    private String url_token;
    @Value("${tableau.params.username}")
    private String username;
    @Value("${tableau.params.target_site}")
    private String target_site;
    @Value("${tableau.params.client_ip}")
    private String client_ip;
    @Autowired
    private TrustStrategyService trustStrategyService;
    @Autowired
    private IUsuarioService usuarioService;
    @Log
    private static Logger LOGGER;

    @GetMapping(value = "/tableau/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken() throws InternalServerErrorResponseException {
        TableauTokenDTO dto = new TableauTokenDTO();
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            map.add("server", base_url);
            UsuarioDTO usuario = usuarioService.pesquisar(UsuarioAutenticado.getIdUsuario());
            if (usuario != null && usuario.getUsuarioTableau() != null) {
                map.add("username", usuario.getUsuarioTableau());
            } else {
                map.add("username", username);
            }

            map.add("client_ip", client_ip);
            map.add("target_site", target_site);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            RestTemplate restTemplate = new RestTemplate(trustStrategyService.retornaTrustRequestFactory());
            ResponseEntity<String> response = restTemplate.postForEntity(base_url + url_token, request, String.class);
            String corpo = response.getBody();
            dto.setToken(corpo);
            LOGGER.info(corpo);
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.servico.tableau.indisponivel"));
        }
        return ResponseEntity.ok(dto);
    }
}
