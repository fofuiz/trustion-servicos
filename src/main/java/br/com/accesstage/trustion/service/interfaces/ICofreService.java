package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.accesstage.trustion.dto.CofreDTO;
import br.com.accesstage.trustion.dto.EmpresaDTO;

public interface ICofreService {
	CofreDTO criar(CofreDTO cofreDTO) throws Exception;
	CofreDTO alterar(CofreDTO cofreDTO) throws Exception;
	CofreDTO pesquisar(Long cofreDTO) throws Exception;
	boolean excluir(Long idCofre) throws Exception;
	List<CofreDTO> listarCriterios(CofreDTO cofreDTO)throws Exception;
	Page<CofreDTO> listarCriterios(CofreDTO cofreDTO, Pageable pageable) throws Exception;
	List<CofreDTO> listarCriterios(List<EmpresaDTO> listaEmpresaFiltroDTO)throws Exception;
}
