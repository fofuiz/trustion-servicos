package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.AtividadeD1DTO;

public interface IAtividadeD1Service {
	
	AtividadeD1DTO alterar(AtividadeD1DTO dto) throws Exception;
	void excluir(Long idAtividade) throws Exception;
	List<AtividadeD1DTO> listarPorOcorrencia(Long idOcorrencia) throws Exception;
	List<AtividadeD1DTO> listarTodos() throws Exception;
	AtividadeD1DTO criarPorAcao(AtividadeD1DTO dto) throws Exception;
}
