package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.ComposicaoConverter;
import br.com.accesstage.trustion.dto.ComposicaoDTO;
import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.model.Composicao;
import br.com.accesstage.trustion.repository.interfaces.IComposicaoRepository;

@Service
public class ComposicaoService {

    @Autowired
    private IComposicaoRepository composicaoRepository;

    private final Converter<Composicao, ComposicaoDTO> converter = (Composicao composicao) -> {
        List<String> listaDenominacao = new ArrayList<>();
        List<Long> listaQuantidade = new ArrayList<>();
        List<Long> listaValor = new ArrayList<>();
        
        ComposicaoDTO composicaoDTOAux = ComposicaoConverter.paraDTO(composicao);
        
        if (composicaoDTOAux.getNotas_001() != null) {
            listaDenominacao.add("Notas de 1");
            listaQuantidade.add(composicaoDTOAux.getNotas_001());
            listaValor.add(composicaoDTOAux.getNotas_001());
        }
        
        if (composicaoDTOAux.getNotas_002() != null) {
            listaDenominacao.add("Notas de 2");
            listaQuantidade.add(composicaoDTOAux.getNotas_002());
            listaValor.add(composicaoDTOAux.getNotas_002() * 2L);
        }
        
        if (composicaoDTOAux.getNotas_005() != null) {
            listaDenominacao.add("Notas de 5");
            listaQuantidade.add(composicaoDTOAux.getNotas_005());
            listaValor.add(composicaoDTOAux.getNotas_005() * 5L);
        }
        
        if (composicaoDTOAux.getNotas_010() != null) {
            listaDenominacao.add("Notas de 10");
            listaQuantidade.add(composicaoDTOAux.getNotas_010());
            listaValor.add(composicaoDTOAux.getNotas_010() * 10L);
        }
        
        if (composicaoDTOAux.getNotas_020() != null) {
            listaDenominacao.add("Notas de 20");
            listaQuantidade.add(composicaoDTOAux.getNotas_020());
            listaValor.add(composicaoDTOAux.getNotas_020() * 20L);
        }
        
        if (composicaoDTOAux.getNotas_050() != null) {
            listaDenominacao.add("Notas de 50");
            listaQuantidade.add(composicaoDTOAux.getNotas_050());
            listaValor.add(composicaoDTOAux.getNotas_050() * 50L);
        }
        
        if (composicaoDTOAux.getNotas_100() != null) {
            listaDenominacao.add("Notas de 100");
            listaQuantidade.add(composicaoDTOAux.getNotas_100());
            listaValor.add(composicaoDTOAux.getNotas_100() * 100L);
        }
        
        composicaoDTOAux.setDenominacao(listaDenominacao);
        composicaoDTOAux.setQuantidade(listaQuantidade);
        composicaoDTOAux.setValor(listaValor);
        
        return composicaoDTOAux;
    };

    public Page<ComposicaoDTO> listarComposicao(DetalheConferenciaDTO detalheConferenciaDTO, Pageable pageable) {

        Page<ComposicaoDTO> lstComposicaoDTO = composicaoRepository.findByNumeroGVT(detalheConferenciaDTO.getNumeroGVT(), pageable).map(converter);

        return lstComposicaoDTO;
    }
}
