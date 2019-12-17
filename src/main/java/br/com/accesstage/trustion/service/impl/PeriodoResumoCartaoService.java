package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.PeriodoResumoCartaoConverter;
import br.com.accesstage.trustion.dto.PeriodoResumoCartaoDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.model.PeriodoResumoCartao;
import br.com.accesstage.trustion.repository.interfaces.IPeriodoResumoCartaoRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoCartaoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;

@Service
public class PeriodoResumoCartaoService implements IPeriodoResumoCartaoService {

    private IPeriodoResumoCartaoRepository periodoResumoCartaoRepository;

    private IAuditoriaService auditoriaService;

    private static final Long PADRAO_VENDA = 30L;

    private static final Long PADRAO_RECEBIMENTO = 7L;

    private static final Long PADRAO_RECEBIMENTO_FUTURO = 7L;

    @Autowired
    public PeriodoResumoCartaoService(IPeriodoResumoCartaoRepository periodoResumoCartaoRepository, IAuditoriaService auditoriaService) {
        this.periodoResumoCartaoRepository = periodoResumoCartaoRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public PeriodoResumoCartaoDTO criar(UsuarioDTO usuario) throws Exception {

        if(usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(periodoResumoCartaoRepository.existsByIdUsuario(usuario.getIdUsuario()))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"resumo"}));

        PeriodoResumoCartao entidade = new PeriodoResumoCartao();

        entidade.setIdUsuario(usuario.getIdUsuario());
        entidade.setPeriodoVenda(PADRAO_VENDA);
        entidade.setPeriodoRecebimento(PADRAO_RECEBIMENTO);
        entidade.setPeriodoRecebimentoFuturo(PADRAO_RECEBIMENTO_FUTURO);
        entidade.setDataCriacao(Calendar.getInstance().getTime());
        entidade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());

        periodoResumoCartaoRepository.save(entidade);

        return PeriodoResumoCartaoConverter.paraDTO(entidade);
    }

    @Override
    public PeriodoResumoCartaoDTO alterar(PeriodoResumoCartaoDTO dto) throws Exception {

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
        		UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
        		UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(!periodoResumoCartaoRepository.existsByIdUsuario(UsuarioAutenticado.getIdUsuario()))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"resumo"}));

        PeriodoResumoCartao entidade = PeriodoResumoCartaoConverter.paraEntidade(dto);

        PeriodoResumoCartao entidadePesquisado = periodoResumoCartaoRepository.findOneByIdUsuario(UsuarioAutenticado.getIdUsuario());


        if(Objects.isNull(entidade.getPeriodoVenda()))
            entidade.setPeriodoVenda(PADRAO_VENDA);

        if(Objects.isNull(entidade.getPeriodoRecebimento()))
            entidade.setPeriodoRecebimento(PADRAO_RECEBIMENTO);

        if (Objects.isNull(entidade.getPeriodoRecebimentoFuturo()))
            entidade.setPeriodoRecebimentoFuturo(PADRAO_RECEBIMENTO_FUTURO);

        entidade.setIdUsuario(entidadePesquisado.getIdUsuario());
        entidade.setIdPeriodoResumo(entidadePesquisado.getIdPeriodoResumo());
        entidade.setIdUsuarioCriacao(entidadePesquisado.getIdUsuarioCriacao());
        entidade.setDataCriacao(entidadePesquisado.getDataCriacao());
        entidade.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
        entidade.setDataAlteracao(Calendar.getInstance().getTime());

        periodoResumoCartaoRepository.save(entidade);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[] {UTF8.Periodo + " de resumo de ", UTF8.cartoes}), null);

        return PeriodoResumoCartaoConverter.paraDTO(entidade);
    }

    @Override
    public PeriodoResumoCartaoDTO pesquisar(Long idUsuario) {

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
        		UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        
        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
        		UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(!periodoResumoCartaoRepository.existsByIdUsuario(idUsuario))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"resumo"}));

        PeriodoResumoCartao entidadePesquisado = periodoResumoCartaoRepository.findOneByIdUsuario(idUsuario);

        return PeriodoResumoCartaoConverter.paraDTO(entidadePesquisado);
    }
}
