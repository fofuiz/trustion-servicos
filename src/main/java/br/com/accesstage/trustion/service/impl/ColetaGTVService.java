package br.com.accesstage.trustion.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.ColetaGTVConverter;
import br.com.accesstage.trustion.dto.ColetaGTVDTO;
import br.com.accesstage.trustion.model.ColetaGTV;
import br.com.accesstage.trustion.repository.criteria.ColetaGTVSpecification;
import br.com.accesstage.trustion.repository.interfaces.IColetaGTVRepository;

import br.com.accesstage.trustion.model.Cofre;
import br.com.accesstage.trustion.repository.interfaces.ICofreRepository;

@Service
public class ColetaGTVService {

    @Autowired
    private IColetaGTVRepository coletaGTVRepository;

    @Autowired
    private ICofreRepository cofreRepository;

    private final Converter<ColetaGTV, ColetaGTVDTO> converter = new Converter<ColetaGTV, ColetaGTVDTO>() {

        @Override
        public ColetaGTVDTO convert(ColetaGTV coletaGTV) {
            ColetaGTVDTO coletaGTVDTO = ColetaGTVConverter.paraDTO(coletaGTV);
            if (coletaGTV.getIdEquipamento() != null && coletaGTV.getNumSerie() != null) {
                Cofre cofre = cofreRepository.findOneByIdEquipamentoAndNumSerie(coletaGTV.getIdEquipamento(), coletaGTV.getNumSerie());
                if (cofre != null) {
                    coletaGTVDTO.setModeloNegocio(cofre.getModeloNegocio().getNome());
                    coletaGTVDTO.setNmeTransportadora(cofre.getModeloNegocio().getTransportadora().getRazaoSocial());
                    coletaGTVDTO.setEmpresa(cofre.getEmpresa().getRazaoSocial());
                }
            }

            return coletaGTVDTO;
        }
    };

    public Page<ColetaGTVDTO> listarColetaGTV(ColetaGTVDTO coletaGTVDTO, Pageable pageable) {

        ColetaGTV coletaGTV = ColetaGTVConverter.paraEntidade(coletaGTVDTO);

        Specification<ColetaGTV> specs = ColetaGTVSpecification.byCriterio(coletaGTV);

        Page<ColetaGTVDTO> listaColetaGTV = coletaGTVRepository.findAll(specs, pageable).map(converter);

        return listaColetaGTV;

    }

    public List<ColetaGTVDTO> listarColetaGTV(ColetaGTVDTO coletaGTVDTO) {

        List<ColetaGTVDTO> lstColetaGTVDTO = new ArrayList<>();

        ColetaGTV coletaGTV = ColetaGTVConverter.paraEntidade(coletaGTVDTO);
        coletaGTV.setColetaDt(LocalDateTime.now());

        Specification<ColetaGTV> specs = ColetaGTVSpecification.byCriterio(coletaGTV);

        List<ColetaGTV> listaColetaGTV = coletaGTVRepository.findAll(specs);

        listaColetaGTV.forEach(coleta -> {
            ColetaGTVDTO gtvDTO = ColetaGTVConverter.paraDTO(coleta);
            lstColetaGTVDTO.add(gtvDTO);
        });

        ColetaGTVDTO aColetaGtv = lstColetaGTVDTO.get(0);
        if (aColetaGtv.getIdEquipamento() != null && aColetaGtv.getNumSerie() != null) {
            Cofre cofre = cofreRepository.findOneByIdEquipamentoAndNumSerie(aColetaGtv.getIdEquipamento(), aColetaGtv.getNumSerie());
            if (cofre != null) {
                aColetaGtv.setModeloNegocio(cofre.getModeloNegocio().getNome());
                aColetaGtv.setNmeTransportadora(cofre.getModeloNegocio().getTransportadora().getRazaoSocial());
                aColetaGtv.setEmpresa(cofre.getEmpresa().getRazaoSocial());
            }
        }

        return lstColetaGTVDTO;

    }
}
