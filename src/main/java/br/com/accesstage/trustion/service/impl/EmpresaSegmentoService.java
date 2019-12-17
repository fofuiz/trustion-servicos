package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.EmpresaSegmentoConverter;
import br.com.accesstage.trustion.dto.EmpresaSegmentoDTO;
import br.com.accesstage.trustion.model.EmpresaSegmento;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaSegmentoRepository;
import br.com.accesstage.trustion.service.interfaces.IEmpresaSegmentoService;

@Service
public class EmpresaSegmentoService implements IEmpresaSegmentoService {

    @Autowired
    private IEmpresaSegmentoRepository repository;

    @Override
    public List<EmpresaSegmentoDTO> listarTodos() throws Exception {
        List<EmpresaSegmentoDTO> dtos = new ArrayList<>();
        for (EmpresaSegmento tipo : repository.findAll()) {
            dtos.add(EmpresaSegmentoConverter.paraDTO(tipo));
        }
        return dtos;
    }
}
