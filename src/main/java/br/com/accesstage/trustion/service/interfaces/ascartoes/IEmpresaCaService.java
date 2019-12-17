package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.dto.ascartoes.EmpresaCADTO;

import java.util.List;
import java.util.Optional;

public interface IEmpresaCaService {

    Optional<List<EmpresaCADTO>> buscarFiliais(final Long empId);

    EmpresaCA find(Long id);

    EmpresaCA merge(EmpresaCA empresa);
}
