package br.com.accesstage.trustion.service.impl;

import java.util.List;

import br.com.accesstage.trustion.converter.TipoMotivoConclusaoConverter;
import br.com.accesstage.trustion.dto.TipoMotivoConclusaoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.model.TipoMotivoConclusao;
import br.com.accesstage.trustion.repository.interfaces.ITipoMotivoConclusaoRespository;
import br.com.accesstage.trustion.service.interfaces.ITipoMotivoConclusaoService;

@Service
public class TipoMotivoConclusaoService implements ITipoMotivoConclusaoService {

	@Autowired
	private ITipoMotivoConclusaoRespository tipoMotivoConclusaoRespository;

	@Override
	public List<TipoMotivoConclusao> listarMotivos() throws Exception {
		List<TipoMotivoConclusao> listMotivoConclusao = tipoMotivoConclusaoRespository.findAll();
		return listMotivoConclusao;
	}

	@Override
	public List<TipoMotivoConclusao> listarMotivosPorDescricao(String descricao) throws Exception {
		List<TipoMotivoConclusao> listMotivoConclusao = tipoMotivoConclusaoRespository.findByDescricaoContaining(descricao);
		return listMotivoConclusao;
	}

	@Override
	public TipoMotivoConclusao listaPorId(Long idMotivo) throws Exception {
		TipoMotivoConclusao tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(idMotivo);
		return tipoMotivoConclusao;
	}

	@Override
	public TipoMotivoConclusaoDTO criar(TipoMotivoConclusaoDTO tipoMotivoConclusaoTO) throws Exception {
		TipoMotivoConclusao tipoMotivoConclusao;
		TipoMotivoConclusaoDTO tipoMotivoConclusaoCriado;

		tipoMotivoConclusao = TipoMotivoConclusaoConverter.paraEntidade(tipoMotivoConclusaoTO);

		tipoMotivoConclusaoCriado = TipoMotivoConclusaoConverter.paraDTO(tipoMotivoConclusaoRespository.save(tipoMotivoConclusao));

		return tipoMotivoConclusaoCriado;
	}

	@Override
	public TipoMotivoConclusaoDTO alterar(TipoMotivoConclusaoDTO tipoMotivoConclusaoDTO) {
		TipoMotivoConclusao tipoMotivoConclusao;
		TipoMotivoConclusaoDTO tipoMotivoConclusaoAlterado;

		tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(tipoMotivoConclusaoDTO.getIdTipoMotivoConclusao());

		if (tipoMotivoConclusao == null) {
			throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[] { "tipos de status" }));
		}

		tipoMotivoConclusao.setDescricao(tipoMotivoConclusaoDTO.getDescricao());

		tipoMotivoConclusaoAlterado = TipoMotivoConclusaoConverter.paraDTO(tipoMotivoConclusaoRespository.save(tipoMotivoConclusao));

		return tipoMotivoConclusaoAlterado;
	}

}
