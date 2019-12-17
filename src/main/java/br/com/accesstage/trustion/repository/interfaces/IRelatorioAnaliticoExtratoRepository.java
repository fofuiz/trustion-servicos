package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoFiltroDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRelatorioAnaliticoExtratoRepository {

	Page<RelatorioAnaliticoExtratoDTO> pesquisarPorCriterio(RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO, Pageable pageable);
	
	List<RelatorioAnaliticoExtratoDTO> pesquisar(RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO);

}
