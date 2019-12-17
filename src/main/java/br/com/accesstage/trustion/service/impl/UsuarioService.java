package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.converter.EmpresaConverter;
import br.com.accesstage.trustion.converter.GrupoEconomicoConverter;
import br.com.accesstage.trustion.converter.UsuarioConverter;
import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.dto.ascartoes.UserEmpresaCADTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.PerfilEnum;
import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.UsuarioNaoExisteException;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Perfil;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.UsuarioSpecification;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IPerfilRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.seguranca.service.UsuarioDetailsService;
import br.com.accesstage.trustion.service.impl.ascartoes.EmpresaCaService;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.INotificacaoService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoCartaoService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoNumerarioService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.EmailTamplate;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.Utils;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IPerfilRepository perfilRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioDetailsService usuarioSegurancaService;

    @Autowired
    private IPeriodoResumoNumerarioService periodoResumoNumerarioService;

    @Autowired
    private IPeriodoResumoCartaoService periodoResumoCartaoService;

    @Autowired
    private INotificacaoService notificacaoService;
    
    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private EmpresaCaService empresaCaService;

    @Log
    private static Logger LOGGER;

    @Override
    public UsuarioDTO criar(UsuarioDTO usuarioDTO) throws Exception {
        String senhaAleatoria;

        Usuario usuario;
        UsuarioDTO usuarioCriado;

        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"e-mail"}));
        }

        if (usuarioRepository.existsByLogin(usuarioDTO.getLogin())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{UTF8.usuario}));
        }

        usuario = UsuarioConverter.paraEntidade(usuarioDTO);
        //retirando os espaços
        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim());
        usuario.setLogin(usuario.getLogin().trim());

        senhaAleatoria = Utils.gerarSenhaAleatoria();
        usuario.setSenha(usuarioSegurancaService.passwordEncoder().encode(senhaAleatoria));
        usuario.setStatus(StatusUsuarioEnum.SUSPENSO.get());
        usuario.setPrimeiroAcesso(true);
        usuario.setDataCriacao(Calendar.getInstance().getTime());
        usuario.setLogin(usuario.getLogin().trim());
        usuario.setNroTelefone(usuario.getNroTelefone());

        usuario = paraEntidadeEmpresaTransportadora(usuario, usuarioDTO);

        usuarioCriado = UsuarioConverter.paraDTO(usuarioRepository.save(usuario));

        //criando o período para cada usuário
        try {
            periodoResumoNumerarioService.criar(usuarioCriado);
        } catch (Exception e) {
            LOGGER.warn("Periodo de Resumo Numerários: " + e.getMessage(), e);
        }

        try {
            periodoResumoCartaoService.criar(usuarioCriado);
        } catch (Exception e) {
            LOGGER.warn("Periodo de Resumo Cartões: " + e.getMessage(), e);
        }

        salvarNotificacao(usuarioDTO, usuarioCriado, false);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{UTF8.Usuario, usuarioCriado.getNome()}), null);

        try {
            emailService.enviarEmail(usuario.getEmail(),
                    Mensagem.get("email.titulo.brinks.portal.primeiro.acesso"),
                    EmailTamplate.redefinirSenha(usuario.getLogin(), senhaAleatoria));

        } catch (Exception e) {
            LOGGER.error("enviarEmail", e);
        }

        return usuarioCriado;
    }

    @Override
    public UsuarioDTO alterar(UsuarioDTO usuarioDTO) throws Exception {

        Usuario usuario;
        UsuarioDTO usuarioAlterado;

        usuarioDTO.setNome(usuarioDTO.getNome().trim());
        usuarioDTO.setEmail(usuarioDTO.getEmail().trim());

        usuario = usuarioRepository.findOne(usuarioDTO.getIdUsuario());

        if (usuario == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }

        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"e-mail"}));
        }

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setStatus(usuarioDTO.getStatus());
        usuario.setIdUsuarioAlteracao(usuarioDTO.getIdUsuarioAlteracao());
        usuario.setDataAlteracao(Calendar.getInstance().getTime());
        usuario.setNroTelefone(usuarioDTO.getNroTelefone());

        usuario = paraEntidadeEmpresaTransportadora(usuario, usuarioDTO);

        usuarioAlterado = UsuarioConverter.paraDTO(usuarioRepository.save(usuario));

        salvarNotificacao(usuarioDTO, usuarioAlterado, true);

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{UTF8.Usuario, usuarioAlterado.getNome()}), null);

        return usuarioAlterado;
    }

    @Override
    public boolean excluir(Long idUsuario) throws Exception {

        if (usuarioRepository.exists(idUsuario)) {
            notificacaoService.excluir(idUsuario);
            usuarioRepository.delete(idUsuario);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }

        return true;
    }

    @Override
    public List<UsuarioDTO> listarTodos() throws Exception {

        List<UsuarioDTO> listaUsuario = new ArrayList<>();

        for (Usuario usuario : usuarioRepository.findAll()) {

            UsuarioDTO usuarioDTO = UsuarioConverter.paraDTO(usuario);

            if (usuario.getIdPerfil() != null) {
                Perfil perfil = perfilRepository.findOne(usuario.getIdPerfil());
                usuarioDTO.setDescricaoPerfil(perfil.getDescricao());
            }

            List<Empresa> empresas = empresaService.empresasPorUsuario(usuario);
            if(empresas != null && !empresas.isEmpty()) {
                GrupoEconomico grupoEconomico = grupoEconomicoRepository.findOne(empresas.get(0).getIdGrupoEconomico());
                usuarioDTO.setNomeGrupoEconomico(grupoEconomico.getNome());
            }

            listaUsuario.add(usuarioDTO);
        }

        return listaUsuario;
    }

    @Override
    public UsuarioDTO pesquisar(Long idUsuario) throws Exception {

        Usuario usuario = usuarioRepository.findOne(idUsuario);
        UsuarioDTO usuarioDTO = UsuarioConverter.paraDTO(usuario);

        if (usuario.getIdPerfil() != null) {
            Perfil perfil = perfilRepository.findOne(usuario.getIdPerfil());
            usuarioDTO.setDescricaoPerfil(perfil.getDescricao());
        }

        //monta estrutura por grupo de empresa e transportadora (para edicao no cad usuario)
        List<GrupoEmpresaTransportadoraUsuarioDTO> grupoEmpTranspUsuarios = new ArrayList<>();

        if (usuarioDTO.getEmpresaList() != null && !usuarioDTO.getEmpresaList().isEmpty()) {
            Map<Long, GrupoEmpresaTransportadoraUsuarioDTO> empresaTransportadoraPorGrupo = new HashMap<>();

            //empresas agrupadas
            usuarioDTO.getEmpresaList().forEach(empresa -> {

                //usuarioDTO não tem o grupo economico da empresa
                Empresa empresaBD = empresaRepository.findOne(empresa.getIdEmpresa());

                if (!empresaTransportadoraPorGrupo.containsKey(empresaBD.getIdGrupoEconomico())) {
                    GrupoEmpresaTransportadoraUsuarioDTO grupEmpTransDTO = new GrupoEmpresaTransportadoraUsuarioDTO();
                    grupEmpTransDTO.setIdGrupoEmpresa(empresaBD.getIdGrupoEconomico());
                    grupEmpTransDTO.setEmpresas(new ArrayList<>());
                    grupEmpTransDTO.getEmpresas().add(empresa.getIdEmpresa());
                    grupEmpTransDTO.setNotificacoes(new ArrayList<>());
                    empresaTransportadoraPorGrupo.put(empresaBD.getIdGrupoEconomico(), grupEmpTransDTO);
                } else {
                    GrupoEmpresaTransportadoraUsuarioDTO grupEmpTransDTO = empresaTransportadoraPorGrupo.get(empresaBD.getIdGrupoEconomico());
                    grupEmpTransDTO.getEmpresas().add(empresa.getIdEmpresa());
                }
            });

            if (!empresaTransportadoraPorGrupo.isEmpty()) {
                grupoEmpTranspUsuarios.addAll(empresaTransportadoraPorGrupo.values());
            }
        }

        if (usuarioDTO.getTransportadoraList() != null && !usuarioDTO.getTransportadoraList().isEmpty()) {
            //transportadoras
            usuarioDTO.getTransportadoraList().forEach(transportadora -> {
                GrupoEmpresaTransportadoraUsuarioDTO grupEmpTransDTO = new GrupoEmpresaTransportadoraUsuarioDTO();
                grupEmpTransDTO.setTransportadora(transportadora.getIdTransportadora());
                grupEmpTransDTO.setNotificacoes(new ArrayList<>());
                grupoEmpTranspUsuarios.add(grupEmpTransDTO);
            });
        }

        //popula informações de notificação na lista existente
        List<NotificacaoDTO> notificacaoDTOList = notificacaoService.pesquisarByUsuario(usuario.getIdUsuario());
        if (notificacaoDTOList.size() > 0) {

            for (NotificacaoDTO notificacaoBD : notificacaoDTOList) {
                //empresa
                if (notificacaoBD.getIdEmpresa() != null) {
                    grupoEmpTranspUsuarios.forEach(grupEmpTransUsuario -> {

                        if (notificacaoBD.getIdGrupoEconomico().equals(grupEmpTransUsuario.getIdGrupoEmpresa())
                                && grupEmpTransUsuario.getEmpresas().contains(notificacaoBD.getIdEmpresa())) {

                            if (!grupEmpTransUsuario.getNotificacoes().contains(notificacaoBD.getIdTipoNotificacao())) {
                                grupEmpTransUsuario.getNotificacoes().add(notificacaoBD.getIdTipoNotificacao());
                            }
                        }
                    });
                }

                //transportadora
                if (notificacaoBD.getIdTransportadora() != null) {
                    //tera apenas 1 elemento
                    grupoEmpTranspUsuarios.forEach(grupEmpTransUsuario -> {
                        if (notificacaoBD.getIdTransportadora().equals(grupEmpTransUsuario.getTransportadora())) {
                            if (!grupEmpTransUsuario.getNotificacoes().contains(notificacaoBD.getIdTipoNotificacao())) {
                                grupEmpTransUsuario.getNotificacoes().add(notificacaoBD.getIdTipoNotificacao());
                            }
                        }
                    });
                }
            }

        }
        usuarioDTO.setEmpresasTransportadoras(grupoEmpTranspUsuarios);

        return usuarioDTO;
    }

    @Override
    public Page<UsuarioDTO> listarCriterios(UsuarioDTO usuarioDTO, Pageable pageable) throws Exception {

        Page<UsuarioDTO> listaUsuario;
        List<Long> listaIdsPerfil = new ArrayList<>();
        List<Long> idsGrupos = new ArrayList<>();
        List<Long> idsTransportadora = new ArrayList<>();

        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

        PerfilEnum.getByDescription(UsuarioAutenticado.getPerfil()).ifPresent(perfilUsuarioLogado -> {
            if (null != perfilUsuarioLogado) {
                switch (perfilUsuarioLogado) {
                    case ADMINISTRADOR:
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.BPO.get());
                        break;
                    case MASTER_TRANSPORTADORA:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
                        if (usuario.getTransportadoraList() != null && ! usuario.getTransportadoraList().isEmpty()) {
                            idsTransportadora.add(usuario.getTransportadoraList().get(0).getIdTransportadora());
                        }
                        break;
                    case MASTER_CLIENTE:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
                        if(usuarioDTO.getIdGrupoEconomico() == null) {
                            Set<Long> idGruposUsuario = usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toSet());
                            if(!idGruposUsuario.isEmpty()) {
                                idsGrupos.addAll(idGruposUsuario);
                            }
                        }
                        break;
                    case MASTER_CLIENTE_VENDA_NUMERARIO:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
                        if(usuarioDTO.getIdGrupoEconomico() == null) {
                            Set<Long> idGruposUsuario = usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toSet());
                            if(!idGruposUsuario.isEmpty()) {
                                idsGrupos.addAll(idGruposUsuario);
                            }
                        }
                        break;
                    case MASTER_CLIENTE_CARTAO:
                    	listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
                        if(usuarioDTO.getIdGrupoEconomico() == null) {
                            Set<Long> idGruposUsuario = usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toSet());
                            if(!idGruposUsuario.isEmpty()) {
                                idsGrupos.addAll(idGruposUsuario);
                            }
                        }
                        break;    
                    case MASTER_CLIENTE_NUMERARIO:
                    	listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
                        if(usuarioDTO.getIdGrupoEconomico() == null) {
                            Set<Long> idGruposUsuario = usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toSet());
                            if(!idGruposUsuario.isEmpty()) {
                                idsGrupos.addAll(idGruposUsuario);
                            }
                        }
                        break;     
                    case BPO:
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
                        if(usuarioDTO.getIdGrupoEconomico() == null) {
                            Set<Long> idGruposBPO = usuario.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toSet());
                            if(!idGruposBPO.isEmpty()) {
                                idsGrupos.addAll(idGruposBPO);
                            }
                        }
                         if (usuario.getTransportadoraList() != null && ! usuario.getTransportadoraList().isEmpty()) {
                            idsTransportadora.add(usuario.getTransportadoraList().get(0).getIdTransportadora());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        
        if(usuarioDTO.getIdGrupoEconomico() != null) {
            idsGrupos.add(usuarioDTO.getIdGrupoEconomico());
        }

        Specification<Usuario> specs = UsuarioSpecification.byCriterio(
                paraEntidadeEmpresaTransportadora(UsuarioConverter.paraEntidade(usuarioDTO), usuarioDTO), listaIdsPerfil, idsGrupos, idsTransportadora
        );



        listaUsuario = usuarioRepository.findAll(specs, pageable).map((Usuario u) -> {

            UsuarioDTO usuarioDTOAux = UsuarioConverter.paraDTO(u);
            if (u.getIdPerfil() != null) {
                Perfil perfil = perfilRepository.findOne(u.getIdPerfil());
                usuarioDTOAux.setDescricaoPerfil(perfil.getDescricao());
            }

            List<EmpresaDTO> empresaDTOS = u.getEmpresaList().stream().map(EmpresaConverter::paraDTO).collect(Collectors.toList());
            
            usuarioDTOAux.setEmpresaList(empresaDTOS);

            List<Long> idGrupos = u.getEmpresaList().stream().map(Empresa::getIdGrupoEconomico).collect(Collectors.toList());

            List<GrupoEconomicoDTO> grupoEconomicoDTOList = new ArrayList<>();


            for(Long id : idGrupos) {
                GrupoEconomicoDTO grupoEconomicoDTO = GrupoEconomicoConverter.paraDTO(grupoEconomicoRepository.findOne(id));
                if(!grupoEconomicoDTOList.contains(grupoEconomicoDTO)){
                    grupoEconomicoDTOList.add(grupoEconomicoDTO);
                    usuarioDTOAux.setGrupoEconomicoList(grupoEconomicoDTOList);
                    usuarioDTOAux.setNomeGrupoEconomico(grupoEconomicoDTO.getNome());

                }

            }

            usuarioDTOAux = paraDTOEmpresaTransportadora(u, usuarioDTOAux);
            return usuarioDTOAux;
        });

        return listaUsuario;
    }

    @Override
    public List<UsuarioDTO> listarCriterios(UsuarioDTO usuarioDTO) throws Exception {

        List<UsuarioDTO> listaUsuario = new ArrayList<>();
        List<Long> listaIdsPerfil = new ArrayList<>();

        PerfilEnum.getByDescription(UsuarioAutenticado.getPerfil()).ifPresent(perfilUsuarioLogado -> {
            if (null != perfilUsuarioLogado) {
                switch (perfilUsuarioLogado) {
                    case ADMINISTRADOR:
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
                        listaIdsPerfil.add(CodigoPerfilEnum.BPO.get());
                        break;
                    case MASTER_TRANSPORTADORA:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
                        break;
                    case MASTER_CLIENTE:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
                        break;
                    case MASTER_CLIENTE_VENDA_NUMERARIO:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
                        break;
                    case MASTER_CLIENTE_CARTAO:
                        listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
                        break;
                    case MASTER_CLIENTE_NUMERARIO:
                    	listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
                        break;
                    default:
                        break;
                }
            }
        });

        Specification<Usuario> specs = UsuarioSpecification.byCriterio(
                paraEntidadeEmpresaTransportadora(UsuarioConverter.paraEntidade(usuarioDTO), usuarioDTO), listaIdsPerfil, null, null
        );

        for (Usuario usuario : usuarioRepository.findAll(specs)) {

            UsuarioDTO usuarioDTOAux = UsuarioConverter.paraDTO(usuario);

            if (usuario.getIdPerfil() != null) {
                Perfil perfil = perfilRepository.findOne(usuario.getIdPerfil());
                usuarioDTOAux.setDescricaoPerfil(perfil.getDescricao());
            }

            List<Empresa> empresas = empresaService.empresasPorUsuario(usuario);
            
            if (empresas != null && !empresas.isEmpty()) {
                usuarioDTOAux.setRazaoSocial(empresas.get(0).getRazaoSocial());
                if(empresas.get(0).getIdGrupoEconomico() != null) {
                    usuarioDTOAux.setNomeGrupoEconomico(this.grupoEconomicoRepository.findOne(empresas.get(0).getIdGrupoEconomico()).getNome());
                }
                
            }

            usuarioDTOAux = paraDTOEmpresaTransportadora(usuario, usuarioDTOAux);

            listaUsuario.add(usuarioDTOAux);
        }

        return listaUsuario;
    }

    @Override
    public void redefinirSenha(RedefinicaoSenhaDTO redefinicaoSenhaDTO) throws Exception {

        Usuario usuario;

        usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

        if (!Utils.isSenhaValida(redefinicaoSenhaDTO.getNovaSenha())) {
            throw new BadRequestResponseException(Mensagem.get("msg.validacao.senha"));

        } else if (!usuarioSegurancaService.passwordEncoder().matches(redefinicaoSenhaDTO.getSenhaAtual(), usuario.getSenha())) {
            throw new BadRequestResponseException(Mensagem.get("msg.senha.invalida"));

        } else if (!redefinicaoSenhaDTO.getConfirmaNovaSenha().equals(redefinicaoSenhaDTO.getNovaSenha())) {
            throw new BadRequestResponseException(Mensagem.get("msg.nova.senha.confirma.senha.nao.conferem"));
        }

        usuario.setSenha(usuarioSegurancaService.passwordEncoder().encode(redefinicaoSenhaDTO.getConfirmaNovaSenha()));
        usuario.setStatus(StatusUsuarioEnum.ATIVO.get());
        usuario.setPrimeiroAcesso(false);

        Usuario criado = usuarioRepository.save(usuario);

        if (criado != null) {
            auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado.senha", new Object[]{UTF8.Usuario}), null);
        }
    }

    @Override
    public void registrarLoginDoUsuario(Long idUsuario) {

        Usuario logado = usuarioRepository.findByIdUsuario(idUsuario);
        logado.setDataLogin(LocalDate.now());

        usuarioRepository.save(logado);

    }



    @Override
    public Set<UserEmpresaCADTO> getUserEmpresaCaList()  {
        Set<UserEmpresaCADTO> userEmpresaCADTOSet = new HashSet<>();

        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
        UsuarioDTO usuarioDTO = UsuarioConverter.paraDTO(usuario);
        List<Empresa> empresaList = empresaService.empresasPorUsuario(usuario);

        if (!CollectionUtils.isEmpty(empresaList)) {
            for(Empresa empresa : empresaList){

                usuarioDTO.setRazaoSocial(empresa.getRazaoSocial());
                EmpresaCA empresaCA = empresaCaService.buscaEmpresa(empresa.getCnpj());

                UserEmpresaCADTO userEmpresaCADTO = new UserEmpresaCADTO();
                userEmpresaCADTO.setCnpj(empresa.getCnpj());
                userEmpresaCADTO.setRazaoSocial(empresa.getRazaoSocial());

                if(Objects.nonNull(empresaCA) && Objects.nonNull(empresaCA.getId())){
                    userEmpresaCADTO.setIdEmpresaCa(empresaCA.getId());
                    userEmpresaCADTO.setTpoRemessaEmpresa(empresaCA.getTpoRemessa());
                    userEmpresaCADTO.setIdSegmento(empresaCA.getCodigoSegmento());

                    userEmpresaCADTOSet.add(userEmpresaCADTO);
                }
            }
        }

        return userEmpresaCADTOSet;
    }

    @Override
    public String esqueceuSenha(EsqueceuSenhaDTO esqueceuSenhaDTO) throws Exception {

        Usuario usuario;
        String email;

        if (usuarioRepository.existsByLogin(esqueceuSenhaDTO.getLogin())) {

            String senhaAleatoria = Utils.gerarSenhaAleatoria();

            usuario = usuarioRepository.findOneByLogin(esqueceuSenhaDTO.getLogin());
            usuario.setSenha(usuarioSegurancaService.passwordEncoder().encode(senhaAleatoria));
            usuario.setPrimeiroAcesso(true);

            email = usuarioRepository.save(usuario).getEmail();

            try {
                emailService.enviarEmail(usuario.getEmail(),
                        Mensagem.get("email.titulo.brinks.portal.primeiro.acesso"),
                        EmailTamplate.redefinirSenha(usuario.getLogin(), senhaAleatoria));

            } catch (Exception e) {
                LOGGER.error("enviarEmail", e);
            }

        } else {
            throw new UsuarioNaoExisteException(Mensagem.get("msg.registro.nao.valido"));
        }

        return this.transformaEmail(email);
    }

    @Override
    public String transformaEmail(String email) {

        StringBuilder emailAntesArroba = new StringBuilder(email.substring(0, email.indexOf("@")));
        StringBuilder emailDepoisArroba = new StringBuilder(email.substring(email.indexOf("@"), email.length()));

        if (emailAntesArroba.length() > 5) {

            emailAntesArroba.replace(0, emailAntesArroba.length() - 5, "*****");

        }

        return emailAntesArroba.append(emailDepoisArroba).toString();

    }

    private Usuario paraEntidadeEmpresaTransportadora(Usuario usuario, UsuarioDTO usuarioDTO) {



        if (usuarioDTO.getEmpresasTransportadoras() != null && !usuarioDTO.getEmpresasTransportadoras().isEmpty()) {
            List<Empresa> le = new ArrayList<>();
            usuarioDTO.getEmpresasTransportadoras().forEach(u -> {
                if (u.getEmpresas() != null) {
                    for (Long idEmpresa : u.getEmpresas()) {
                        Empresa e = new Empresa();
                        e.setIdEmpresa(idEmpresa);
                        le.add(e);
                    }
                }
            });
            usuario.setEmpresaList(le);
        }

        if (usuarioDTO.getEmpresasTransportadoras() != null && !usuarioDTO.getEmpresasTransportadoras().isEmpty()) {
            List<Transportadora> lt = new ArrayList<>();
            usuarioDTO.getEmpresasTransportadoras().forEach(u -> {
                if (u.getTransportadora() != null) {
                    Transportadora t = new Transportadora();
                    t.setIdTransportadora(u.getTransportadora());
                    lt.add(t);
                }
            });
            usuario.setTransportadoraList(lt);
        }

        return usuario;
    }

    private UsuarioDTO paraDTOEmpresaTransportadora(Usuario usuario, UsuarioDTO usuarioDTOAux) {

        if (usuario.getEmpresaList() != null && !usuario.getEmpresaList().isEmpty()) {
            List<EmpresaDTO> le = new ArrayList<>();
            usuario.getEmpresaList().forEach(e -> {
                Empresa empresa = empresaRepository.findOne(e.getIdEmpresa());
                EmpresaDTO dto = new EmpresaDTO();
                dto.setIdEmpresa(empresa.getIdEmpresa());
                dto.setRazaoSocial(empresa.getRazaoSocial());
                le.add(dto);
                usuarioDTOAux.setEmpresaList(le);
            });
        }

        if (usuario.getTransportadoraList() != null && !usuario.getTransportadoraList().isEmpty()) {
            List<TransportadoraDTO> lt = new ArrayList<>();
            usuario.getTransportadoraList().forEach(t -> {
                Transportadora tranportadora = transportadoraRepository.findOne(t.getIdTransportadora());
                TransportadoraDTO dto = new TransportadoraDTO();
                dto.setIdTransportadora(tranportadora.getIdTransportadora());
                dto.setRazaoSocial(tranportadora.getRazaoSocial());
                lt.add(dto);
                usuarioDTOAux.setTransportadoraList(lt);
            });
        }

        return usuarioDTOAux;
    }

    private void salvarNotificacao(UsuarioDTO dto, UsuarioDTO usuario, boolean excluir) {

        if (dto.getEmpresasTransportadoras() != null && !dto.getEmpresasTransportadoras().isEmpty()) {

            if (excluir) {
                try {
                    notificacaoService.excluir(usuario.getIdUsuario());
                } catch (Exception ex) {
                    LOGGER.error("Notificacoes: " + ex.getMessage(), ex);
                }
            }

            NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
            notificacaoDTO.setIdUsuario(usuario.getIdUsuario());
            notificacaoDTO.setUsuarioDTO(usuario);
            dto.getEmpresasTransportadoras().forEach(u -> {
                notificacaoDTO.setIdGrupoEconomico(u.getIdGrupoEmpresa());
                notificacaoDTO.setIdTransportadora(u.getTransportadora());
                u.getNotificacoes().forEach(un -> {
                    notificacaoDTO.setIdTipoNotificacao(un);
                    if (u.getEmpresas() != null && ! u.getEmpresas().isEmpty()) {
                        u.getEmpresas().forEach(e -> {
                            notificacaoDTO.setIdEmpresa(e);
                            criarNotificacao(notificacaoDTO);
                        });
                    } else {
                        notificacaoDTO.setIdEmpresa(null);
                        criarNotificacao(notificacaoDTO);
                    }
                });
            });
        }

    }

    private void criarNotificacao(NotificacaoDTO notificacaoDTO) {
        try {
            notificacaoService.criar(notificacaoDTO);
        } catch (Exception ex) {
            LOGGER.error("Notificacoes: " + ex.getMessage(), ex);
        }
    }

}
