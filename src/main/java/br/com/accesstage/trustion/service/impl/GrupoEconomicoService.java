package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.GrupoEconomicoConverter;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.GrupoEconomicoSpecification;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IGrupoEconomicoService;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import br.com.accesstage.trustion.util.ValidarCNPJ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.*;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdUsuario;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

@Service
public class GrupoEconomicoService implements IGrupoEconomicoService {

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IRelatorioAnaliticoCreditoService relatorioAnaliticoCreditoService;

    private final Converter<GrupoEconomico, GrupoEconomicoDTO> converter = (GrupoEconomico source) -> GrupoEconomicoConverter.paraDTO(source);

    @Override
    public GrupoEconomicoDTO criar(GrupoEconomicoDTO grupoTO) throws Exception {

        GrupoEconomico grupo;

        //Valida o CNPJ
        if (StringUtils.isNotBlank(grupoTO.getCnpj())) {
            ValidarCNPJ.validarCNPJ(grupoTO.getCnpj());
        }

        GrupoEconomicoDTO grupoCriado = new GrupoEconomicoDTO();
        if (!grupoRepository.existsByCnpj(grupoTO.getCnpj())) {
            grupo = GrupoEconomicoConverter.paraEntidade(grupoTO);
            grupo.setDataCriacao(Calendar.getInstance().getTime());
            grupo.setIdUsuarioCriacao(grupoTO.getIdUsuarioCriacao());
            grupoCriado = GrupoEconomicoConverter.paraDTO(grupoRepository.save(grupo));


            auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{"Grupo", grupoCriado.getNome()}), null);

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"Grupo"}));
        }
        return grupoCriado;
    }

    @Override
    public GrupoEconomicoDTO alterar(GrupoEconomicoDTO grupoTO) throws Exception {

        GrupoEconomicoDTO grupoAlterado = new GrupoEconomicoDTO();

        GrupoEconomico grupo = grupoRepository.findOne(grupoTO.getIdGrupoEconomico());

        if (grupo != null) {

            grupo.setNome(grupoTO.getNome());
            grupo.setIdUsuarioAlteracao(grupoTO.getIdUsuarioAlteracao());
            grupo.setDataAlteracao(Calendar.getInstance().getTime());

            grupoAlterado = GrupoEconomicoConverter.paraDTO(grupoRepository.save(grupo));

            empresaRepository.findOne(grupoAlterado.getIdUsuarioAlteracao());

            auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{"Grupo", grupoAlterado.getNome()}), null);

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Grupo"}));
        }
        return grupoAlterado;
    }

    @Override
    public boolean excluir(Long idGrupoEconomico) throws Exception {
        if (grupoRepository.exists(idGrupoEconomico)) {
            grupoRepository.delete(idGrupoEconomico);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Grupo"}));
        }
        return true;
    }

    @Override
    public List<GrupoEconomicoDTO> listarTodos() throws Exception {
        List<GrupoEconomicoDTO> listaDTO = new ArrayList<>();
        grupoRepository.findAll().forEach((grupo) -> {
            listaDTO.add(GrupoEconomicoConverter.paraDTO(grupo));
        });
        return listaDTO;
    }

    @Override
    public List<GrupoEconomicoDTO> listarTodosPorIdOcorrencia(Long idOcorrencia) throws Exception {

        List<GrupoEconomicoDTO> listaDTO = new ArrayList<>();

        RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO = relatorioAnaliticoCreditoService.pesquisarPorIdOcorrencia(idOcorrencia);

        Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(new GrupoEconomico(), Arrays.asList(relatorioAnaliticoCreditoDTO.getIdGrupoEconomico()));

        grupoRepository.findAll(specs).forEach((grupo) -> {
            listaDTO.add(GrupoEconomicoConverter.paraDTO(grupo));
        });
        return listaDTO;
    }

    @Override
    public List<GrupoEconomicoDTO> listarPorUsuario(Long idUsuario) throws Exception {
        List<GrupoEconomicoDTO> dtos = new ArrayList<>();
        if (usuarioRepository.exists(idUsuario)) {
            grupoRepository.findAllByIdUsuarioCriacao(idUsuario).forEach((grupoEconomico) -> {
                dtos.add(GrupoEconomicoConverter.paraDTO(grupoEconomico));
            });
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }
        return dtos;
    }

    @Override
    public List<GrupoEconomicoDTO> listarPorPerfilUsuario(Long idPerfil) throws Exception {
        List<GrupoEconomicoDTO> dtos = new ArrayList<>();

        if (idPerfil == ADMINISTRADOR.get()) {
            List<GrupoEconomico> entidades = grupoRepository.findAll();
            entidades.forEach((entidade) -> {
                dtos.add(GrupoEconomicoConverter.paraDTO(entidade));
            });

        } else {
            Usuario usuarioLogado = usuarioRepository.findOne(getIdUsuario());
            Set<Long> idsGrupos = new HashSet<>();

            if (idPerfil == MASTER_TRANSPORTADORA.get()
                    || idPerfil == OPERADOR_TRANSPORTADORA.get()) {
                List<EmpresaDTO> empresaDTOs = empresaService.listaEmpresasPorUsuarioLogado();

                if (isNotEmpty(empresaDTOs)) {
                    empresaDTOs.forEach(empresaDTO -> {
                        idsGrupos.add(empresaDTO.getIdGrupoEconomico());
                    });
                }

            } else if (BPO.get().equals(idPerfil) 
                    || MASTER_CLIENTE.get().equals(idPerfil)
                    || MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(idPerfil)
                    || MASTER_CLIENTE_CARTAO.get().equals(idPerfil)
                    || MASTER_CLIENTE_NUMERARIO.get().equals(idPerfil)
                    || OPERADOR_CLIENTE_CARTAO.get().equals(idPerfil)
                    || OPERADOR_CLIENTE_NUMERARIO.get().equals(idPerfil)
                    || OPERADOR_CLIENTE.get().equals(idPerfil)
                    || OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(idPerfil)) {

                List<Empresa> empresaList = usuarioLogado.getEmpresaList();

                if (isNotEmpty(empresaList)) {
                    empresaList.forEach(empresa -> {
                        idsGrupos.add(empresa.getIdGrupoEconomico());
                    });
                }

            } 
           

            if (isNotEmpty(idsGrupos)) {
                idsGrupos.forEach(idGrupo -> {
                    GrupoEconomico grupo = grupoRepository.findOne(idGrupo);
                    if (grupo != null) {
                        dtos.add(GrupoEconomicoConverter.paraDTO(grupo));
                    }
                });
            }
        }
        return dtos;
    }

    @Override
    public GrupoEconomicoDTO pesquisar(Long idGrupo) throws Exception {
        GrupoEconomico grupo = grupoRepository.findOne(idGrupo);
        if (grupo != null) {
            return GrupoEconomicoConverter.paraDTO(grupo);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"grupo"}));
        }
    }

    @Override
    public List<GrupoEconomicoDTO> listarGruposSpecs(GrupoEconomicoDTO dto) throws Exception {

        List<GrupoEconomicoDTO> dtos = new ArrayList<>();

        Usuario usuarioPesquisa = usuarioRepository.findOne(dto.getIdUsuarioCriacao());
        List<Long> idsGrupos = new ArrayList<>();
        if (usuarioPesquisa != null) {
            if (MASTER_TRANSPORTADORA.get().equals(usuarioPesquisa.getIdPerfil())
            || MASTER_CLIENTE.get().equals(usuarioPesquisa.getIdPerfil())
            || MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
            || MASTER_CLIENTE_CARTAO.get().equals(usuarioPesquisa.getIdPerfil())
            || MASTER_CLIENTE_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
            || BPO.get().equals(usuarioPesquisa.getIdPerfil())) {
                
                idsGrupos = empresaService.empresasPorUsuario(usuarioPesquisa).stream().map(empresa -> empresa.getIdGrupoEconomico()).collect(Collectors.toList());
                
                if (!idsGrupos.isEmpty()) {
                    Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(GrupoEconomicoConverter.paraEntidade(dto), idsGrupos);
                    List<GrupoEconomico> gruposListados = grupoRepository.findAll(specs);
                    if (isNotEmpty(gruposListados)) {
                        gruposListados.forEach((grupo) -> {
                            dtos.add(GrupoEconomicoConverter.paraDTO(grupo));
                        });
                    }
                }
                return dtos;

            
            }

            Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(GrupoEconomicoConverter.paraEntidade(dto), idsGrupos);
            List<GrupoEconomico> gruposListados = grupoRepository.findAll(specs);
            if (isNotEmpty(gruposListados)) {
                gruposListados.forEach((grupo) -> {
                    dtos.add(GrupoEconomicoConverter.paraDTO(grupo));
                });
            }
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }
        return dtos;
    }

    @Override
    public Page<GrupoEconomicoDTO> listarGruposSpecs(GrupoEconomicoDTO dto, Pageable pageable) throws Exception {
        Page<GrupoEconomicoDTO> dtos = new PageImpl<>(new ArrayList<>());
        Usuario usuarioPesquisa = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
        List<Long> idsGrupos = new ArrayList<>();

        if (usuarioPesquisa != null) {
            if (MASTER_TRANSPORTADORA.get().equals(usuarioPesquisa.getIdPerfil())) {
                idsGrupos = empresaService.empresasPorUsuario(usuarioPesquisa).stream().map(empresa -> empresa.getIdGrupoEconomico()).collect(Collectors.toList());

                if (!idsGrupos.isEmpty()) {
                    Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(GrupoEconomicoConverter.paraEntidade(dto), idsGrupos);
                    Page<GrupoEconomico> gruposListados = grupoRepository.findAll(specs, pageable);
                    dtos = gruposListados.map(converter);
                }

                return dtos;
            }

            Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(GrupoEconomicoConverter.paraEntidade(dto), idsGrupos);
            Page<GrupoEconomico> gruposListados = grupoRepository.findAll(specs, pageable);
            dtos = gruposListados.map(converter);

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }

        return dtos;
    }

    @Override
    public List<GrupoEconomicoDTO> listarGruposSpecsOutros(GrupoEconomicoDTO dto) throws Exception {
        List<GrupoEconomicoDTO> dtos = new ArrayList<>();

        Usuario usuarioPesquisa = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());

        List<Long> idsGrupos = new ArrayList<>();
        if (usuarioPesquisa != null) {
            if (MASTER_TRANSPORTADORA.get().equals(usuarioPesquisa.getIdPerfil()) 
                    || OPERADOR_TRANSPORTADORA.get().equals(usuarioPesquisa.getIdPerfil())
                    || MASTER_CLIENTE.get().equals(usuarioPesquisa.getIdPerfil())
                    || MASTER_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
                    || MASTER_CLIENTE_CARTAO.get().equals(usuarioPesquisa.getIdPerfil())
                    || MASTER_CLIENTE_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
                    || OPERADOR_CLIENTE.get().equals(usuarioPesquisa.getIdPerfil())
                    || OPERADOR_CLIENTE_VENDA_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
                    || OPERADOR_CLIENTE_CARTAO.get().equals(usuarioPesquisa.getIdPerfil())
                    || OPERADOR_CLIENTE_NUMERARIO.get().equals(usuarioPesquisa.getIdPerfil())
                    || BPO.get().equals(usuarioPesquisa.getIdPerfil())) {

                idsGrupos = empresaService.empresasPorUsuario(usuarioPesquisa).stream().map(empresa -> empresa.getIdGrupoEconomico()).collect(Collectors.toList());

            } else if ( ADMINISTRADOR.get().equals(usuarioPesquisa.getIdPerfil())) {
                return dtos;
            }

            if (!idsGrupos.isEmpty()) {
                Specification<GrupoEconomico> specs = GrupoEconomicoSpecification.byCriterio(GrupoEconomicoConverter.paraEntidade(dto), idsGrupos);
                List<GrupoEconomico> gruposListados = grupoRepository.findAll(specs);
                if (isNotEmpty(gruposListados)) {
                    gruposListados.forEach((grupo) -> {
                        dtos.add(GrupoEconomicoConverter.paraDTO(grupo));
                    });
                }
            }
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{UTF8.usuario}));
        }
        return dtos;
    }
}
