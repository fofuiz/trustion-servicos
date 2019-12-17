package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.EmpresaCADTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.repository.impl.EmpresaCARepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IEmpresaCaService;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Setter
public class EmpresaCaService implements IEmpresaCaService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private EmpresaCARepository empresaCARepository;

    @Override
    public Optional<List<EmpresaCADTO>> buscarFiliais(final Long empId) {
        LOGGER.info(LogsEnum.EMPCA001.texto(), empId);
        Objects.requireNonNull(empId);
        return empresaCARepository.buscarFiliais(empId);
    }

    @Override
    public EmpresaCA find(Long id) {
        return empresaCARepository.find(id);
    }

    @Override
    public EmpresaCA merge(EmpresaCA empresaCA) {
        return empresaCARepository.merge(empresaCA);
    }
    
    public EmpresaCA buscaEmpresa(String cnpj) {
        return empresaCARepository.findByCnpj(cnpj);
    }
}
