package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import java.util.List;
import org.springframework.dao.DataAccessException;

public interface IOperadoraCAService {

    List<OperadoraCA> carregarComboOperadora(boolean flgFiltroPortal) throws DataAccessException;
    
}
