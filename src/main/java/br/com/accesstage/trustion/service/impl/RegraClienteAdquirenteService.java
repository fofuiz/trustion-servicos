package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.asconciliacao.model.RegraClienteAdquirenteEntity;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelaRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoRegraClienteAdquirenteDTO;
import br.com.accesstage.trustion.repository.impl.RegraClienteAdquirenteRepository;
import br.com.accesstage.trustion.service.interfaces.IRegraClienteAdquirenteService;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.dao.DataAccessException;

@Service
public class RegraClienteAdquirenteService implements IRegraClienteAdquirenteService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private RegraClienteAdquirenteRepository regraClienteAdquirenteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoRegraClienteAdquirenteDTO> pesquisarRegraClienteAdquirente(FiltroTelaRegraClienteAdquirenteDTO filtro) throws DataAccessException {

        List<ResultadoRegraClienteAdquirenteDTO> resultado = new ArrayList<>();

        try {

            resultado = converterRegraClienteAdquirenteEntity(regraClienteAdquirenteRepository.pesquisarRegraClienteAdquirente(filtro));

        } catch (Exception e) {
            LOGGER.error(">>RegraClienteAdquirenteRepository.pesquisarRegraClienteAdquirente - Erro ao pesquisar registros.", e);
        }

        return resultado;
    }

    /**
     * metodo para converter RegraClienteAdquirenteEntity para
     * ResultadoRegraClienteAdquirenteDTO
     *
     * @param listaEntitys RegraClienteAdquirenteEntity
     * @return lsita ResultadoRegraClienteAdquirenteVO.
     */
    private List<ResultadoRegraClienteAdquirenteDTO> converterRegraClienteAdquirenteEntity(
            List<RegraClienteAdquirenteEntity> listaEntitys) throws Exception {

        List<ResultadoRegraClienteAdquirenteDTO> resultado = new ArrayList<>();

        listaEntitys.stream().map(entity -> {
            ResultadoRegraClienteAdquirenteDTO dto = new ResultadoRegraClienteAdquirenteDTO();
            dto.setCodBanco(entity.getCodBanco());
            dto.setCodOperadora(entity.getCodOperadora());
            dto.setNmeExibicaoPortal(entity.getNmeExibicaoPortal());
            dto.setEmpId(entity.getEmpId());
            dto.setDscChavePrincipal(entity.getDscChavePrincipal());
            dto.setDscChaveSecundaria(entity.getDscChaveSecundaria());
            dto.setStaAtivo(entity.getStaAtivo());
            return dto;
        }).forEachOrdered(dto -> {
            resultado.add(dto);
        });

        return resultado;
    }

}
