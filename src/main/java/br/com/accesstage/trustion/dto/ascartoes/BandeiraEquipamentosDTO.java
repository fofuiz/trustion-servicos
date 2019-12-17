package br.com.accesstage.trustion.dto.ascartoes;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class BandeiraEquipamentosDTO implements Serializable {
    private List<AluguelEquipamentosDTO> aluguelEquipamentos;
    private BigDecimal totalBandeira;
}
