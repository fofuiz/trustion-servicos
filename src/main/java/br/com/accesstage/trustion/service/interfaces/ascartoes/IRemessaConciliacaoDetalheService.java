package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.RemessaConciliacaoDetalhe;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.RemessaConciliacaoDetalheDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author raphael
 */
public interface IRemessaConciliacaoDetalheService {

    RemessaConciliacaoDetalhe merge(RemessaConciliacaoDetalhe remessa);

    void desconciliarTransacoes(String hashValue);

    List<RemessaConciliacaoDetalheDTO> desagrupaTransacoes(
            String dscAutorizacao, Long codNsu, Long empId, Date dtaVenda, Long codOperadora,
            Long nroPlano, String nomeProduto);

    RemessaConciliacaoDetalhe findById(Long idDetalhe);

    List<RemessaConciliacaoDetalheDTO> fatoTransacaoDesconciliar(GestaoVendasDTO gestaoVendasDTO);

    List<RemessaConciliacaoDetalheDTO> remessaDesconciliar(String hashValue);
    
    List<RemessaConciliacaoDetalheDTO> listaTransacoesASeremConciliadas(Long codDetalhe, Long empId, Date data);
    
    List<RemessaConciliacaoDetalheDTO> listaPossiveisTransacoes(String dscAutorizacao, Long codNsu, Long empId, Date data, Long codLoja, Long codOperadora);
}
