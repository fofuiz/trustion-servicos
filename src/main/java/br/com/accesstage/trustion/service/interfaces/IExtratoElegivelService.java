package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.ExtratoElegivelDTO;
import br.com.accesstage.trustion.model.ExtratoElegivel;

import java.util.List;

public interface IExtratoElegivelService {

    List<ExtratoElegivelDTO> listarPorIdRelatorioAnaliticoD1(Long idRelatorioAnaliticoD1) throws Exception;

    List<ExtratoElegivelDTO> listarPorIdRelatorioAnaliticoD0(Long idRelatorioAnaliticoD0) throws Exception;

    void desconciliar(Long idConciliacao) throws Exception;

    void desconciliarD0(Long idConciliacao) throws Exception;

    List<ExtratoElegivelDTO> pesquisarExtratosParaConciliacao(Long idRelatorioAnaliticoD1)throws Exception;

    List<ExtratoElegivelDTO> pesquisarExtratosParaConciliacaoD0(Long idRelatorioAnalitcoD0)throws Exception;

    void conciliar(Long idRelatorioAnaliticoD1, List<ExtratoElegivelDTO> idExtratos) throws Exception;

    void conciliarD0(Long idRelatorioAnaliticoD0, List<ExtratoElegivelDTO> idExtratos) throws Exception;

}
