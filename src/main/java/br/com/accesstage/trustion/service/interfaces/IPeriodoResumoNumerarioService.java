package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.PeriodoResumoNumerarioDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.model.Usuario;

public interface IPeriodoResumoNumerarioService {

    PeriodoResumoNumerarioDTO criar(UsuarioDTO usuario) throws Exception;

    PeriodoResumoNumerarioDTO alterar(PeriodoResumoNumerarioDTO dto) throws Exception;

    PeriodoResumoNumerarioDTO pesquisar(Long idUsuario);



}
