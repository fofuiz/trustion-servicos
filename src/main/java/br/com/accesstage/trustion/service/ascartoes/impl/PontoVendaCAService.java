package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.PontoVendaCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IPontoVendaCAService;
import br.com.accesstage.trustion.util.Funcoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PontoVendaCAService implements IPontoVendaCAService {

    @Autowired
    private PontoVendaCARepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboPontoVenda(Long idEmpresa) throws DataAccessException {

        List<LojaOuPontoVendaDTO> dtos = new ArrayList<>();

        List<PontoVendaCA> list = repository.listarTodosNulos(idEmpresa);
        list.forEach((pv) -> {
            dtos.add(new LojaOuPontoVendaDTO(pv.getIdPontoVenda(), Funcoes.removeZerosEsquerda(pv.getNumeroTerminal())));
        });

        return dtos;

    }

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboPontoVendaPorCodLoja(Long codLoja) throws DataAccessException {

        List<LojaOuPontoVendaDTO> dtos = new ArrayList<>();

        List<PontoVendaCA> list = repository.buscarPorCodLoja(codLoja);
        list.forEach((pv) -> {
            dtos.add(new LojaOuPontoVendaDTO(pv.getIdPontoVenda(), Funcoes.removeZerosEsquerda(pv.getNumeroTerminal())));
        });

        return dtos;

    }

}
