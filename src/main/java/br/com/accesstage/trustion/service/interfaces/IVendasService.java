package br.com.accesstage.trustion.service.interfaces;

import org.springframework.data.domain.Page;

import br.com.accesstage.trustion.model.MovimentoDiario;

public interface IVendasService {

	public Page<MovimentoDiario> findAll();

}
