package br.com.accesstage.trustion.client.cartoes.resource.impl;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoStatusTransacaoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.OperadoraStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.ProdutoStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroMultiplasDatasDTO;
import br.com.accesstage.trustion.client.cartoes.enums.StatusTransacaoEnum;
import br.com.accesstage.trustion.client.cartoes.resource.IStatusTransacaoResource;
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
public class StatusTransacaoResource implements IStatusTransacaoResource {

    @Value("${clients.cartoes.url}")
    private String urlBase;

    @Value("${clients.cartoes.transacao-status.url}")
    private String urlStatusTransacao;

    @Value("${clients.cartoes.transacao-status.total}")
    private String urlStatusTransacaoTotal;

    @Value("${clients.cartoes.transacao-status.operadora}")
    private String urlOperadora;

    @Value("${clients.cartoes.transacao-status.produto}")
    private String urlProduto;

    private RestTemplate restTemplate;

    private Gson gson;

    @Autowired
    public StatusTransacaoResource(@Qualifier("restCartoes") RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    @Override
    public List<CategorizacaoStatusTransacaoDTO> getTotalStatusTransacao(FiltroMultiplasDatasDTO dto) {

        List<CategorizacaoStatusTransacaoDTO> dtos = null;

        ResponseEntity<String> response = restTemplate.postForEntity(urlBase + urlStatusTransacao + urlStatusTransacaoTotal, dto, String.class);
        dtos = gson.fromJson(response.getBody(), new TypeToken<ArrayList<CategorizacaoStatusTransacaoDTO>>(){}.getType());

        return dtos;
    }

    @Override
    public List<OperadoraStatusDTO> pesquisarTotalPorOperadoraStatus(FiltroDataDTO dto, StatusTransacaoEnum transacaoEnum) {

        List<OperadoraStatusDTO> dtos = null;

        ResponseEntity<String> response = restTemplate.postForEntity(urlBase + urlStatusTransacao + urlOperadora + transacaoEnum, dto, String.class);
        dtos = gson.fromJson(response.getBody(), new TypeToken<ArrayList<OperadoraStatusDTO>>(){}.getType());

        return dtos;
    }

    @Override
    public List<ProdutoStatusDTO> pesquisarTotalPorProdutoStatus(FiltroDataDTO dto, Long idOperadora, StatusTransacaoEnum transacaoEnum) {

        List<ProdutoStatusDTO> dtos = null;

        String uri = String.valueOf(urlBase + urlStatusTransacao + urlProduto + transacaoEnum + "/" + idOperadora);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, dto, String.class);
        dtos = gson.fromJson(response.getBody(), new TypeToken<ArrayList<ProdutoStatusDTO>>(){}.getType());

        return dtos;
    }
}
