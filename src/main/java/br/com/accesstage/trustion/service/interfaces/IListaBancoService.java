package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.ListaBancoDTO;
import java.util.List;

public interface IListaBancoService {

    List<ListaBancoDTO> listarTodos() throws Exception;

    ListaBancoDTO pesquisar(Long idListaBanco) throws Exception;

    ListaBancoDTO pesquisarPorCodigoBanco(Integer codigoBanco) throws Exception;
}
