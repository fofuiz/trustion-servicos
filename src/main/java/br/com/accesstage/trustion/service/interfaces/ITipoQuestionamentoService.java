package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.TipoQuestionamentoDTO;
import java.util.List;

public interface ITipoQuestionamentoService {

    List<TipoQuestionamentoDTO> listarTodos() throws Exception;

    List<TipoQuestionamentoDTO> listarPorDescricao(final String descricao);
    
    TipoQuestionamentoDTO criar(TipoQuestionamentoDTO tipoQuestionamentoDTO) throws Exception;
    
    TipoQuestionamentoDTO pesquisar (Long id) throws Exception;
    
    TipoQuestionamentoDTO alterar (TipoQuestionamentoDTO tipoQuestionamentoDTO) throws Exception;
}
