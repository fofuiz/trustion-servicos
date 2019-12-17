package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.DepositoDTO;
import br.com.accesstage.trustion.exception.DefaultResponseErrorHandler;

import br.com.accesstage.trustion.seguranca.service.TrustStrategyService;
import br.com.accesstage.trustion.service.interfaces.IDepositoService;
import br.com.accesstage.trustion.util.brinks.UrlsAPI;
import br.com.accesstage.trustion.util.brinks.UsuarioAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class DepositoService implements IDepositoService {

    @Autowired
    private UrlsAPI urlsAPI;

    @Autowired
    private UsuarioAPI usuarioAPI;

    @Autowired
    private TrustStrategyService trustStrategyService;

    @Log
    private static Logger LOGGER;

    @Override
    public List<DepositoDTO> consultaApi(DepositoDTO dto) {

        DefaultResponseErrorHandler handler = new DefaultResponseErrorHandler();

        List<DepositoDTO> dtos;

        RestTemplate restTemplate = new RestTemplate(trustStrategyService.retornaTrustRequestFactory());
        restTemplate.setErrorHandler(handler);
        String url = urlsAPI.getSelecionaDepositosDet() + "?ClienteCNPJ=" + dto.getCnpjCliente() + "&CwpLogin=" + usuarioAPI.getLogin() + "&CWPPwd=" + usuarioAPI.getSenha() + "&CwpEquipamentoId=" + dto.getEquipamentoID() + "&CwpEquipamentoNrSerie=" + dto.getEquipamentoNumeroSerial() + "&Dt_Inicio=" + dto.getDataInicio() + "&Dt_Fim=" + dto.getDataFinal() + "&api_key=O";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        dtos = Arrays.asList(gson.fromJson(response.getBody(), DepositoDTO[].class));
        return dtos;
    }

}