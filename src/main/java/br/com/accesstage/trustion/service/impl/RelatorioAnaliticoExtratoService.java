package br.com.accesstage.trustion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoFiltroDTO;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoExtratoRepository;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoExtratoService;

@Service
public class RelatorioAnaliticoExtratoService implements IRelatorioAnaliticoExtratoService {

	@Autowired
	private IRelatorioAnaliticoExtratoRepository relAnaliticoExtratoRepository;

	@Override
	public Page<RelatorioAnaliticoExtratoDTO> listarCriterios(
			RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO, Pageable pageable) throws Exception {
		
		Page<RelatorioAnaliticoExtratoDTO> listarelatorioAnaliticoExtratoDTO = relAnaliticoExtratoRepository.pesquisarPorCriterio(relAnaliticoExtratoFiltroDTO, pageable);
		
		return listarelatorioAnaliticoExtratoDTO;
	}


	@Override
	public List<RelatorioAnaliticoExtratoDTO> exportar(RelatorioAnaliticoExtratoFiltroDTO relAnaliticoExtratoFiltroDTO) {
		return relAnaliticoExtratoRepository.pesquisar(relAnaliticoExtratoFiltroDTO);
	}

}
