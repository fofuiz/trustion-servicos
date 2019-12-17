package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.asconciliacao.model.MotivoConciliacao;
import br.com.accesstage.trustion.dto.conciliacao.AdquirenteDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoMesReferenciaDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoModalidadeTarifaDTO;

import java.util.List;

public interface IFiltroComboBoxService {

    /**
     * metodo para listar o mes referencia.
     *
     * @return
     * @throws Exception
     */
    List<ResultadoMesReferenciaDTO> getMesReferencia() throws Exception;

    /**
     * metodo para listar adquirente.
     *
     * @return
     * @throws Exception
     */
    List<AdquirenteDTO> listarAdquirentes() throws Exception;

    /**
     * metodo para pesquisar modalidades de tarifa.
     *
     * @return
     * @throws Exception
     */
    List<ResultadoModalidadeTarifaDTO> pesquisarTodasModalidadesTarifas()
            throws Exception;

    /**
     * metodo para listar todos motivos da conciliacao.
     *
     * @return
     * @throws Exception
     */
    List<MotivoConciliacao> listarMotivoConciliacaoPorEmpID(Long empID) throws Exception;

}
