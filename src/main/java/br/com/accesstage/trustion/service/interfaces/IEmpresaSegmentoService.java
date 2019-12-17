package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.EmpresaSegmentoDTO;

public interface IEmpresaSegmentoService {

    List<EmpresaSegmentoDTO> listarTodos() throws Exception;
    
}
