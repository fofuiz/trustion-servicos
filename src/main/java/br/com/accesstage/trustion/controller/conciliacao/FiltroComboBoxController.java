package br.com.accesstage.trustion.controller.conciliacao;

import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoModalidadeTarifaDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoMesReferenciaDTO;
import br.com.accesstage.trustion.dto.conciliacao.AdquirenteDTO;
import br.com.accesstage.trustion.asconciliacao.model.MotivoConciliacao;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.service.interfaces.IDomicilioBancarioService;
import br.com.accesstage.trustion.service.interfaces.IFiltroComboBoxService;
import br.com.accesstage.trustion.util.DataUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FiltroComboBoxController {

    @Log
    private static Logger LOGGER;

    @Autowired
    private IDomicilioBancarioService domicilioBancarioService;

    @Autowired
    private IFiltroComboBoxService filtroComboBoxService;

    /**
     * Metodo para carregar o combo de domicílio bancário
     *
     * @return listaBancos
     */
    @RequestMapping(value = "/carregarComboDomicilioBancario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoDomicilioBancarioDTO> carregarComboDomicilioBancario(@Param("empId") Integer empId) {

        FiltroDomicilioBancarioDTO filtroTelaDomicilioBancarioVO = new FiltroDomicilioBancarioDTO();

        filtroTelaDomicilioBancarioVO.setEmpID(empId);

        LOGGER.info(">>FiltroComboBoxController.carregarComboDomicilioBancario");

        List<ResultadoDomicilioBancarioDTO> listaBancos = new ArrayList<ResultadoDomicilioBancarioDTO>();

        try {
            listaBancos = domicilioBancarioService.pesquisarTodosBancos(filtroTelaDomicilioBancarioVO);
        } catch (Exception e) {
            LOGGER.error("FiltroComboBoxController.carregarComboDomicilioBancario - Erro ao buscar dados domic�lio banc�rio." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxController.carregarComboDomicilioBancario");

        return listaBancos;
    }

    /**
     * Metodo para carregar dados Mes Referencia no combo na tela
     *
     * @return listaMes
     */
    @RequestMapping(value = "/carregarComboReferencia", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoMesReferenciaDTO> carregarComboReferencia() {

        LOGGER.info(">>FiltroComboBoxController.carregarComboReferencia");

        List<ResultadoMesReferenciaDTO> listaMes = new ArrayList<ResultadoMesReferenciaDTO>();
        try {

            listaMes = filtroComboBoxService.getMesReferencia();

        } catch (Exception e) {
            LOGGER.error("FiltroComboBoxController.carregarComboReferencia - Erro ao buscar dados combo referencia." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxController.carregarComboReferencia");

        return listaMes;
    }

    /**
     * Metodo para carregar dados Ano referencia no combo na tela
     *
     * @return listaModalidade
     */
    @RequestMapping(value = "/carregarComboAno", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> carregarComboAno() {

        return DataUtil.getAnoReferencia();
    }

    /**
     * Metodo para carregar dados dos adiquirentes no combo da tela
     *
     * @return listaAdiquirentes
     */
    @RequestMapping(value = "/carregarComboAdquirente", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdquirenteDTO> carregarComboAdquirentes() {

        LOGGER.info(">>FiltroComboBoxController.carregarAdiquirentes");

        List<AdquirenteDTO> listaAdquirentes = new ArrayList<AdquirenteDTO>();

        try {
            listaAdquirentes = filtroComboBoxService.listarAdquirentes();
        } catch (Exception e) {
            LOGGER.error("FiltroComboBoxController.carregarComboAdquirente - Erro ao buscar dados combo adquirentes." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxController.carregarComboAdquirente");

        return listaAdquirentes;
    }

    /**
     * Metodo para carregar dados modalidades no combo na tela
     *
     * @return
     */
    @RequestMapping(value = "/carregarComboModalidadeTarifa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResultadoModalidadeTarifaDTO> carregarComboModalidadesTarifas() {

        LOGGER.info(">>FiltroComboBoxController.carregarComboModalidadeTarifas");

        List<ResultadoModalidadeTarifaDTO> listaModalidade = new ArrayList<ResultadoModalidadeTarifaDTO>();

        try {

            listaModalidade = filtroComboBoxService.pesquisarTodasModalidadesTarifas();

        } catch (Exception e) {
            LOGGER.error("FiltroComboBoxController.arregarComboModalidadesTarifas - Erro ao buscar dados combo modalidade." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxController.carregarComboModalidadeTarifas");

        return listaModalidade;
    }

    @RequestMapping(value = "/carregarComboMovivoConciliacao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MotivoConciliacao> listarMotivoConciliacaoPorEmpID(@RequestParam("empId") Long empID) throws Exception {

        LOGGER.info(">>FiltroComboBoxController.listarMotivoConciliacaoPorEmpID");

        List<MotivoConciliacao> listaMotivo = new ArrayList<MotivoConciliacao>();

        try {
            
            listaMotivo = filtroComboBoxService.listarMotivoConciliacaoPorEmpID(empID);
            
        } catch (Exception e) {
            LOGGER.error("FiltroComboBoxController.listarMotivoConciliacaoPorEmpID - Erro ao buscar dados combo motivo conciliacao." + e.getMessage(), e);
        }

        LOGGER.info("<<FiltroComboBoxController.listarMotivoConciliacaoPorEmpID");

        return listaMotivo;
    }
}
