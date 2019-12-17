package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.TipoQuestionamentoConverter;
import br.com.accesstage.trustion.dto.TipoQuestionamentoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.TipoQuestionamento;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.ITipoQuestionamentoRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.ITipoQuestionamentoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoQuestionamentoService implements ITipoQuestionamentoService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ITipoQuestionamentoRepository tipoQuestionamentoRepository;

    @Override
    public List<TipoQuestionamentoDTO> listarTodos() throws Exception {
        List<TipoQuestionamentoDTO> dtos = null;
        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
        if (usuario != null) {
            dtos = new ArrayList<>();
            List<TipoQuestionamento> entidades = tipoQuestionamentoRepository.findAll();
            for (TipoQuestionamento entidade : entidades) {
                dtos.add(TipoQuestionamentoConverter.paraDTO(entidade));
            }
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }
        return dtos;
    }
    
    @Override
    public TipoQuestionamentoDTO pesquisar(Long id) throws Exception {
        TipoQuestionamentoDTO tipoQuestionamentoDTO = null;
        TipoQuestionamento tipoQuestionamento = tipoQuestionamentoRepository.findOne(id);
        
        if(null != tipoQuestionamento) {
            tipoQuestionamentoDTO = TipoQuestionamentoConverter.paraDTO(tipoQuestionamento);
        }
        
        return tipoQuestionamentoDTO;
    }

    @Override
    public List<TipoQuestionamentoDTO> listarPorDescricao(String descricao) {
        List<TipoQuestionamentoDTO> tiposQuestionamentosDtos = new ArrayList<>();
        
        List<TipoQuestionamento> entidades = tipoQuestionamentoRepository.findByDescricaoContaining(descricao);
        
        entidades.forEach((entidade) -> {
            tiposQuestionamentosDtos.add(TipoQuestionamentoConverter.paraDTO(entidade));
        });
        
        return tiposQuestionamentosDtos;
    }

    @Override
    public TipoQuestionamentoDTO criar(TipoQuestionamentoDTO tipoQuestionamentoDTO) throws Exception {
        TipoQuestionamento tipoQuestionamento = TipoQuestionamentoConverter.paraEntidade(tipoQuestionamentoDTO);
        TipoQuestionamentoDTO tipoQuestionamentoDtoCriado;
        
        tipoQuestionamento.setDescricao(StringUtils.trim(tipoQuestionamento.getDescricao()));
        tipoQuestionamento.setDataCriacao(LocalDateTime.now());
        tipoQuestionamento.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        
        if(!tipoQuestionamentoValido(tipoQuestionamento)) {
            throw new BadRequestResponseException("Campos inválidos");
        }
        
        tipoQuestionamentoDtoCriado = TipoQuestionamentoConverter.paraDTO(tipoQuestionamentoRepository.save(tipoQuestionamento));
        return tipoQuestionamentoDtoCriado;        
    }

    @Override
    public TipoQuestionamentoDTO alterar(TipoQuestionamentoDTO tipoQuestionamentoDTO) throws Exception {
        TipoQuestionamento tipoQuestionamento = tipoQuestionamentoRepository.findOne(tipoQuestionamentoDTO.getIdTipoQuestionamento());

        if(tipoQuestionamento == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{tipoQuestionamento}));
        }
        
        tipoQuestionamento.setDescricao(tipoQuestionamentoDTO.getDescricao().trim());
        tipoQuestionamento.setUsuarioAlteracao(usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario()));
        
        if(!tipoQuestionamentoValido(tipoQuestionamento)) {
            throw new BadRequestResponseException("Campos inválidos");
        }
        
        tipoQuestionamentoRepository.save(tipoQuestionamento);
        return TipoQuestionamentoConverter.paraDTO(tipoQuestionamento);
    }
    
    private boolean tipoQuestionamentoValido(TipoQuestionamento tipoQuestionamento) {
        boolean ehValido = true;
        
        if(StringUtils.length(tipoQuestionamento.getDescricao()) < 3 || StringUtils.length(tipoQuestionamento.getDescricao()) > 150) {
            ehValido = false;
        }
        
        return ehValido;
    }
}
