package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.TipoCreditoConverter;
import br.com.accesstage.trustion.dto.TipoCreditoDTO;
import br.com.accesstage.trustion.model.TipoCredito;
import br.com.accesstage.trustion.repository.interfaces.ITipoCreditoRepository;
import br.com.accesstage.trustion.service.interfaces.ITipoCreditoService;

@Service
public class TipoCreditoService implements ITipoCreditoService {
	
	@Autowired
	private ITipoCreditoRepository creditoRepository;

	@Override
	public List<TipoCreditoDTO> listarTodos() throws Exception {
		List<TipoCreditoDTO> dtos = new ArrayList<>();
		for(TipoCredito credito: creditoRepository.findAll()){
			dtos.add(TipoCreditoConverter.paraDTO(credito));
		}
		return dtos;
	}

}
