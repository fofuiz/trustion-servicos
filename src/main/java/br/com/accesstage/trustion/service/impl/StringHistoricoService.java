package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.StringHistoricoConverter;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.dto.StringHistoricoDTO;
import br.com.accesstage.trustion.enums.CodigoFlagStringHistorico;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.model.StringHistorico;
import br.com.accesstage.trustion.repository.criteria.StringHistoricoSpecification;
import br.com.accesstage.trustion.repository.interfaces.IStringHistoricoRepository;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IListaBancoService;
import br.com.accesstage.trustion.service.interfaces.IStringHistoricoService;
import br.com.accesstage.trustion.util.Mensagem;
import java.util.Calendar;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.ADMINISTRADOR;
import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.BPO;
import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.MASTER_TRANSPORTADORA;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdPerfil;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdUsuario;

@Service
public class StringHistoricoService implements IStringHistoricoService {

    @Autowired
    private IStringHistoricoRepository stringHistoricoRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IListaBancoService listaBancoService;

    @Log
    private static Logger LOGGER;

    private final Converter<StringHistorico, StringHistoricoDTO> converter = source -> {
        StringHistoricoDTO dto = StringHistoricoConverter.paraDTO(source);
        try {
            dto.setBanco(listaBancoService.pesquisar(dto.getIdListaBanco()).getDescricao());
        } catch (Exception e) {
            LOGGER.error("Exceção " + e.getMessage(), e);
        }
        return dto;
    };

    @Override
    public StringHistoricoDTO criar(StringHistoricoDTO dto) throws Exception {

        if (getIdPerfil() != ADMINISTRADOR.get()
                && getIdPerfil() != BPO.get()
                && getIdPerfil() != MASTER_TRANSPORTADORA.get()) {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        if (stringHistoricoRepository.existsByIdListaBancoAndTextoIgnoringCase(dto.getIdListaBanco(), dto.getTexto())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe.banco", new Object[]{"String", "string"}));
        }

        StringHistorico entidade = StringHistoricoConverter.paraEntidade(dto);
        entidade.setIdUsuarioCriacao(getIdUsuario());
        entidade.setDataCriacao(Calendar.getInstance().getTime());
        entidade.setFlag(CodigoFlagStringHistorico.REPROCESSAR.get());

        entidade = stringHistoricoRepository.save(entidade);

        StringHistoricoDTO dtoCriado = StringHistoricoConverter.paraDTO(entidade);

        if (dtoCriado == null) {
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"String"}));
        }

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new String[]{"String", dtoCriado.getTexto()}), null);

        return dtoCriado;
    }

    @Override
    public StringHistoricoDTO alterar(StringHistoricoDTO dto) throws Exception {

        if (getIdPerfil() != ADMINISTRADOR.get()
                && getIdPerfil() != BPO.get()
                && getIdPerfil() != MASTER_TRANSPORTADORA.get()) {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        if (!stringHistoricoRepository.exists(dto.getIdStringHistorico())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"String"}));
        }

        StringHistorico entidadePesquisa = stringHistoricoRepository.findOne(dto.getIdStringHistorico());

        if (entidadePesquisa.getIdListaBanco() != dto.getIdListaBanco()) {
            if (stringHistoricoRepository.existsByIdListaBancoAndTextoIgnoringCase(dto.getIdListaBanco(), dto.getTexto())) {
                throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe.banco", new Object[]{"String", "string"}));
            }
        }

        StringHistorico entidade = StringHistoricoConverter.paraEntidade(dto);
        entidade.setDataCriacao(entidadePesquisa.getDataCriacao());
        entidade.setIdUsuarioCriacao(entidadePesquisa.getIdUsuarioCriacao());
        entidade.setIdUsuarioAlteracao(getIdUsuario());
        entidade.setDataAlteracao(Calendar.getInstance().getTime());
        entidade.setFlag(entidadePesquisa.getFlag());

        if (entidade.getIdListaBanco() != entidadePesquisa.getIdListaBanco()) {
            entidade.setFlag(CodigoFlagStringHistorico.REPROCESSAR.get());
        }

        entidade = stringHistoricoRepository.save(entidade);

        StringHistoricoDTO dtoAlterado = StringHistoricoConverter.paraDTO(entidade);

        if (dtoAlterado == null) {
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.alterar", new Object[]{"String"}));
        }

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new String[]{"String", dtoAlterado.getTexto()}), null);

        return dtoAlterado;
    }

    @Override
    public StringHistoricoDTO pesquisar(Long idStringHistorico) throws Exception {

        if (getIdPerfil() != ADMINISTRADOR.get()
                && getIdPerfil() != BPO.get()
                && getIdPerfil() != MASTER_TRANSPORTADORA.get()) {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        if (!stringHistoricoRepository.exists(idStringHistorico)) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"String"}));
        }

        StringHistoricoDTO dto = StringHistoricoConverter.paraDTO(stringHistoricoRepository.findOne(idStringHistorico));

        dto.setBanco(listaBancoService.pesquisar(dto.getIdListaBanco()).getDescricao());

        return dto;
    }

    @Override
    public Page<StringHistoricoDTO> pesquisar(StringHistoricoDTO dto, Pageable pageable) throws Exception {

        if (getIdPerfil() != ADMINISTRADOR.get()
                && getIdPerfil() != BPO.get()
                && getIdPerfil() != MASTER_TRANSPORTADORA.get()) {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        Specification<StringHistorico> specs = StringHistoricoSpecification.byCriterio(dto);

        Page<StringHistorico> entidades = stringHistoricoRepository.findAll(specs, pageable);

        Page<StringHistoricoDTO> dtos = entidades.map(converter);

        return dtos;
    }

    @Override
    public boolean excluir(Long idStringHistorico) throws Exception {
        return false;
    }
}
