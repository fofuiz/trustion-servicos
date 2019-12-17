package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.PeriodoResumoNumerarioConverter;
import br.com.accesstage.trustion.dto.PeriodoResumoNumerarioDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.model.PeriodoResumoNumerario;
import br.com.accesstage.trustion.repository.interfaces.IPeriodoResumoNumerarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoNumerarioService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;

@Service
public class PeriodoResumoNumerarioService implements IPeriodoResumoNumerarioService {

    private IPeriodoResumoNumerarioRepository periodoResumoNumerarioRepository;

    private IAuditoriaService auditoriaService;

    private static final Long PADRAO_COLETA = 30L;

    private static final Long PADRAO_CONFERENCIA = 30L;

    private static final Long PADRAO_CREDITO = 30L;

    @Autowired
    public PeriodoResumoNumerarioService(IPeriodoResumoNumerarioRepository periodoResumoNumerarioRepository, IAuditoriaService auditoriaService) {
        this.periodoResumoNumerarioRepository = periodoResumoNumerarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public PeriodoResumoNumerarioDTO criar(UsuarioDTO usuario) throws Exception {

        if(usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get() &&
                usuario.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(periodoResumoNumerarioRepository.existsByIdUsuario(usuario.getIdUsuario()))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"resumo"}));

        PeriodoResumoNumerario entidade = new PeriodoResumoNumerario();

        entidade.setIdUsuario(usuario.getIdUsuario());
        entidade.setPeriodoColeta(PADRAO_COLETA);
        entidade.setPeriodoConferencia(PADRAO_CONFERENCIA);
        entidade.setPeriodoCredito(PADRAO_CREDITO);
        entidade.setDataCriacao(Calendar.getInstance().getTime());
        entidade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());

        PeriodoResumoNumerario entidadeSalvo = periodoResumoNumerarioRepository.save(entidade);

        return PeriodoResumoNumerarioConverter.paraDTO(entidadeSalvo);
    }

    @Override
    public PeriodoResumoNumerarioDTO alterar(PeriodoResumoNumerarioDTO dto) throws Exception {

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(!periodoResumoNumerarioRepository.existsByIdUsuario(UsuarioAutenticado.getIdUsuario()))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"resumo"}));

        PeriodoResumoNumerario entidade = PeriodoResumoNumerarioConverter.paraEntidade(dto);

        PeriodoResumoNumerario entidadePesquisado = periodoResumoNumerarioRepository.findOneByIdUsuario(UsuarioAutenticado.getIdUsuario());

        if(Objects.isNull(entidade.getPeriodoColeta()))
            entidade.setPeriodoColeta(PADRAO_COLETA);

        if(Objects.isNull(entidade.getPeriodoConferencia()))
            entidade.setPeriodoConferencia(PADRAO_CONFERENCIA);

        if(Objects.isNull(entidade.getPeriodoCredito()))
            entidade.setPeriodoCredito(PADRAO_CREDITO);

        entidade.setIdPeriodoResumo(entidadePesquisado.getIdPeriodoResumo());
        entidade.setIdUsuario(entidadePesquisado.getIdUsuario());
        entidade.setIdUsuarioCriacao(entidadePesquisado.getIdUsuarioCriacao());
        entidade.setDataCriacao(entidadePesquisado.getDataCriacao());
        entidade.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
        entidade.setDataAlteracao(Calendar.getInstance().getTime());

        PeriodoResumoNumerario entidadeAlterado = periodoResumoNumerarioRepository.save(entidade);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[] {UTF8.Periodo + " de resumo de ", UTF8.numerarios }), null);

        return PeriodoResumoNumerarioConverter.paraDTO(entidadeAlterado);
    }

    @Override
    public PeriodoResumoNumerarioDTO pesquisar(Long idUsuario) {

        if(UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get() &&
                UsuarioAutenticado.getIdPerfil() != CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get())
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        if(!periodoResumoNumerarioRepository.existsByIdUsuario(idUsuario))
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"resumo"}));

        PeriodoResumoNumerario entidadePesquisado = periodoResumoNumerarioRepository.findOneByIdUsuario(idUsuario);

        return PeriodoResumoNumerarioConverter.paraDTO(entidadePesquisado);
    }
}
