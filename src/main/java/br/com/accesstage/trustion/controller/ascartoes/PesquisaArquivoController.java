package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.PesquisaArquivoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IPesquisaArquivoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PesquisaArquivoController {

    @Autowired
    private IPesquisaArquivoService service;

    @Log
    private static Logger LOGGER;

    /**
     * Pesquisa arquivos tela principal
     *
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/gestaovendas/pesquisaArquivo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<PesquisaArquivoDTO>> pesquisaArquivo(@Valid @RequestBody PesquisaArquivoDTO filtro, BindingResult camposInvalidos) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        if (camposInvalidos.hasErrors()) {
            LOGGER.error(Mensagem.get(camposInvalidos));
            throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
        }

        List<PesquisaArquivoDTO> dtos;

        try {
            if (!StringUtils.isEmpty(filtro.getNomeArquivo()) || !StringUtils.isEmpty(filtro.getSequencial())) {
                dtos = service.pesquisarArquivo(filtro);
            } else {
                dtos = service.pesquisarArquivoData(filtro);
            }

        } catch (BadRequestResponseException e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"PesquisarArquivo"}));
        }

        LOGGER.info(Utils.getFimMetodo());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
