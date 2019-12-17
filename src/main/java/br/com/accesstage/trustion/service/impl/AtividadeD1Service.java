package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import br.com.accesstage.trustion.dto.AuditoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.AtividadeD1Converter;
import br.com.accesstage.trustion.dto.AtividadeD1DTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoStatusOcorrencia;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;
import br.com.accesstage.trustion.model.TipoMotivoConclusao;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IGrupoEconomicoRepository;
import br.com.accesstage.trustion.repository.interfaces.IOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoD1Repository;
import br.com.accesstage.trustion.repository.interfaces.ITipoMotivoConclusaoRespository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAtividadeD1Service;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.util.EmailTamplate;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import java.util.Objects;

@Service
public class AtividadeD1Service implements IAtividadeD1Service {

    @Autowired
    private IAtividadeRepository atividadeRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IOcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

    @Autowired
    private ITipoMotivoConclusaoRespository tipoMotivoConclusaoRespository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IEmpresaRepository empresaRepository;
    
    @Autowired
    private IRelatorioAnaliticoCreditoD1Repository relatorioD1Repository;
    

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    private static final String MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA = "msg.auditoria.atividade.na.ocorrencia";

    

    @Override
    public AtividadeD1DTO criarPorAcao(AtividadeD1DTO dto) throws Exception {

        AtividadeD1DTO dtoAtividade = null;
        Ocorrencia ocorrenciaAltera = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
        TipoMotivoConclusao tipoMotivoConclusao = null;
        Empresa empresa = null;
        GrupoEconomico grupoEconomico = null;
        List<Usuario> usuariosEmail = null;

        if (Objects.equals(ocorrenciaAltera.getIdTipoStatusOcorrencia(), dto.getStatusOcorrencia())) {
            return dtoAtividade;
        }

        dto.setDataHorario(Calendar.getInstance().getTime());
        dto.setDataCriacao(Calendar.getInstance().getTime());
        dto.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        dto.setIdUsuario(UsuarioAutenticado.getIdUsuario());

        AtividadeD1DTO dtoCriado = null;
        Ocorrencia ocorrencia = null;
        TipoStatusOcorrencia ocorrenciaAnterior = null;

        // Se status Concluida deve primeiro Aguardar Aprovacao
        if (CodigoTipoStatusOcorrencia.CONCLUIDA.getId().toString().equals(dto.getStatusOcorrencia())) {
            dto.setStatusOcorrencia(CodigoTipoStatusOcorrencia.AGUARDANDO_APROVACAO.getId().toString());
        }

        ocorrencia = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
        ocorrenciaAnterior = tipoStatusOcorrenciaRepository.findOne(ocorrencia.getIdTipoStatusOcorrencia());
        ocorrencia.setIdTipoStatusOcorrencia(Long.valueOf(dto.getStatusOcorrencia()));
        ocorrenciaRepository.save(ocorrencia);
        
        TipoStatusOcorrencia statusOcorrencia = null;

        if (null == dto.getStatusOcorrencia()) {
            statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(ocorrenciaAnterior.getIdTipoStatusOcorrencia());
        } else {
            statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(Long.valueOf(dto.getStatusOcorrencia()));
        }

        Atividade atividade = AtividadeD1Converter.paraEntidade(dto);
        atividade.setIdUsuario(UsuarioAutenticado.getIdUsuario());
        atividade.setAtividade("Status alterado de " + ocorrenciaAnterior.getDescricao() + " para "
                + statusOcorrencia.getDescricao() + ", Observação: " + atividade.getAtividade());
        atividade.setTipoAtividade("M");
        atividade.setIdOcorrencia(dto.getIdOcorrencia());
        atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        atividade.setIdtTipoOcorrencia(statusOcorrencia.getIdTipoStatusOcorrencia());
        atividade.setIdMotivo(dto.getIdMotivo());
        atividade.setDataHorario(Calendar.getInstance().getTime());

        if (null == dto.getResponsavel() || dto.getResponsavel().trim().isEmpty()) {
            if(CodigoTipoStatusOcorrencia.AGUARDANDO_APROVACAO.getId().toString().equals(dto.getStatusOcorrencia())) {
                atividade.setResponsavel(getResponsavelAprovacao(ocorrencia));
            } else {
                atividade.setResponsavel(getResponsavel(atividade));
            }
        }

        Atividade entidade = atividadeRepository.save(atividade);

        RelatorioAnaliticoCreditoD1 relatorioAnaliticoCredito = relatorioD1Repository
                .findOneByIdOcorrencia(dto.getIdOcorrencia());
        relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
        relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
        relatorioD1Repository.save(relatorioAnaliticoCredito);

        if (entidade != null) {
            dtoCriado = AtividadeD1Converter.paraDTO(entidade);
            Usuario usuario = usuarioRepository.findOne(dtoCriado.getIdUsuario());

            if (!(null == entidade.getIdMotivo())) {
                tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(entidade.getIdMotivo());
                dtoCriado.setMotivoDescricao(tipoMotivoConclusao.getDescricao());
            }

            if (usuario != null) {
                dtoCriado.setUsuario(usuario.getNome());
            }

            auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[]{entidade.getIdOcorrencia()}), entidade.getIdOcorrencia());
        }

        grupoEconomico = grupoEconomicoRepository.findByNome(dto.getResponsavel());
        empresa = empresaRepository.findByRazaoSocial(dto.getResponsavel());

        if (null == grupoEconomico) {
            usuariosEmail = usuarioRepository.findByEmpresaList_IdEmpresa(empresa.getIdEmpresa());
            for (Usuario usuario : usuariosEmail) {
                emailService.enviarEmail(usuario.getEmail(), Mensagem.get("email.titulo.brinks.portal.notificacao"),
                        EmailTamplate.envioNotificacao(ocorrencia.getIdOcorrencia()));
            }
        }

        if (null == empresa) {
            usuariosEmail = usuarioRepository.findByEmpresaList_IdGrupoEconomico(grupoEconomico.getIdGrupoEconomico());
            for (Usuario usuario : usuariosEmail) {
                emailService.enviarEmail(usuario.getEmail(), Mensagem.get("email.titulo.brinks.portal.notificacao"),
                        EmailTamplate.envioNotificacao(ocorrencia.getIdOcorrencia()));
            }
        }

        return dtoCriado;
    }

    @Override
    public AtividadeD1DTO alterar(AtividadeD1DTO dto) throws Exception {

        Atividade entidadePesquisa = atividadeRepository.findOne(dto.getIdAtividade());
        AtividadeD1DTO dtoAlterado = null;

        if (entidadePesquisa != null) {
            dto.setDataAlteracao(Calendar.getInstance().getTime());
            dto.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            Atividade entidade = atividadeRepository.save(AtividadeD1Converter.paraEntidade(dto));
            if (entidade != null) {
                dtoAlterado = AtividadeD1Converter.paraDTO(entidade);
                Usuario usuario = usuarioRepository.findOne(dtoAlterado.getIdUsuario());
                if (usuario != null) {
                    dtoAlterado.setUsuario(usuario.getNome());
                }
                auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado",
                        new Object[]{"Atividade na " + UTF8.ocorrencia + " de Id ", dtoAlterado.getIdOcorrencia()}), dtoAlterado.getIdOcorrencia());
            }

        } else {
            throw new ResourceNotFoundException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Atividade"}));
        }

        return dtoAlterado;
    }

    @Override
    public void excluir(Long idAtividade) throws Exception {

        if (idAtividade == null) {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"Atividade"}));
        }

        atividadeRepository.delete(idAtividade);
    }

    @Override
    public List<AtividadeD1DTO> listarPorOcorrencia(Long idOcorrencia) throws Exception {

        List<AtividadeD1DTO> dtos = new ArrayList<>();
        TipoMotivoConclusao tipoMotivoConclusao = null;

        for (Atividade entidade : atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(idOcorrencia)) {

            AtividadeD1DTO dtoInner = AtividadeD1Converter.paraDTO(entidade);

            Usuario usuario = usuarioRepository.findOne(dtoInner.getIdUsuario());
            dtoInner.setStatusOcorrencia(
                    tipoStatusOcorrenciaRepository.findOne(entidade.getIdtTipoOcorrencia()).getDescricao());

            if (null != entidade.getIdMotivo()) {
                tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(entidade.getIdMotivo());
                dtoInner.setMotivoDescricao(tipoMotivoConclusao.getDescricao());
                dtoInner.setIdMotivo(tipoMotivoConclusao.getIdMotivo());
            }

            if (usuario != null) {
                dtoInner.setUsuario(usuario.getNome());
            }

            dtos.add(dtoInner);
        }

        if (!dtos.isEmpty()) {
            dtos.stream().max(Comparator.comparing(AtividadeD1DTO::getDataHorario))
                    .ifPresent(atividadeD1 -> atividadeD1.setHabilitado(true));
        }

        return dtos;
    }

    @Override
    public List<AtividadeD1DTO> listarTodos() throws Exception {
        List<AtividadeD1DTO> dtos = new ArrayList<>();
        for (Atividade entidade : atividadeRepository.findAll()) {
            Ocorrencia ocorrencia = ocorrenciaRepository.findOne(entidade.getIdOcorrencia());
            if(ocorrencia.isIsOcorrenciaD1()) {
                AtividadeD1DTO dtoInner = AtividadeD1Converter.paraDTO(entidade);
                Usuario usuario = usuarioRepository.findOne(dtoInner.getIdUsuario());
                if (usuario != null) {
                    dtoInner.setUsuario(usuario.getNome());
                }
                dtos.add(dtoInner);
            }
        }
        return dtos;
    }
    
    
    public String getResponsavel(Atividade atividade) {
        Usuario usuario = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());
        if(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get().equals(usuario.getIdPerfil()) 
                || CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get().equals(usuario.getIdPerfil())) {
            
            if(usuario.getTransportadoraList().size() == 1) {
                return usuario.getTransportadoraList().get(0).getRazaoSocial();
            }
            Ocorrencia ocorrencia = this.ocorrenciaRepository.findOne(atividade.getIdOcorrencia());
            for(Transportadora t : usuario.getTransportadoraList()) {
                if(t.getIdTransportadora().equals(ocorrencia.getIdTransportadora())) {
                    return t.getRazaoSocial();
                }
            }
        }
        else {
            if(usuario.getEmpresaList().size() == 1) {
                return this.grupoEconomicoRepository.findOne(usuario.getEmpresaList().get(0).getIdGrupoEconomico()).getNome();
            }
            Ocorrencia ocorrencia = this.ocorrenciaRepository.findOne(atividade.getIdOcorrencia());
            if(ocorrencia.isIsOcorrenciaD1()) {
                RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
                for(Empresa empresa : usuario.getEmpresaList()) {
                    if(empresa.getIdEmpresa().equals(relD1.getIdEmpresa())) {
                        return grupoEconomicoRepository.findOne(empresa.getIdGrupoEconomico()).getNome();
                    }
                }
            } 
        }
        
        return null;
    } 
    
    private String getResponsavelAprovacao(Ocorrencia ocorrencia) {
        Usuario usuario = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());
        String responsavel = "";
    
        if(CodigoPerfilEnum.MASTER_CLIENTE.get().equals(usuario.getIdPerfil()) 
                || CodigoPerfilEnum.OPERADOR_CLIENTE.get().equals(usuario.getIdPerfil())) {
            
            // Responsabilidade de aprovar será da Transportadora
            if(ocorrencia.getIdTransportadora() != null) {
                responsavel = transportadoraRepository.findOne(ocorrencia.getIdTransportadora()).getRazaoSocial();
            }
            
        } else {
            // Responsabilidade de aprovar será da Empresa
            RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());            
            if(relD1 != null && relD1.getIdGrupoEconomico() != null) {
                responsavel = this.grupoEconomicoRepository.findOne(relD1.getIdGrupoEconomico()).getNome();
            }
        }

        return responsavel;
    }      
    

}
