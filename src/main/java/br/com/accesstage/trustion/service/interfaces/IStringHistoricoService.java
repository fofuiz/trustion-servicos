package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.StringHistoricoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStringHistoricoService {

    StringHistoricoDTO criar(StringHistoricoDTO dto) throws Exception;

    StringHistoricoDTO alterar(StringHistoricoDTO dto) throws Exception;

    StringHistoricoDTO pesquisar(Long idStringHistorico) throws Exception;

    Page<StringHistoricoDTO> pesquisar(StringHistoricoDTO dto, Pageable pageable) throws Exception;

    boolean excluir(Long idStringHistorico) throws Exception;

}
