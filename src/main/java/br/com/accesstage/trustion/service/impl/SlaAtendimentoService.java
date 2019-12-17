package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.model.SlaAtendimento;
import br.com.accesstage.trustion.repository.interfaces.ISlaAtendimentoRepository;
import br.com.accesstage.trustion.service.interfaces.ISlaAtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlaAtendimentoService implements ISlaAtendimentoService {

    @Autowired
    private ISlaAtendimentoRepository slaAtendimentoRepository;

    @Override
    public SlaAtendimento criar(SlaAtendimento slaAtendimento) {
        return slaAtendimentoRepository.save(slaAtendimento);
    }

    @Override
    public SlaAtendimento alterar(SlaAtendimento slaAtendimento) {
        return slaAtendimentoRepository.save(slaAtendimento);
    }

}
