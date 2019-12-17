package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGrupoEconomicoService {

    GrupoEconomicoDTO criar(GrupoEconomicoDTO grupoTO) throws Exception;

    GrupoEconomicoDTO alterar(GrupoEconomicoDTO grupoTO) throws Exception;

    GrupoEconomicoDTO pesquisar(Long idGrupo) throws Exception;

    boolean excluir(Long idGrupoEconomico) throws Exception;

    List<GrupoEconomicoDTO> listarTodos() throws Exception;

    List<GrupoEconomicoDTO> listarGruposSpecs(GrupoEconomicoDTO dto) throws Exception;

    Page<GrupoEconomicoDTO> listarGruposSpecs(GrupoEconomicoDTO dto, Pageable pageable) throws Exception;

    List<GrupoEconomicoDTO> listarGruposSpecsOutros(GrupoEconomicoDTO dto) throws Exception;

    List<GrupoEconomicoDTO> listarPorUsuario(Long idUsuario) throws Exception;

    List<GrupoEconomicoDTO> listarPorPerfilUsuario(Long idPerfil) throws Exception;

    List<GrupoEconomicoDTO> listarTodosPorIdOcorrencia(Long idOcorrencia) throws Exception;
}
