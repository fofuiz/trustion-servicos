package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.asconciliacao.model.ModalidadeTarifa;
import br.com.accesstage.trustion.asconciliacao.model.MotivoConciliacao;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.conciliacao.AdquirenteDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoMesReferenciaDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoModalidadeTarifaDTO;
import br.com.accesstage.trustion.enums.MesReferenciaEnum;
import br.com.accesstage.trustion.repository.impl.FiltroComboBoxRepository;
import br.com.accesstage.trustion.service.interfaces.IFiltroComboBoxService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FiltroComboBoxService implements IFiltroComboBoxService {

    @Log
    private static Logger LOGGER;

    @Autowired
    private FiltroComboBoxRepository filtroComboBoxRepository;

    /**
     * Metodo para retornar a lista de meses do ano
     *
     * @return listaMes
     */
    public List<ResultadoMesReferenciaDTO> getMesReferencia() throws Exception {

        List<ResultadoMesReferenciaDTO> listaMes = new ArrayList<ResultadoMesReferenciaDTO>();

        for (MesReferenciaEnum mes : MesReferenciaEnum.values()) {
            ResultadoMesReferenciaDTO mesRef = new ResultadoMesReferenciaDTO();
            mesRef.setCodigoMes(mes.getCodigoMes());
            mesRef.setLabelMes(mes.getLabelMes());
            listaMes.add(mesRef);
        }

        return listaMes;
    }

    /**
     * metodo para listar todos adquirentes.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AdquirenteDTO> listarAdquirentes() throws Exception {
        return filtroComboBoxRepository.listarAdquirentes();
    }

    @Transactional(readOnly = true)
    public List<ResultadoModalidadeTarifaDTO> pesquisarTodasModalidadesTarifas() {
        LOGGER.info(">>FiltroComboBoxServiceImpl.pesquisarTodasModalidadesTarifas");
        List<ResultadoModalidadeTarifaDTO> resultado = new ArrayList<ResultadoModalidadeTarifaDTO>();
        try {
            resultado = converterModalidadeTarifaEntityParaResultadoModalidadeTarifaVOs(filtroComboBoxRepository.pesquisarTodasModalidadesTarifas());
        } catch (Exception e) {
            LOGGER.error(">>FiltroComboBoxServiceImpl.pesquisarTodasModalidadesTarifas - Erro ao pesquisar Modalidades." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxServiceImpl.pesquisarTodasModalidadesTarifas");
        return resultado;
    }

    /**
     * metodo para converter ModalidadeTarifaEntity para
     * ResultadoModalidadeTarifaVO
     *
     * @param listaEntitys ModalidadeTarifaEntity
     * @return lsita ResultadoModalidadeTarifaVO.
     */
    private List<ResultadoModalidadeTarifaDTO> converterModalidadeTarifaEntityParaResultadoModalidadeTarifaVOs(
            List<ModalidadeTarifa> listaEntitys) {
        LOGGER.info(">>FiltroComboBoxServiceImpl.converterModalidadeTarifaEntityParaResultadoModalidadeTarifaVOs");
        List<ResultadoModalidadeTarifaDTO> resultado = new ArrayList<ResultadoModalidadeTarifaDTO>();

        for (ModalidadeTarifa entity : listaEntitys) {
            ResultadoModalidadeTarifaDTO vo = new ResultadoModalidadeTarifaDTO();
            vo.setCodigoModalidadeStr(entity.getCodigoModalidadeStr());
            vo.setDescricaoModalidade(entity.getDescricaoModalidade());
            vo.setTipoArrecadacao(entity.getTipoArrecadacao());
            resultado.add(vo);
        }

        LOGGER.info("<<FiltroComboBoxServiceImpl.converterModalidadeTarifaEntityParaResultadoModalidadeTarifaVOs");
        return resultado;
    }

    /**
     * metodo para listar todos motivos da conciliacao.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<MotivoConciliacao> listarMotivoConciliacaoPorEmpID(Long empID) throws Exception {
        return filtroComboBoxRepository.listarMotivoConciliacaoPorEmpID(empID);
    }

}
