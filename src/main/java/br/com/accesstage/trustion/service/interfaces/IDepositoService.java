package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.DepositoDTO;
import java.util.List;

public interface IDepositoService {

    List<DepositoDTO> consultaApi(DepositoDTO dto);
}
