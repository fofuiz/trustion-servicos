package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.LogApiDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILogApiService {

    Page<LogApiDTO> listaLogsSpecs(LogApiDTO dto, Pageable pageable) throws Exception;
}
