package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.dto.NumerarioBananinhasGtvDTO;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.model.Pac;
import br.com.accesstage.trustion.service.interfaces.INumerarioBananinhaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/numerario/bananinha")
public class NumerarioBananinhaController {

    @Autowired
    private INumerarioBananinhaService numerarioBananinha;

    @GetMapping(value = "/{gtv}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<NumerarioBananinhasGtvDTO>> listarNumerarioBananinhas(@PathVariable int gtv, Pageable paginacao) {

        log.info(Utils.getInicioMetodo());

        Page<NumerarioBananinhasGtvDTO> listDTOConsolidado;

        try {
            Page<Pac> listVideoGTV = this.numerarioBananinha.findAllByGtv(gtv, paginacao);

            if (listVideoGTV.getContent().isEmpty()){
                return new ResponseEntity(listVideoGTV, HttpStatus.OK);
            }

            listDTOConsolidado = NumerarioBananinhasGtvDTO.converter(listVideoGTV);

        } catch (ForbiddenResponseException | ResourceNotFoundException e) {
            log.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[] { "Numerario Bananinhas por GTV" }));
        }

        log.info(Utils.getFimMetodo());

        return new ResponseEntity(listDTOConsolidado, HttpStatus.OK);
    }


    @GetMapping(value = "/exportar/{gtv}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NumerarioBananinhasGtvDTO>> listarNumerarioBananinhasExportar(@PathVariable int gtv) {

        log.info(Utils.getInicioMetodo());

        List<NumerarioBananinhasGtvDTO> listDTO;

        try {
            List<Pac> listVideoGTV = this.numerarioBananinha.findAllByGtv(gtv);

            if (listVideoGTV.isEmpty()){
                return new ResponseEntity(listVideoGTV, HttpStatus.OK);
            }

            listDTO = NumerarioBananinhasGtvDTO.converter(listVideoGTV);

        } catch (ForbiddenResponseException | ResourceNotFoundException e) {
            log.error("Exceção " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exceção " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(
                    Mensagem.get("msg.nao.foi.possivel.listar", new Object[] { "Numerario Bananinhas por GTV - Exportar" }));
        }

        log.info(Utils.getFimMetodo());

        return new ResponseEntity(listDTO, HttpStatus.OK);
    }


}
