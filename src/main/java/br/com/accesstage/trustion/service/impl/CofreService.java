package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.CofreConverter;
import br.com.accesstage.trustion.dto.CofreDTO;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Cofre;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.criteria.CofreSpecification;
import br.com.accesstage.trustion.repository.interfaces.ICofreRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IPerfilRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.ICofreService;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.Utils;

@Service
public class CofreService implements ICofreService {

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private ICofreRepository cofreRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IPerfilRepository perfilRepository;

    @Autowired
    private IEmpresaService empresaService;

    @Override
    public CofreDTO criar(CofreDTO cofreDTO) throws Exception {
        CofreDTO dtoCriado = new CofreDTO();

        if (!cofreRepository.existsByIdEquipamento(cofreDTO.getIdEquipamento()) && !cofreRepository.existsByNumSerie(cofreDTO.getNumSerie())) {


            Cofre cofre = CofreConverter.paraEntidade(cofreDTO);
            cofre.setCodCofreTransportadora(cofreDTO.getCodigoNaTransportadora());
            cofre.setDataCriacao(Calendar.getInstance().getTime());

            dtoCriado = CofreConverter.paraDTO(cofreRepository.save(cofre));

            auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{"Cofre", dtoCriado.getIdCofre()}), null);

        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"cofre"}));
        }
        return dtoCriado;
    }

    @Override
    public CofreDTO alterar(CofreDTO cofreDTO) throws Exception {

        Cofre cofre = cofreRepository.findOne(cofreDTO.getIdCofre());
        cofre.setDataCriacao(Calendar.getInstance().getTime());
        cofre.setCodCofreTransportadora(cofreDTO.getCodigoNaTransportadora());

        if (!cofre.getIdEquipamento().equals(cofreDTO.getIdEquipamento())) {
            if (cofreRepository.existsByIdEquipamento(cofreDTO.getIdEquipamento())) {
                throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"cofre"}));
            }
        }
        if (!cofre.getNumSerie().equals(cofreDTO.getNumSerie())) {
            if (cofreRepository.existsByNumSerie(cofreDTO.getNumSerie())) {
                throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"cofre"}));
            }
        }

        BeanUtils.copyProperties(cofreDTO, cofre);

        cofre.setCodCofreTransportadora(cofreDTO.getCodigoNaTransportadora());
        cofre.setIdUsuarioAlteracao(cofreDTO.getIdUsuarioAlteracao());
        cofre.setDataAlteracao(Calendar.getInstance().getTime());

        CofreDTO dtoAlterado = CofreConverter.paraDTO(cofreRepository.save(cofre));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{"Cofre", dtoAlterado.getIdCofre()}), null);

        return dtoAlterado;
    }

    @Override
    public CofreDTO pesquisar(Long idCofreDTO) throws Exception {
        Cofre cofre = cofreRepository.findOne(idCofreDTO);

        CofreDTO cofreDTO = CofreConverter.paraDTO(cofre);

        return cofreDTO;
    }

    @Override
    public boolean excluir(Long idCofre) throws Exception {
        return false;
    }

    @Override
    public Page<CofreDTO> listarCriterios(CofreDTO cofreDTO, Pageable pageable) throws Exception {
        Page<CofreDTO> dtos;
        List<Long> idsGrupos = new ArrayList<>();

        Specification<Cofre> specs = CofreSpecification.byCriterio(CofreConverter.paraEntidade(cofreDTO), idsGrupos);

        dtos = cofreRepository.findAll(specs, pageable).map((Cofre cofre) -> {
            CofreDTO cofreDTOAux = CofreConverter.paraDTO(cofre);

            if (cofre.getIdGrupoEconomico() != null) {
                GrupoEconomico grupoEconomico = grupoRepository.findOne(cofre.getIdGrupoEconomico());
                cofreDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
            }

            if (cofre.getIdEmpresa() != null) {
                Empresa empresa = empresaRepository.findOne(cofre.getIdEmpresa());
                cofreDTOAux.setRazaoSocial(empresa.getRazaoSocial());
            }

            return cofreDTOAux;
        });

        return dtos;
    }

    @Override
    public List<CofreDTO> listarCriterios(CofreDTO cofreDTO) throws Exception {
        List<CofreDTO> dtos = new ArrayList<>();
        List<Long> idsGrupos = new ArrayList<>();

        Usuario usuario = usuarioRepository.findOne(UsuarioAutenticado.getIdUsuario());
        

        if (CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuario.getIdPerfil())) {
            List<Empresa> empresas = empresaService.empresasPorUsuario(usuario);
            if(empresas != null && !empresas.isEmpty()) {
                empresas.stream().forEach((empresa) -> {
                    idsGrupos.add(empresa.getIdGrupoEconomico());
                });
            }
        }

        Specification<Cofre> specs = CofreSpecification.byCriterio(CofreConverter.paraEntidade(cofreDTO), idsGrupos);

        for (Cofre cofre : cofreRepository.findAll(specs)) {

            CofreDTO cofreDTOAux = CofreConverter.paraDTO(cofre);

            if (cofre.getIdGrupoEconomico() != null) {
                GrupoEconomico grupoEconomico = grupoRepository.findOne(cofre.getIdGrupoEconomico());
                cofreDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
            }

            if (cofre.getIdEmpresa() != null) {
                Empresa empresa = empresaRepository.findOne(cofre.getIdEmpresa());
                cofreDTOAux.setRazaoSocial(empresa.getRazaoSocial());
            }

            dtos.add(cofreDTOAux);
        }

        return dtos;
    }

    @Override
    public List<CofreDTO> listarCriterios(List<EmpresaDTO> listaEmpresaFiltroDTO) throws Exception {

        List<CofreDTO> listaCofreDTO = new ArrayList<>();
        List<Long> listaIdEmpresa = new ArrayList<>();

        if (Utils.isNotEmpty(listaEmpresaFiltroDTO)) {
            listaEmpresaFiltroDTO.forEach((empresaDTO) -> {
                listaIdEmpresa.add(empresaDTO.getIdEmpresa());
            });

            Specification<Cofre> specs = CofreSpecification.byCriterio(listaIdEmpresa);

            for (Cofre cofre : cofreRepository.findAll(specs)) {

                CofreDTO cofreDTOAux = CofreConverter.paraDTO(cofre);

                if (cofre.getIdGrupoEconomico() != null) {
                    GrupoEconomico grupoEconomico = grupoRepository.findOne(cofre.getIdGrupoEconomico());
                    cofreDTOAux.setNomeGrupoEconomico(grupoEconomico.getNome());
                }

                if (cofre.getIdEmpresa() != null) {
                    Empresa empresa = empresaRepository.findOne(cofre.getIdEmpresa());
                    cofreDTOAux.setRazaoSocial(empresa.getRazaoSocial());
                }

                listaCofreDTO.add(cofreDTOAux);
            }
        }

        return listaCofreDTO;
    }

}
