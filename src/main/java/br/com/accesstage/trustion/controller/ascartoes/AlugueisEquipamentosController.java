package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.BandeiraEquipamentosDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroAluguelEquipamentosDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.service.interfaces.IAlugueEquipamentosService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alugueis/equipamentos")
public class AlugueisEquipamentosController {

    @Log
    private static Logger LOGGER;

    @Autowired
    private IAlugueEquipamentosService alugueEquipamentosService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public HttpEntity buscarEquipamentos(FiltroAluguelEquipamentosDTO filtroAluguelEquipamentosDTO){
        LOGGER.info(LogsEnum.ALUQEUIP001.texto(),
                filtroAluguelEquipamentosDTO.getDataDe(),
                filtroAluguelEquipamentosDTO.getDataAte(),
                filtroAluguelEquipamentosDTO.getEmpId());

        try {
            List<BandeiraEquipamentosDTO> resultado = alugueEquipamentosService.buscarEquipamentos(filtroAluguelEquipamentosDTO);
            LOGGER.info(LogsEnum.ALUQEUIP003.texto(),
                    filtroAluguelEquipamentosDTO.getDataDe(),
                    filtroAluguelEquipamentosDTO.getDataAte(),
                    filtroAluguelEquipamentosDTO.getEmpId(),resultado == null ? "0": resultado.size());
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            LOGGER.error(LogsEnum.ALUQEUIP002.texto(),
                    filtroAluguelEquipamentosDTO.getDataDe(),
                    filtroAluguelEquipamentosDTO.getDataAte(),
                    filtroAluguelEquipamentosDTO.getEmpId(),e);
            return ResponseEntity.badRequest().build();
        }
    }
}
