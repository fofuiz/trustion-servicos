package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.ConciliadoConverter;
import br.com.accesstage.trustion.dto.ConciliadoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Conciliado;
import br.com.accesstage.trustion.repository.interfaces.IConciliadoRepository;
import br.com.accesstage.trustion.service.interfaces.IConciliadoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConciliadoService implements IConciliadoService {

    @Autowired
    private IConciliadoRepository conciliadoRepository;


    @Override
    public ConciliadoDTO criar(ConciliadoDTO conciliadoDTO) throws Exception {

        Conciliado conciliado = conciliadoRepository.save(ConciliadoConverter.paraEntidade(conciliadoDTO));
        if(conciliado == null)
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.conciliacao}));

        return ConciliadoConverter.paraDTO(conciliado);
    }

    @Override
    public ConciliadoDTO pesquisar(Long idConciliado) throws Exception {

        ConciliadoDTO conciliadoDTO = ConciliadoConverter.paraDTO(conciliadoRepository.findOne(idConciliado));

        if(conciliadoDTO == null)
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.conciliacao}));

        return conciliadoDTO;
    }

    @Override
    public void deletar(Long idConciliado) throws Exception {
        conciliadoRepository.delete(idConciliado);
    }
}
