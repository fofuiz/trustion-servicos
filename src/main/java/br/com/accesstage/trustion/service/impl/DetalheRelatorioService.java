package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.DetalheRelatorioConverter;
import br.com.accesstage.trustion.dto.DetalheRelatorioDTO;
import br.com.accesstage.trustion.model.DetalheRelatorio;
import br.com.accesstage.trustion.repository.interfaces.IDetalheRelatorioRepository;
import br.com.accesstage.trustion.service.interfaces.IDetalheRelatorioService;

@Service
public class DetalheRelatorioService implements IDetalheRelatorioService {

	@Autowired
	private IDetalheRelatorioRepository detalheRelatorioRepository;

	private Converter<DetalheRelatorio, DetalheRelatorioDTO> converter = new Converter<DetalheRelatorio, DetalheRelatorioDTO>() {

		@Override
		public DetalheRelatorioDTO convert(DetalheRelatorio detalheRelatorio) {

			DetalheRelatorioDTO  retalheRelatorioDTOAux = DetalheRelatorioConverter.paraDTO(detalheRelatorio);


			return retalheRelatorioDTOAux;
		}
	};

	@Override
	public List<DetalheRelatorioDTO> listarPorCodigoFechamento(Long codigoFechamento) throws Exception {

		List<DetalheRelatorioDTO> listaDetalheRelatorioDTO = new ArrayList<>();

		for (DetalheRelatorio detalheRelatorio : detalheRelatorioRepository.findAllByCodigoFechamento(codigoFechamento)) {

			listaDetalheRelatorioDTO.add(DetalheRelatorioConverter.paraDTO(detalheRelatorio));
		}

		return listaDetalheRelatorioDTO;
	}


	@Override
	public Page<DetalheRelatorioDTO> listarPorCodigoFechamento(Long codigoFechamento,String numSerie, Pageable pageable) throws Exception {
		Page<DetalheRelatorioDTO> detalheDTOPage;
		Page<DetalheRelatorio> listaDetalheRelatorioDTO = detalheRelatorioRepository.findAllByCodigoFechamento(codigoFechamento, numSerie, pageable);

		detalheDTOPage = listaDetalheRelatorioDTO.map(converter);

		return detalheDTOPage;
	}
}
