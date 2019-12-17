package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.CategoriaDTO;
import br.com.accesstage.trustion.enums.StatusAtivoInativo;
import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.model.Categoria;

public class CategoriaConverter {

    public static Categoria paraEntidade(CategoriaDTO dto) {

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.getIdCategoria());
        categoria.setDescricao(dto.getDescricao());
        categoria.setDataCriacao(dto.getDataCriacao());
        categoria.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        categoria.setDataAlteracao(dto.getDataAlteracao());
        categoria.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        categoria.setStatus(dto.getStatus());
        categoria.setIdTipoCategoria(dto.getIdTipoCategoria());

        return categoria;
    }

    public static CategoriaDTO paraDTO(Categoria categoria) {

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setIdCategoria(categoria.getIdCategoria());
        categoriaDTO.setDescricao(categoria.getDescricao());
        categoriaDTO.setDataCriacao(categoria.getDataCriacao());
        categoriaDTO.setIdUsuarioCriacao(categoria.getIdUsuarioCriacao());
        categoriaDTO.setDataAlteracao(categoria.getDataAlteracao());
        categoriaDTO.setIdUsuarioAlteracao(categoria.getIdUsuarioAlteracao());
        categoriaDTO.setStatus(categoria.getStatus());
        categoriaDTO.setIdTipoCategoria(categoria.getIdTipoCategoria());

        if (categoriaDTO.getStatus() != null) {
            if (categoriaDTO.getStatus().equals(StatusUsuarioEnum.ATIVO.get())) {
                categoriaDTO.setStatusDescricao(StatusAtivoInativo.ATIVO.getTexto());
            } else {
                categoriaDTO.setStatusDescricao(StatusAtivoInativo.INATIVO.getTexto());
            }
        }

        return categoriaDTO;
    }
}
