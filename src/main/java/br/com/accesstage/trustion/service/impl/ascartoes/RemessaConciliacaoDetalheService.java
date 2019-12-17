package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.RemessaConciliacaoDetalhe;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.RemessaConciliacaoDetalheDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.RemessaConciliacaoDetalheRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IRemessaConciliacaoDetalheService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class RemessaConciliacaoDetalheService implements IRemessaConciliacaoDetalheService {

    @Autowired
    private RemessaConciliacaoDetalheRepository repository;

    @Override
    public RemessaConciliacaoDetalhe merge(RemessaConciliacaoDetalhe remessa) {
        return repository.merge(remessa);
    }

    @Override
    public void desconciliarTransacoes(String hashValue) {
        repository.desconciliarTransacoes(hashValue);
    }

    @Override
    public List<RemessaConciliacaoDetalheDTO> desagrupaTransacoes(String dscAutorizacao,
            Long codNsu, Long empId, Date dtaVenda, Long codOperadora, Long nroPlano, String nomeProduto) {
        return repository.desagrupaTransacoes(dscAutorizacao, codNsu, empId, dtaVenda, codOperadora, nroPlano, nomeProduto);
    }

    @Override
    public RemessaConciliacaoDetalhe findById(Long idDetalhe) {
        return repository.findById(idDetalhe);
    }

    @Override
    public List<RemessaConciliacaoDetalheDTO> fatoTransacaoDesconciliar(GestaoVendasDTO gestaoVendasDTO) {
        return repository.fatoTransacaoDesconciliar(gestaoVendasDTO);
    }

    @Override
    public List<RemessaConciliacaoDetalheDTO> remessaDesconciliar(String hashValue) {
        return repository.remessaDesconciliar(hashValue);
    }

    @Override
    public List<RemessaConciliacaoDetalheDTO> listaTransacoesASeremConciliadas(Long codDetalhe, Long empId, Date data) {
        return repository.listaTransacoesASeremConciliadas(codDetalhe, empId, data);
    }

    @Override
    public List<RemessaConciliacaoDetalheDTO> listaPossiveisTransacoes(String dscAutorizacao, Long codNsu, Long empId, Date data, Long codLoja, Long codOperadora) {
        return repository.listaPossiveisTransacoes(dscAutorizacao, codNsu, empId, data, codLoja, codOperadora);
    }
}
