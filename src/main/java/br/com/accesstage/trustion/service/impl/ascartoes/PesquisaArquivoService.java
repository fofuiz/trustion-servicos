package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.PesquisaArquivoDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.PesquisaArquivoRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IPesquisaArquivoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PesquisaArquivoService implements IPesquisaArquivoService {

    @Autowired
    private PesquisaArquivoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<PesquisaArquivoDTO> pesquisarArquivo(PesquisaArquivoDTO filtro) {
        return repository.pesquisarArquivo(filtro.getNomeArquivo(), filtro.getSequencial(), filtro.getEmpId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PesquisaArquivoDTO> pesquisarArquivoData(PesquisaArquivoDTO filtro) {
        return repository.pesquisarArquivoData(filtro.getDataInicial(), filtro.getDataFinal(), filtro.getEmpId());
    }

}
