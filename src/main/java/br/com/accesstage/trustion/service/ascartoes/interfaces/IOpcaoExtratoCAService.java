package br.com.accesstage.trustion.service.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.OpcaoExtratoCA;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author raphael
 */
public interface IOpcaoExtratoCAService {

    List<OpcaoExtratoCA> carregarComboOpcaoExtrato() throws DataAccessException;
}
