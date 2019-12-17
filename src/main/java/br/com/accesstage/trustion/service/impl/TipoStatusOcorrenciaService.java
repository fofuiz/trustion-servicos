package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoStatusOcorrencia;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.TipoStatusOcorrenciaConverter;
import br.com.accesstage.trustion.dto.TipoStatusOcorrenciaDTO;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.service.interfaces.ITipoStatusOcorrenciaService;

@Service
public class TipoStatusOcorrenciaService implements ITipoStatusOcorrenciaService {

	@Autowired
	private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

	@Override
	public List<TipoStatusOcorrenciaDTO> listarTodos() throws Exception {

		List<TipoStatusOcorrenciaDTO> dtos = null;

		dtos = new ArrayList<>();
		List<TipoStatusOcorrencia> entidades = tipoStatusOcorrenciaRepository.findAll();

		for(TipoStatusOcorrencia entidade: entidades) {
			if(entidade.getIdTipoStatusOcorrencia() == 5L)
					continue;
			
			dtos.add(TipoStatusOcorrenciaConverter.paraDTO(entidade));
		}

		return dtos;
	}

	@Override
	public List<TipoStatusOcorrenciaDTO> listarReabertura() throws Exception {

		List<TipoStatusOcorrenciaDTO> dtos = new ArrayList<>();

		if(UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() || UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get()){

			dtos.add(TipoStatusOcorrenciaConverter.paraDTO(tipoStatusOcorrenciaRepository.findOne(CodigoTipoStatusOcorrencia.ANALISE_ANDAMENTO.getId())));

		}else if(UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE.get() 
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get()
				|| UsuarioAutenticado.getIdPerfil() == CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get()){

			dtos.add(TipoStatusOcorrenciaConverter.paraDTO(tipoStatusOcorrenciaRepository.findOne(CodigoTipoStatusOcorrencia.ANALISE_SOLICITADA.getId())));

		}

		return dtos;
	}

}
