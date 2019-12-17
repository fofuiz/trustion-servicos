package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.accesstage.trustion.dto.CategoriaDTO;

public interface ICategoriaService {

    CategoriaDTO criar(CategoriaDTO categoriaDTO) throws Exception;

    CategoriaDTO alterar(CategoriaDTO categoriaDTO) throws Exception;

    CategoriaDTO pesquisar(Integer idCategoriaDTO) throws Exception;

    boolean excluir(Integer idCategoriaDTO) throws Exception;

    List<CategoriaDTO> listarCriterios(CategoriaDTO categoriaDTO) throws Exception;

    Page<CategoriaDTO> listarCriterios(CategoriaDTO categoriaDTO, Pageable pageable) throws Exception;

}
