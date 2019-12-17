package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.ascartoes.BandeiraEquipamentosDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroAluguelEquipamentosDTO;

import java.util.List;

public interface IAlugueEquipamentosService {
    List<BandeiraEquipamentosDTO> buscarEquipamentos(FiltroAluguelEquipamentosDTO filtroAluguelEquipamentosDTO);
}
