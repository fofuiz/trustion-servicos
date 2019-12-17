package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.AtividadeDTO;

public interface IAtividadeService {
	
	AtividadeDTO criar(AtividadeDTO dto) throws Exception;
	AtividadeDTO alterar(AtividadeDTO dto) throws Exception;
	void excluir(Long atividadeId) throws Exception;
	List<AtividadeDTO> listarPorOcorrencia(Long idOcorrencia) throws Exception;
	List<AtividadeDTO> listarTodos() throws Exception;
	AtividadeDTO criarPorAcao(AtividadeDTO dto) throws Exception;
	AtividadeDTO aprovar(AtividadeDTO dto) throws Exception;
	AtividadeDTO rejeitar(AtividadeDTO dto) throws Exception;
}
