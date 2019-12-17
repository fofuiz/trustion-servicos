package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.ConciliadoDTO;

public interface IConciliadoService {

    ConciliadoDTO criar(ConciliadoDTO conciliadoDTO)throws Exception;
    ConciliadoDTO pesquisar(Long idConciliado) throws Exception;
    void deletar(Long idConciliado) throws Exception;

}
