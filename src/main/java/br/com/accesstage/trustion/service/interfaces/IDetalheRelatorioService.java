package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.accesstage.trustion.dto.DetalheRelatorioDTO;

public interface IDetalheRelatorioService {

	List<DetalheRelatorioDTO> listarPorCodigoFechamento(Long codigoFechamento) throws Exception;
	Page<DetalheRelatorioDTO> listarPorCodigoFechamento(Long codigoFechamento,String numSerie, Pageable pageable) throws Exception;
}
