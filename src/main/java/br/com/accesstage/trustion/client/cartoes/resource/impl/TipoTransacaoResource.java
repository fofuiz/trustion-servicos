package br.com.accesstage.trustion.client.cartoes.resource.impl;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoTipoProdutoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;
import br.com.accesstage.trustion.client.cartoes.resource.ITipoTransacaoResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoTransacaoResource implements ITipoTransacaoResource{

    @Value("${clients.cartoes.url}")
    private String urlBase;

    @Value("${clients.cartoes.transaco-tipo.url}")
    private String urlTipoTransacao;

    @Value("${clients.cartoes.transaco-tipo.total}")
    private String urlTipoTransacaoTotal;

    private RestTemplate restTemplate;

    private Gson gson;

    @Autowired
    public TipoTransacaoResource(@Qualifier("restCartoes") RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    @Override
    public List<CategorizacaoTipoProdutoDTO> getTotalAVistaParceladoOutros(FiltroDataDTO dto) {

        List<CategorizacaoTipoProdutoDTO> dtos = null;
        ResponseEntity<String> response = restTemplate.postForEntity(urlBase + urlTipoTransacao + urlTipoTransacaoTotal, dto, String.class);
        dtos = gson.fromJson(response.getBody(), new TypeToken<ArrayList<CategorizacaoTipoProdutoDTO>>(){}.getType());
        
        return dtos;
    }
}
