package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.TransportadoraConverter;
import br.com.accesstage.trustion.dto.*;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.repository.criteria.TransportadoraSpecification;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.ITransportadoraService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.ValidarCNPJ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.accesstage.trustion.enums.StatusAtivoInativo;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.EmpresaModeloNegocio;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaModeloNegocioRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;

import static br.com.accesstage.trustion.enums.CodigoPerfilEnum.*;
import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdUsuario;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import static br.com.accesstage.trustion.util.Utils.isNotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class TransportadoraService implements ITransportadoraService {

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    @Autowired
    private IAuditoriaService auditoriaService;
    
    @Autowired
    private IEmpresaService empresaService;
    
    
    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private IEmpresaModeloNegocioRepository empresaModeloNegocioRepository;
    

    private static final String TRANSPORTADORA = "transportadora";

    @Override
    public TransportadoraDTO criar(TransportadoraDTO dto) throws Exception {

        Transportadora transportadora;
        TransportadoraDTO transportadoraCriada = new TransportadoraDTO();

        if(dto.getCnpj() != null && !dto.getCnpj().isEmpty()) {
            ValidarCNPJ.validarCNPJ(dto.getCnpj());
        }
        
        if (transportadoraRepository.existsByCnpj(dto.getCnpj())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"cnpj"}));
        }

        transportadora = TransportadoraConverter.paraEntidade(dto);
        
        transportadora.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        transportadora.setDataCriacao(Calendar.getInstance().getTime());

        transportadoraCriada = TransportadoraConverter.paraDTO(transportadoraRepository.save(transportadora));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.criado", new Object[]{TRANSPORTADORA, transportadoraCriada.getRazaoSocial()}), null);

        return transportadoraCriada;
    }

    @Override
    public TransportadoraDTO alterar(TransportadoraDTO dto) throws Exception {

        Transportadora transportadora;
        TransportadoraDTO transportadoraAlterada = new TransportadoraDTO();

        transportadora = transportadoraRepository.findOne(dto.getIdTransportadora());

        if (transportadora == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{TRANSPORTADORA}));
        }

        if (!transportadora.getCnpj().equals(dto.getCnpj()) && transportadoraRepository.existsByCnpj(dto.getCnpj())) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.ja.existe", new Object[]{"CNPJ"}));
        }

        transportadora = TransportadoraConverter.paraEntidadeComID(dto);
        
        transportadora.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
        transportadora.setDataAlteracao(Calendar.getInstance().getTime());

        transportadoraAlterada = TransportadoraConverter.paraDTO(transportadoraRepository.save(transportadora));

        auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado", new Object[]{TRANSPORTADORA, transportadoraAlterada.getRazaoSocial()}), null);

        return transportadoraAlterada;
    }

    @Override
    public boolean excluir(Long idTransportadora) throws Exception {

        if (transportadoraRepository.exists(idTransportadora)) {
            transportadoraRepository.delete(idTransportadora);
        } else {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{TRANSPORTADORA}));
        }

        return true;
    }

    @Override
    public List<TransportadoraDTO> listarTodos() throws Exception {

        List<TransportadoraDTO> lista = new ArrayList<>();

        transportadoraRepository.findAll().stream().map((t) -> TransportadoraConverter.paraDTO(t)).forEachOrdered((dto) -> {
            
            lista.add(dto);
        });

        return lista;
    }

    @Override
    public TransportadoraDTO pesquisar(Long id) throws Exception {

        Transportadora t = transportadoraRepository.findOne(id);
        
        TransportadoraDTO dto = TransportadoraConverter.paraDTO(t);
       
        return dto;
    }

    @Override
    public Page<TransportadoraDTO> listarCriterios(TransportadoraDTO dto, Pageable pageable) throws Exception {

        Page<TransportadoraDTO> lista;

        Specification<Transportadora> specs = TransportadoraSpecification.byCriterio(
                TransportadoraConverter.paraEntidade(dto) );

        lista = transportadoraRepository.findAll(specs, pageable).map(new Converter<Transportadora, TransportadoraDTO>() {

            @Override
            public TransportadoraDTO convert(Transportadora transportadora) {

                TransportadoraDTO transportadoraDTOAux = TransportadoraConverter.paraDTO(transportadora);

                return transportadoraDTOAux;
            }
        });

        return lista;
    }

    @Override
    public List<TransportadoraDTO> listarCriterios(TransportadoraDTO dto) throws Exception {

        List<TransportadoraDTO> lista = new ArrayList<>();

        Specification<Transportadora> specs = TransportadoraSpecification.byCriterio(
                TransportadoraConverter.paraEntidade(dto));

        Specifications<Transportadora> where = null;
       final CodigoPerfilEnum usuarioPerfil = UsuarioAutenticado.getPerfilEnum();

        switch (usuarioPerfil){
            case MASTER_TRANSPORTADORA:
            case OPERADOR_TRANSPORTADORA:
                Specification<Transportadora> specsUsuarioTrans = TransportadoraSpecification.byUsuarioTransp(UsuarioAutenticado.getIdUsuario());

                where =  Specifications.where(specs).and(specsUsuarioTrans);
                break;
            case MASTER_CLIENTE:
            case MASTER_CLIENTE_VENDA_NUMERARIO:
            case MASTER_CLIENTE_CARTAO:
            case MASTER_CLIENTE_NUMERARIO:
            case OPERADOR_CLIENTE:
            case OPERADOR_CLIENTE_VENDA_NUMERARIO:
            case OPERADOR_CLIENTE_CARTAO:
            case OPERADOR_CLIENTE_NUMERARIO:
            case BPO:
                Specification<Transportadora> specsUsuarioEmp = TransportadoraSpecification.byUsuarioEmpr(UsuarioAutenticado.getIdUsuario());

                where =  Specifications.where(specs).and(specsUsuarioEmp);
                break;
            case ADMINISTRADOR:
                where =  Specifications.where(specs);
                break;

                default:
                    throw  new Exception(Mensagem.get("msg.usario.perfil.sem.permissao", new Object[]{usuarioPerfil.name()}));

        }



        transportadoraRepository.findAll(where).stream().map((transportadora) -> TransportadoraConverter.paraDTO(transportadora)).forEachOrdered((transportadoraDTOAux) -> {
            lista.add(transportadoraDTOAux);
        });

        return lista;
    }

    @Override
    public List<TransportadoraDTO> listarPorPerfilUsuarioETipoCredito(Long idPerfil, Long tipoCredito) throws Exception {
        List<TransportadoraDTO> dtos = new ArrayList<>();
        Set<EmpresaModeloNegocio> empresaModeloNegocioList = new HashSet();

        if (ADMINISTRADOR.get().equals(idPerfil)) {
            empresaModeloNegocioList.addAll(empresaModeloNegocioRepository.findDistinctModeloNegocioByModeloNegocio_idTipoCreditoAndModeloNegocio_transportadora_status(tipoCredito, StatusAtivoInativo.A.getTexto()));
        } else {
            
            Usuario usuarioLogado = usuarioRepository.findOne(getIdUsuario());
            
            if (MASTER_TRANSPORTADORA.get().equals(idPerfil)
                    || OPERADOR_TRANSPORTADORA.get().equals(idPerfil)) {
                List<EmpresaDTO> empresaDTOs = empresaService.listaEmpresasPorUsuarioLogado();
                if (isNotEmpty(empresaDTOs)) {
                    empresaDTOs.forEach(empresaDTO -> {
                        empresaModeloNegocioList.addAll(
                                empresaModeloNegocioRepository.findByEmpresa_IdEmpresaAndModeloNegocio_idTipoCreditoAndModeloNegocio_transportadora_status(empresaDTO.getIdEmpresa(), tipoCredito, StatusAtivoInativo.A.getTexto())
                                .stream()
                                .filter(empresaModeloNegocio -> {
                                    boolean match = false;
                                    for(EmpresaModeloNegocioDTO empresaModeloNegocioDTO: empresaDTO.getEmpresaModeloNegocios()){
                                        if (empresaModeloNegocioDTO.getIdModeloNegocio().equals(empresaModeloNegocio.getEmpresaModeloNegocioPK().getIdModeloNegocio())) {
                                            match = true;
                                            break;
                                        }
                                    }
                                    return match;
                                }).collect(Collectors.toList())
                        );
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
                        empresaModeloNegocioList.addAll(empresaModeloNegocioRepository.findByEmpresa_IdEmpresaAndModeloNegocio_idTipoCreditoAndModeloNegocio_transportadora_status(empresa.getIdEmpresa(), tipoCredito, StatusAtivoInativo.A.getTexto()));
                    });
                }
            }
        }
        
        // Para cada transportadora obtem a lista de grupo economico, necessario para montagem dos combos 
        // nas telas de Consulta Analitica de Credito D0 e DN
        if (!empresaModeloNegocioList.isEmpty()) {
            TransportadoraDTO dto;
            GrupoEconomicoDTO geDto;
            for(EmpresaModeloNegocio empresaModeloNegocio : empresaModeloNegocioList) {
                dto = TransportadoraConverter.paraDTO(empresaModeloNegocio.getModeloNegocio().getTransportadora());

                geDto = new GrupoEconomicoDTO();
                geDto.setIdGrupoEconomico(empresaModeloNegocio.getEmpresa().getIdGrupoEconomico());

                if(!dtos.contains(dto)) {
                    dto.setOutrasEmpresas(new ArrayList<>());
                    dto.getOutrasEmpresas().add(geDto);
                    dtos.add(dto);
                } else {
                    dto = dtos.get(dtos.indexOf(dto));
                    if(!dto.getOutrasEmpresas().contains(geDto)) {
                        dto.getOutrasEmpresas().add(geDto);
                    }
                }                
            }
        } 

        return dtos;

    }    
}
