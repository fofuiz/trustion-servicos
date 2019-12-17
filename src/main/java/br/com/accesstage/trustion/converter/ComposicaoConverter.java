package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.ComposicaoDTO;
import br.com.accesstage.trustion.model.Composicao;

public class ComposicaoConverter {

    public static Composicao paraEntidade(ComposicaoDTO composicaoDTO) {
        Composicao entidade = new Composicao();
        entidade.setNumeroGVT(composicaoDTO.getNumeroGVT());
        entidade.setCompartimento(composicaoDTO.getCompartimento());
        entidade.setSelo(composicaoDTO.getSelo());
        entidade.setNotas_001(composicaoDTO.getNotas_001());
        entidade.setNotas_002(composicaoDTO.getNotas_002());
        entidade.setNotas_005(composicaoDTO.getNotas_005());
        entidade.setNotas_010(composicaoDTO.getNotas_010());
        entidade.setNotas_020(composicaoDTO.getNotas_020());
        entidade.setNotas_050(composicaoDTO.getNotas_050());
        entidade.setNotas_100(composicaoDTO.getNotas_100());
        entidade.setDataCriacao(composicaoDTO.getDataCriacao());

        return entidade;
    }

    public static ComposicaoDTO paraDTO(Composicao composicao) {

        ComposicaoDTO dto = new ComposicaoDTO();
        dto.setNumeroGVT(composicao.getNumeroGVT());
        dto.setCompartimento(composicao.getCompartimento());
        dto.setSelo(composicao.getSelo());
        dto.setNotas_001(composicao.getNotas_001());
        dto.setNotas_002(composicao.getNotas_002());
        dto.setNotas_005(composicao.getNotas_005());
        dto.setNotas_010(composicao.getNotas_010());
        dto.setNotas_020(composicao.getNotas_020());
        dto.setNotas_050(composicao.getNotas_050());
        dto.setNotas_100(composicao.getNotas_100());
        dto.setDataCriacao(composicao.getDataCriacao());

        return dto;
    }
}
