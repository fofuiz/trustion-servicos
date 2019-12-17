package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.util.EmailTamplate;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

import br.com.accesstage.trustion.enums.StatusAtivoInativo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.NotificacaoConverter;
import br.com.accesstage.trustion.dto.NotificacaoDTO;
import br.com.accesstage.trustion.enums.PerfilEnum;
import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Notificacao;
import br.com.accesstage.trustion.model.TipoNotificacao;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.NotificacaoSpecification;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.INotificacaoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoNotificacaoRespository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.INotificacaoService;
import org.slf4j.Logger;

@Service
public class NotificacaoService implements INotificacaoService {

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private INotificacaoRepository notificacaoRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IUsuarioRepository usarioRepository;

    @Autowired
    private ITipoNotificacaoRespository tipoNotificacaoRespository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private IEmpresaService empresaService;

    @Log
    private static Logger LOGGER;

    @Override
    public NotificacaoDTO criar(NotificacaoDTO notificacaoDTO) throws Exception {
        NotificacaoDTO notificacaoCriada = new NotificacaoDTO();

        Notificacao notificacao = NotificacaoConverter.paraEntidade(notificacaoDTO);
        notificacao.setDataCriacao(Calendar.getInstance().getTime());

        Usuario usuario = usarioRepository.findOne(notificacao.getUsuario().getIdUsuario());

        notificacaoCriada = NotificacaoConverter.paraDTO(notificacaoRepository.save(notificacao));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{UTF8.Notificacao, "de Id: " + notificacaoCriada.getIdNotificacao() + " e tipo: " + tipoNotificacaoRespository.findOne(notificacaoCriada.getIdTipoNotificacao()).getDescNotificacao()}), null);

        return notificacaoCriada;
    }

    @Override
    public NotificacaoDTO alterar(NotificacaoDTO notificacaoDTO) throws Exception {

        NotificacaoDTO notificacaoParaAlterado = new NotificacaoDTO();

        Notificacao notificacao = notificacaoRepository.findOne(notificacaoDTO.getIdNotificacao());
        notificacaoDTO.setDataCriacao(notificacao.getDataCriacao());

        BeanUtils.copyProperties(notificacaoDTO, notificacao);
        notificacao.setIdUsuarioAlteracao(notificacaoDTO.getIdUsuarioAlteracao());
        notificacao.setDataAlteracao(Calendar.getInstance().getTime());

        notificacaoParaAlterado = NotificacaoConverter.paraDTO(notificacaoRepository.save(notificacao));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Notificacao, "de Id: " + notificacaoParaAlterado.getIdNotificacao() + " e novo tipo: " + tipoNotificacaoRespository.findOne(notificacaoParaAlterado.getIdTipoNotificacao()).getDescNotificacao()}), null);

        return notificacaoParaAlterado;
    }

    @Override
    public NotificacaoDTO pesquisar(Long idNotificacaoDTO) throws Exception {
        Notificacao notificacao = notificacaoRepository.findOne(idNotificacaoDTO);

        NotificacaoDTO notificacaoDTO = NotificacaoConverter.paraDTO(notificacao);

        return notificacaoDTO;
    }

    @Override
    public void excluir(Long idUsuario) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        notificacaoRepository.deleteByUsuario(usuario);
    }

    @Override
    public Page<NotificacaoDTO> listarCriterios(NotificacaoDTO notificacaoDTO, Pageable pageable) throws Exception {
        Page<NotificacaoDTO> lstNotificacaoDTO;
        List<Long> idsGrupoEconomico = new ArrayList<>();

        if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_TRANSPORTADORA.get())) {
            List<Empresa> empresas = empresaService.empresasPorUsuarioLogado();
            
            if (empresas != null && !empresas.isEmpty()) {
                empresas.forEach(empresaAssoc -> {
                    idsGrupoEconomico.add(empresaAssoc.getIdGrupoEconomico());
                });
            }

        }

        Specification<Notificacao> specs = NotificacaoSpecification.byCriterio(notificacaoDTO, idsGrupoEconomico);

        lstNotificacaoDTO = notificacaoRepository.findAll(specs, pageable).map(new Converter<Notificacao, NotificacaoDTO>() {

            @Override
            public NotificacaoDTO convert(Notificacao notificacao) {

                NotificacaoDTO notificacaoAux = NotificacaoConverter.paraDTO(notificacao);

                if (notificacao.getIdGrupoEconomico() != null) {
                    GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(notificacao.getIdGrupoEconomico());
                    notificacaoAux.setNomeGrupoEconomico(grupoEconomico.getNome());
                }

                if (notificacao.getIdEmpresa() != null) {
                    Empresa empresa = empresaRepository.findOne(notificacao.getIdEmpresa());
                    notificacaoAux.setRazaoSocial(empresa.getRazaoSocial());
                }

                if (notificacao.getIdTipoNotificacao() != null) {
                    TipoNotificacao tipoNotificacao = tipoNotificacaoRespository.findOne(notificacao.getIdTipoNotificacao());
                    notificacaoAux.setDescTipoNotificacao(tipoNotificacao.getDescNotificacao());
                }

                return notificacaoAux;
            }
        });

        return lstNotificacaoDTO;
    }

    @Override
    public List<NotificacaoDTO> listarCriterios(NotificacaoDTO notificacaoDTO) throws Exception {
        List<NotificacaoDTO> lstNotificacaoDTO = new ArrayList<>();
        List<Long> idsGrupoEconomico = new ArrayList<>();

        if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_TRANSPORTADORA.get())) {
            List<Empresa> empresas = empresaService.empresasPorUsuarioLogado();
            if (empresas != null && !empresas.isEmpty()) {
                empresas.forEach(empresaAssoc -> {
                    idsGrupoEconomico.add(empresaAssoc.getIdGrupoEconomico());
                });
            }
        }

        Specification<Notificacao> specs = NotificacaoSpecification.byCriterio(notificacaoDTO, idsGrupoEconomico);

        for (Notificacao notificacao : notificacaoRepository.findAll(specs)) {
            NotificacaoDTO notificacaoAux = NotificacaoConverter.paraDTO(notificacao);

            if (notificacao.getIdGrupoEconomico() != null) {
                GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(notificacao.getIdGrupoEconomico());
                notificacaoAux.setNomeGrupoEconomico(grupoEconomico.getNome());
            }

            if (notificacao.getIdEmpresa() != null) {
                Empresa empresa = empresaRepository.findOne(notificacao.getIdEmpresa());
                notificacaoAux.setRazaoSocial(empresa.getRazaoSocial());
            }

            if (notificacao.getIdTipoNotificacao() != null) {
                TipoNotificacao tipoNotificacao = tipoNotificacaoRespository.findOne(notificacao.getIdTipoNotificacao());
                notificacaoAux.setDescTipoNotificacao(tipoNotificacao.getDescNotificacao());
            }

            lstNotificacaoDTO.add(notificacaoAux);
        }

        return lstNotificacaoDTO;
    }

    @Override
    public void enviarNotificacaoAnaliseDivergencia(Long idOcorrencia, Long idEmpresa) {

        long analiseDeDivergencias = 3;
        List<Notificacao> lstNotificacao = notificacaoRepository.findByIdTipoNotificacaoAndIdEmpresaAndStatus(analiseDeDivergencias, idEmpresa, StatusAtivoInativo.ATIVO.getTexto());

        for (Notificacao notificacao : lstNotificacao) {
            Usuario usuarioLogado = usuarioRepository.findByIdUsuario(notificacao.getUsuario().getIdUsuario());

            if (usuarioLogado.getStatus() != null && usuarioLogado.getStatus().equalsIgnoreCase(StatusUsuarioEnum.ATIVO.get())) {
                try {
                    emailService.enviarEmail(usuarioLogado.getEmail(),
                            Mensagem.get("email.titulo.brinks.portal.notificacao"),
                            EmailTamplate.envioNotificacao(idOcorrencia));

                } catch (MessagingException e) {
                    LOGGER.error("enviarEmail: ", e);
                }
            }
        }

    }

    @Override
    public List<NotificacaoDTO> pesquisarByUsuario(Long idUsuario) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        List<Notificacao> notificacaoList = notificacaoRepository.findDistinctByUsuarioOrderByIdNotificacao(usuario);
        List<NotificacaoDTO> notificacaoDTOList = new ArrayList<>();
        notificacaoList.forEach(n -> {
            NotificacaoDTO notificacaoDTO = NotificacaoConverter.paraDTO(n);
            notificacaoDTOList.add(notificacaoDTO);
        });
        return notificacaoDTOList;
    }

    @Override
    public List<NotificacaoDTO> pesquisarByUsuarioAndIdGrupoEmpresa(Long idUsuario, Long idGrupoEmpresa) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        List<Notificacao> notificacaoList = notificacaoRepository.findDistinctByUsuarioAndIdGrupoEconomicoOrderByIdNotificacao(usuario, idGrupoEmpresa);
        List<NotificacaoDTO> notificacaoDTOList = new ArrayList<>();
        notificacaoList.forEach(n -> {
            NotificacaoDTO notificacaoDTO = NotificacaoConverter.paraDTO(n);
            notificacaoDTOList.add(notificacaoDTO);
        });
        return notificacaoDTOList;
    }

    @Override
    public List<NotificacaoDTO> pesquisarByUsuarioAndIdTransportadora(Long idUsuario, Long idTransportadora) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        List<Notificacao> notificacaoList = notificacaoRepository.findDistinctByUsuarioAndIdTransportadoraOrderByIdNotificacao(usuario, idTransportadora);
        List<NotificacaoDTO> notificacaoDTOList = new ArrayList<>();
        notificacaoList.forEach(n -> {
            NotificacaoDTO notificacaoDTO = NotificacaoConverter.paraDTO(n);
            notificacaoDTOList.add(notificacaoDTO);
        });
        return notificacaoDTOList;
    }

}
