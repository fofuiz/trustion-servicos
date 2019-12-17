package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.PeriodoResumoCartaoDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;

public interface IPeriodoResumoCartaoService {

    PeriodoResumoCartaoDTO criar(UsuarioDTO usuario) throws Exception;

    PeriodoResumoCartaoDTO alterar(PeriodoResumoCartaoDTO dto) throws Exception;

    PeriodoResumoCartaoDTO pesquisar(Long idUsuario);
}
