package br.com.accesstage.trustion.controller.conciliacao;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.service.interfaces.IDomicilioBancarioService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/domicilios/bancarios")
public class DomicilioBancarioController {

    @Log
    private static Logger LOGGER;

    @Autowired
    private IDomicilioBancarioService domicilioBancarioService;

    @GetMapping("/detalhe")
    public HttpEntity buscarDetalheDomiciliosBancarios(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO){
        LOGGER.info(LogsEnum.DOMBANC001.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());

        try {
            validarEmpId(filtroDomicilioBancarioDTO);
            List<ResultadoDomicilioBancarioDTO> listaDetalheDomicilioBancario = domicilioBancarioService.perquisarDetalheDomicilioBancario(filtroDomicilioBancarioDTO);
            LOGGER.info(LogsEnum.DOMBANC001.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(),listaDetalheDomicilioBancario == null ? "0": listaDetalheDomicilioBancario.size());
            return ResponseEntity.ok(listaDetalheDomicilioBancario);
        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC006.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(),e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value={"","/"})
    public HttpEntity buscarTodosDomiciliosBancarios(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO){
        LOGGER.info(LogsEnum.DOMBANC012.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());

        try {
            validarEmpId(filtroDomicilioBancarioDTO);
            List<ResultadoDomicilioBancarioDTO> listaDetalheDomicilioBancario = domicilioBancarioService.pesquisarTodosBancos(filtroDomicilioBancarioDTO);
            LOGGER.info(LogsEnum.DOMBANC014.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(),listaDetalheDomicilioBancario == null ? "0": listaDetalheDomicilioBancario.size());
            return ResponseEntity.ok(listaDetalheDomicilioBancario);
        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC013.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(),e);
            return ResponseEntity.badRequest().build();
        }
    }

    private void validarEmpId(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO){
        if(filtroDomicilioBancarioDTO.getEmpID() == null){
            throw new ResourceNotFoundException("EmpId obrigatório");
        }
    }
}
