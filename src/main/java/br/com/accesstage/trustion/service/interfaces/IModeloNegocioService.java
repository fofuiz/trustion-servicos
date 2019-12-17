package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.TipoCredito;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IModeloNegocioService {

    ModeloNegocioDTO criar(ModeloNegocioDTO dto) throws Exception;

    ModeloNegocioDTO alterar(ModeloNegocioDTO dto) throws Exception;

    ModeloNegocioDTO pesquisar(Long idModeloNegocio) throws Exception;

    boolean excluir(Long idModeloNegocio) throws Exception;

    List<ModeloNegocioDTO> listarCriterio(ModeloNegocioDTO dto) throws Exception;

    Page<ModeloNegocioDTO> listarCriterio(ModeloNegocioDTO dto, Pageable pageable) throws Exception;

    List<Long> listarIdsModeloNegocioPorTipoCredito(TipoCredito tipoCredito);

    Set<ModeloNegocioDTO> listarPorIdEmpresa() throws Exception;
    
    Set<ModeloNegocioDTO> listarPorIdEmpresa(Long idEmpresa) throws Exception;

    List<ModeloNegocioDTO> listarPorIdGrupo(Long idGrupo) throws Exception;

    ModeloNegocio buscaModeloNegocioByEmpresaETipoCredito(Long idEmpresa, Long idTipoCredito);
}
