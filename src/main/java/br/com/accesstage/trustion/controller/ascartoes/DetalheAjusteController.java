package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.DetalheBilheteDTO;
import br.com.accesstage.trustion.dto.ascartoes.DetalhesNsuDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ascartoes.INsuService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestaovendas")
public class DetalheAjusteController {

    @Log
    private static Logger LOGGER;

    @Autowired
    private INsuService service;

    /**
     * Executa a pesquisa dos detalhes do ajuste de prepara o conteúdo para a
     * apresentação
     *
     * @param filtro
     * @return
     */
    @PostMapping(value = "/pesquisaDetalhesHash", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<DetalhesNsuDTO>> pesquisaDetalhesHash(@RequestBody GestaoVendasDTO filtro) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        List<DetalhesNsuDTO> detalhes = new ArrayList<>();

        try {

            DetalheBilheteDTO detalheBilhete = service.pesquisarDetalhesDoBilhete(filtro.getDscAreaCliente(), filtro.getCodArquivo());

            detalhes = service.consultaDetalhesNsu(filtro, detalheBilhete);

        } catch (ForbiddenResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"tipos de " + UTF8.conciliacao}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return ResponseEntity.ok(detalhes);

    }

}
