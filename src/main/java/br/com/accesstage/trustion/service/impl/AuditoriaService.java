package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.AuditoriaConverter;
import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.model.Auditoria;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.repository.criteria.AuditoriaSpecification;
import br.com.accesstage.trustion.repository.interfaces.IAuditoriaRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService implements IAuditoriaService {

    @Autowired
    private IAuditoriaRepository auditoriaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    private final Sort orderByDataAcao = new Sort(Sort.Direction.DESC, "dataAcao");

    private final Converter<Auditoria, AuditoriaDTO> converter = (Auditoria auditoria) -> {
        AuditoriaDTO dto = AuditoriaConverter.paraDTO(auditoria);
        if (auditoria.getGrupoEconomico() != null) {
            dto.setGrupoEconomico(auditoria.getGrupoEconomico());
        }
        if (auditoria.getEmpresa() != null) {
            dto.setEmpresa(auditoria.getEmpresa());
        }
        if (auditoria.getUsuario() != null) {
            dto.setUsuario(auditoria.getUsuario());
        }
        return dto;
    };

    @Override
    public List<AuditoriaDTO> listarPorUsuario(AuditoriaDTO dto) throws Exception {

        List<AuditoriaDTO> dtos = new ArrayList<>();

        List<Long> idsEmpresa = listaIdsEmpresaUsuarioLogado();

        Specification<Auditoria> auditoriaSpecs = AuditoriaSpecification.byCriterio(dto, idsEmpresa);
        List<Auditoria> auditorias = auditoriaRepository.findAll(auditoriaSpecs, orderByDataAcao);

        for (Auditoria auditoria : auditorias) {
            dtos.add(converter.convert(auditoria));
        }
        return dtos;
    }

    @Override
    public Page<AuditoriaDTO> listarPorUsuario(AuditoriaDTO dto, Pageable pageable) throws Exception {

        Page<AuditoriaDTO> dtos;

        List<Long> idsEmpresa = listaIdsEmpresaUsuarioLogado();

        Specification<Auditoria> auditoriaSpecs = AuditoriaSpecification.byCriterio(dto, idsEmpresa);
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), orderByDataAcao);

        Page<Auditoria> auditorias = auditoriaRepository.findAll(auditoriaSpecs, pageRequest);
        dtos = auditorias.map(converter);

        return dtos;
    }


    @Override
    public AuditoriaDTO registrar(String acao, Long idOcorrencia ) throws Exception {

        Auditoria auditoria = new Auditoria();

        auditoria.setIdUsuario(UsuarioAutenticado.getIdUsuario());
        auditoria.setUsuario(UsuarioAutenticado.getNome());
        if(idOcorrencia != null) {
            auditoria.setNroOcorrencia(String.valueOf(idOcorrencia));
        }


        if(idOcorrencia != null) {

            OcorrenciaDTO ocorrenciaDTO = ocorrenciaService.pesquisar(Long.parseLong(auditoria.getNroOcorrencia()));

            auditoria.setIdGrupoEconomico(ocorrenciaDTO.getIdGrupoEconomico());
            auditoria.setGrupoEconomico(ocorrenciaDTO.getGrupoEconomico());

            auditoria.setIdEmpresa(ocorrenciaDTO.getIdEmpresa());
            auditoria.setEmpresa(ocorrenciaDTO.getEmpresa());


        }else {
            List<EmpresaDTO> listaEmpresas = empresaService.listaEmpresasPorUsuarioLogado();

            listaEmpresas.forEach(e -> {
                auditoria.setIdGrupoEconomico(e.getIdGrupoEconomico());
                auditoria.setGrupoEconomico(grupoRepository.findOne(e.getIdGrupoEconomico()).getNome());

                auditoria.setIdEmpresa(e.getIdEmpresa());
                auditoria.setEmpresa(empresaRepository.findOne(e.getIdEmpresa()).getRazaoSocial());
            });
        }

        auditoria.setDataAcao(Calendar.getInstance().getTime());
        auditoria.setAcao(acao);

        AuditoriaDTO criado = AuditoriaConverter.paraDTO(auditoriaRepository.save(auditoria));

        return criado;
    }


    /**
     * Metodo para retornar uma {@link List} contendo os ids das {@link Empresa}s do usuario logado
     * @return retorna uma lista de id empresa.
     */
    private List<Long> listaIdsEmpresaUsuarioLogado() throws Exception {

        List<Long> idsEmpresa = new ArrayList<>();

        List<EmpresaDTO> listaEmpresas = empresaService.listaEmpresasPorUsuarioLogado();
        listaEmpresas.forEach(empresaDTO -> {
            idsEmpresa.add(empresaDTO.getIdEmpresa());
        });

        return idsEmpresa;
    }

}
