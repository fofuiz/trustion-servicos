package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoFiltroDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRelatorioAnaliticoExtratoService {

    Page<RelatorioAnaliticoExtratoDTO> listarCriterios(RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO, Pageable pageable) throws Exception;

    List<RelatorioAnaliticoExtratoDTO> exportar(RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO) throws Exception;

}
