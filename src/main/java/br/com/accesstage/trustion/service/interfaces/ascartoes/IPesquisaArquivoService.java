package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.PesquisaArquivoDTO;
import java.util.List;

public interface IPesquisaArquivoService {

    List<PesquisaArquivoDTO> pesquisarArquivo(PesquisaArquivoDTO filtro);

    List<PesquisaArquivoDTO> pesquisarArquivoData(PesquisaArquivoDTO filtro);

}
