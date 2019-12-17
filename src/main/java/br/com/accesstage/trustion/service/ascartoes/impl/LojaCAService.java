package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.ascartoes.model.LojaCA;
import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.dto.ascartoes.EmpresaCADTO;
import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.PontoVendaCARepository;
import java.util.List;

import br.com.accesstage.trustion.repository.impl.EmpresaCARepository;
import br.com.accesstage.trustion.service.impl.ascartoes.EmpresaCaService;
import br.com.accesstage.trustion.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.repository.ascartoes.interfaces.ILojaCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ILojaCAService;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LojaCAService implements ILojaCAService {

    @Autowired
    private ILojaCARepository lojaRepository;

    @Autowired
    private PontoVendaCARepository pvRepository;

    @Autowired
    private EmpresaCARepository empresaCARepository;

    @Autowired
    private EmpresaCaService empresaCaService;

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboLoja(Long idEmpresa) throws DataAccessException {

        List<LojaOuPontoVendaDTO> dtos = new ArrayList<>();

        List<LojaCA> listLoja = lojaRepository.findByIdEmpresaOrderByNomeAsc(idEmpresa);
        listLoja.forEach((loja) -> dtos.add(new LojaOuPontoVendaDTO(loja.getId(), loja.getNome())));

        List<PontoVendaCA> listPv = pvRepository.listarTodosNulos(idEmpresa);
        listPv.forEach((pv) -> dtos.add(new LojaOuPontoVendaDTO(pv.getIdPontoVenda(), pv.getNumeroTerminal())));

        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboLojaPorCnpjEmpresa(String cnpj) throws DataAccessException {
        EmpresaCA empresa = empresaCaService.buscaEmpresa(Utils.retirarCaracteresEspeciaisString(cnpj));
        return this.carregarComboLoja(empresa.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboSomenteLoja(Long idEmpresa) throws DataAccessException {

        List<LojaOuPontoVendaDTO> dtos = new ArrayList<>();

        List<LojaCA> listLoja = lojaRepository.findByIdEmpresaOrderByNomeAsc(idEmpresa);
        listLoja.forEach((loja) -> dtos.add(new LojaOuPontoVendaDTO(loja.getId(), loja.getNome())));

        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LojaOuPontoVendaDTO> carregarComboSomenteLojaPorCnpjEmpresa(String cnpj) throws DataAccessException {
        EmpresaCA empresa = empresaCaService.buscaEmpresa(Utils.retirarCaracteresEspeciaisString(cnpj));
        return this.carregarComboSomenteLoja(empresa.getId());
    }

}
