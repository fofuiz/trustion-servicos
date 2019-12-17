package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import br.com.accesstage.trustion.repository.impl.DomicilioBancarioRepository;
import br.com.accesstage.trustion.service.interfaces.IDomicilioBancarioService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cristiano.silva
 *
 */
@Service
public class DomicilioBancarioService implements IDomicilioBancarioService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private DomicilioBancarioRepository domicilioBancarioDAO;

    /*
     * (non-Javadoc)
     * 
     * @see br.com.accesstage.conciliacao.service.DomicilioBancarioService#
     * perquisarDetalheDomicilioBancario(br.com.accesstage.conciliacao.vos.
     * FiltroDomicilioBancarioVO)
     */
    @Transactional(readOnly = true)
    public List<ResultadoDomicilioBancarioDTO> perquisarDetalheDomicilioBancario(
            FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) throws Exception {
        LOGGER.info(LogsEnum.DOMBANC002.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());
        List<ResultadoDomicilioBancarioDTO> resultado;
        try {
            resultado = converterDomicilioBancarioEntitysParaResultadoDomicilioBancarioVOs(domicilioBancarioDAO
                    .perquisarDetalheDomicilioBancario(filtroDomicilioBancarioDTO));

        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC003.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(), e);
            throw e;
        }
        return resultado;
    }

    /**
     * metodo para converter DomicilioBancario para ResultadoDomicilioBancarioVO
     *
     * @param listaEntitys DomicilioBancario
     * @return lsita ResultadoDomicilioBancarioVO.
     */
    List<ResultadoDomicilioBancarioDTO> converterDomicilioBancarioEntitysParaResultadoDomicilioBancarioVOs(
            List<DomicilioBancario> listaEntitys) throws Exception {
        List<ResultadoDomicilioBancarioDTO> resultado = new ArrayList<>();

        for (DomicilioBancario entity : listaEntitys) {
            ResultadoDomicilioBancarioDTO vo = new ResultadoDomicilioBancarioDTO();
            vo.setDescContaBanco(entity.getDescContaBanco());
            vo.setNroBanco(entity.getNroBanco());
            vo.setNroAgencia(entity.getNroAgencia());
            vo.setNroContaCorrente(entity.getNroContaCorrente());
            vo.setEmpID(entity.getEmpID());
            vo.setStaAtivo(entity.getStaAtivo());
            resultado.add(vo);
        }
        return resultado;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * br.com.accesstage.conciliacao.service.DomicilioBancarioService#pesquisarTodosBancos
     * ()
     */
    @Transactional(readOnly = true)
    public List<ResultadoDomicilioBancarioDTO> pesquisarTodosBancos(FiltroDomicilioBancarioDTO filtroDomicilioBancarioDTO) throws Exception {
        LOGGER.info(LogsEnum.DOMBANC004.texto(),
                filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                filtroDomicilioBancarioDTO.getNroBanco(),
                filtroDomicilioBancarioDTO.getEmpID());

        List<ResultadoDomicilioBancarioDTO> resultado;

        try {
            resultado = converterDomicilioBancarioEntitysParaResultadoDomicilioBancarioVOs(domicilioBancarioDAO.pesquisarTodosBancos(filtroDomicilioBancarioDTO));
        } catch (Exception e) {
            LOGGER.error(LogsEnum.DOMBANC005.texto(),
                    filtroDomicilioBancarioDTO.getCodigoBancoStr(),
                    filtroDomicilioBancarioDTO.getNroBanco(),
                    filtroDomicilioBancarioDTO.getEmpID(), e);
            throw e;
        }
        return resultado;
    }
}
