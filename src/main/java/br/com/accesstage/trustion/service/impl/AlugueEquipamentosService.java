package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.BandeiraEquipamentosDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroAluguelEquipamentosDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.repository.impl.AluguelEquipamentosRepositoty;
import br.com.accesstage.trustion.service.interfaces.IAlugueEquipamentosService;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IEmpresaCaService;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Setter
public class AlugueEquipamentosService implements IAlugueEquipamentosService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private AluguelEquipamentosRepositoty aluguelEquipamentosRepositoty;

    @Autowired
    private IEmpresaCaService empresaCaService;

    public List<BandeiraEquipamentosDTO> buscarEquipamentos(FiltroAluguelEquipamentosDTO filtroAluguelEquipamentosDTO) {
        LOGGER.info(LogsEnum.ALUQEUIP004.texto(),
                filtroAluguelEquipamentosDTO.getDataDe(),
                filtroAluguelEquipamentosDTO.getDataAte(),
                filtroAluguelEquipamentosDTO.getEmpId());
        String filiais = montaEmpIdFiliais(filtroAluguelEquipamentosDTO.getEmpId());
        LOGGER.info(LogsEnum.ALUQEUIP005.texto(),
                filtroAluguelEquipamentosDTO.getDataDe(),
                filtroAluguelEquipamentosDTO.getDataAte(),
                filtroAluguelEquipamentosDTO.getEmpId(), filiais);
        return aluguelEquipamentosRepositoty.buscarAluguelEquipamento(filtroAluguelEquipamentosDTO, filiais);
    }

    // Monta uma string dos ids das filiais separando por virgula.
    private String montaEmpIdFiliais(Long empId) {
        return empresaCaService.buscarFiliais(empId)
                .orElse(new ArrayList<>())
                .stream().map(empresaCADTO -> String.valueOf(empresaCADTO.getId()))
                .collect(Collectors.joining(","));
    }
}
