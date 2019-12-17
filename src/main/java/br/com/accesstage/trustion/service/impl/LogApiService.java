package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.LogApiConverter;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.LogApiDTO;
import br.com.accesstage.trustion.model.*;
import br.com.accesstage.trustion.repository.criteria.LogApiSpecification;
import br.com.accesstage.trustion.repository.interfaces.*;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.ILogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogApiService implements ILogApiService {

    @Autowired
    private ILogApiRepository logApiRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private ICofreRepository cofreRepository;

    @Autowired
    private IEmpresaService empresaService;

    private final Converter<LogApi, LogApiDTO> converter = new Converter<LogApi, LogApiDTO>() {

        @Override
        public LogApiDTO convert(LogApi entidade) {
            LogApiDTO dto = LogApiConverter.paraDTO(entidade);

            GrupoEconomico grupo = grupoRepository.findOne(entidade.getIdGrupoEconomico());
            dto.setGrupoEconomico(grupo == null ? null : grupo.getNome());
            Empresa empresa = empresaRepository.findOne(entidade.getIdEmpresa());
            dto.setEmpresa(empresa == null ? null : empresa.getRazaoSocial());
            Cofre cofre = cofreRepository.findOne(entidade.getIdCofre());
            dto.setNumSerialCofre(cofre.getNumSerie() == null ? null : cofre.getNumSerie());

            return dto;
        }
    };

    @Override
    public Page<LogApiDTO> listaLogsSpecs(LogApiDTO dto, Pageable pageable) throws Exception {

        Page<LogApiDTO> dtos = null;
        List<EmpresaDTO> listaEmpresas = new ArrayList<>();
        List<Long> idsEmpresa = new ArrayList<>();


        listaEmpresas = empresaService.listaEmpresasPorUsuarioLogado();
        listaEmpresas.forEach(empresaDTO -> {
            idsEmpresa.add(empresaDTO.getIdEmpresa());
        });

        Specification<LogApi> specsTransportadora = LogApiSpecification.byCriterio(dto, idsEmpresa);
        Page<LogApi> logApis = logApiRepository.findAll(specsTransportadora, pageable);
        dtos = logApis.map(converter);

        return dtos;

    }

}
