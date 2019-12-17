package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.ListaBancoDTO;
import br.com.accesstage.trustion.model.ListaBanco;

public class ListaBancoConverter {

    public static ListaBanco paraEntidade(ListaBancoDTO dto){

        if(dto == null)
            return null;

        ListaBanco entidade = new ListaBanco();
        entidade.setIdListaBanco(dto.getIdListaBanco());
        entidade.setCodigoBanco(dto.getCodigoBanco());
        entidade.setDescricao(dto.getDescricao());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());

        return entidade;
    }

    public static ListaBancoDTO paraDTO(ListaBanco entidade){

        if(entidade == null)
            return null;

        ListaBancoDTO dto = new ListaBancoDTO();
        dto.setIdListaBanco(entidade.getIdListaBanco());
        dto.setCodigoBanco(entidade.getCodigoBanco());
        dto.setDescricao(entidade.getDescricao());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;
    }
}
