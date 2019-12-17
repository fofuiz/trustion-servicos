package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.AtividadeConverter;
import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.CodigoTipoStatusOcorrencia;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
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
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoMotivoConclusaoRespository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.service.interfaces.IAuditoriaService;
import br.com.accesstage.trustion.service.interfaces.IOcorrenciaService;
import br.com.accesstage.trustion.util.EmailTamplate;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;

@Service
public class AtividadeService implements IAtividadeService {

    @Autowired
    private IAtividadeRepository atividadeRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IAuditoriaService auditoriaService;

    @Autowired
    private IOcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

    @Autowired
    private ITipoMotivoConclusaoRespository tipoMotivoConclusaoRespository;

    @Autowired
    private IGrupoEconomicoRepository grupoEconomicoRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IRelatorioAnaliticoCreditoRepository relatorioRepository;
    
    @Autowired
    private IRelatorioAnaliticoCreditoD1Repository relatorioD1Repository;
    
    
    @Autowired
    private IOcorrenciaService ocorrenciaService;

    private static final String MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA = "msg.auditoria.atividade.na.ocorrencia";

    @Override
    public AtividadeDTO criar(AtividadeDTO dto) throws Exception {

        dto.setDataHorario(Calendar.getInstance().getTime());
        dto.setDataCriacao(Calendar.getInstance().getTime());
        dto.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        dto.setIdUsuario(UsuarioAutenticado.getIdUsuario());

        if (null == dto.getResponsavel()) {
            if(dto.getIdOcorrencia() != null) {
                Ocorrencia ocorrencia = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
                if(ocorrencia != null) {
                    dto.setResponsavel(ocorrenciaService.getResponsavel(ocorrencia));
                }
            }
        }

        AtividadeDTO dtoCriado = null;
        dto.setAtividade("Ocorrência criada: " + dto.getAtividade());
        dto.setIdOcorrencia(dto.getIdOcorrencia());
        dto.setTipoAtividade(dto.getTipoAtividade() == null ? "M" : dto.getTipoAtividade());
        Atividade entidade = atividadeRepository.save(AtividadeConverter.paraEntidade(dto));

        if (entidade != null) {
            dtoCriado = AtividadeConverter.paraDTO(entidade);
            Usuario usuario = usuarioRepository.findOne(dtoCriado.getIdUsuario());

            if (usuario != null) {
                dtoCriado.setUsuario(usuario.getNome());
            }

            auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[]{entidade.getIdOcorrencia()}), entidade.getIdOcorrencia());
        }

        return dtoCriado;
    }

    @Override
    public AtividadeDTO criarPorAcao(AtividadeDTO dto) throws Exception {

        TipoMotivoConclusao tipoMotivoConclusao;
        Empresa empresa;
        GrupoEconomico grupoEconomico;
        List<Usuario> usuariosEmail;

        dto.setDataHorario(Calendar.getInstance().getTime());
        dto.setDataCriacao(Calendar.getInstance().getTime());
        dto.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
        dto.setIdUsuario(UsuarioAutenticado.getIdUsuario());

        AtividadeDTO dtoCriado = null;
        Ocorrencia ocorrencia;
        TipoStatusOcorrencia ocorrenciaAnterior;

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
        Atividade atividade = AtividadeConverter.paraEntidade(dto);
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
                atividade.setResponsavel(ocorrenciaService.getResponsavelAprovacao(ocorrencia));
            } else {
                atividade.setResponsavel(getResponsavel(atividade));
            }
        }
        
        Atividade entidade = atividadeRepository.save(atividade);

        if(ocorrencia.isIsOcorrenciaD1()) {
            RelatorioAnaliticoCreditoD1 relD1 = relatorioD1Repository.findOneByIdOcorrencia(dto.getIdOcorrencia());
            relD1.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relD1.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioD1Repository.save(relD1);
            
        }else {
            RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioRepository.findOneByIdOcorrencia(dto.getIdOcorrencia());
            relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioRepository.save(relatorioAnaliticoCredito);
        }

        if (entidade != null) {
            dtoCriado = AtividadeConverter.paraDTO(entidade);
            Usuario usuario = usuarioRepository.findOne(dtoCriado.getIdUsuario());

            if (!(null == entidade.getIdMotivo())) {
                tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(entidade.getIdMotivo());
                dtoCriado.setMotivoDescricao(tipoMotivoConclusao.getDescricao());
            }

            if (usuario != null) {
                dtoCriado.setUsuario(usuario.getNome());
            }

            String msg = Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[]{entidade.getIdOcorrencia()});
            auditoriaService.registrar(msg, entidade.getIdOcorrencia());
        }

        grupoEconomico = grupoEconomicoRepository.findByNome(dto.getResponsavel());

        empresa = empresaRepository.findByRazaoSocial(dto.getResponsavel());

        if (null == grupoEconomico && empresa !=null) {
            usuariosEmail = usuarioRepository.findByEmpresaList_IdEmpresa(empresa.getIdEmpresa());
            for (Usuario usuario : usuariosEmail) {
                emailService.enviarEmail(usuario.getEmail(), Mensagem.get("email.titulo.brinks.portal.notificacao"),
                        EmailTamplate.envioNotificacao(ocorrencia.getIdOcorrencia()));
            }
        }

        if (null == empresa && grupoEconomico !=null) {
            usuariosEmail = usuarioRepository.findByEmpresaList_IdGrupoEconomico(grupoEconomico.getIdGrupoEconomico());
            for (Usuario usuario : usuariosEmail) {
                emailService.enviarEmail(usuario.getEmail(), Mensagem.get("email.titulo.brinks.portal.notificacao"),
                        EmailTamplate.envioNotificacao(ocorrencia.getIdOcorrencia()));
            }
        }

        return dtoCriado;
    }

    private String getResponsavel(Atividade atividade) {
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
            } else {
                RelatorioAnaliticoCredito relD0 = relatorioRepository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
                for(Empresa empresa : usuario.getEmpresaList()) {
                    if(empresa.getIdEmpresa().equals(relD0.getIdEmpresa())) {
                        return grupoEconomicoRepository.findOne(empresa.getIdGrupoEconomico()).getNome();
                    }
                }
            }
        }
        
        return null;
    }  
    
        
    
    private void atualizaRelatorioAnaliticoCreditoD0(Ocorrencia ocorrencia, TipoStatusOcorrencia statusOcorrencia) {
        RelatorioAnaliticoCredito relatorioAnaliticoCredito = relatorioRepository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
        if (null != ocorrencia.getMesclas()) {
            relatorioAnaliticoCredito.setStatusConciliacao(StatusConciliacaoEnum.AJUSTE.get());
            relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioRepository.save(relatorioAnaliticoCredito);
        } else {
            relatorioAnaliticoCredito.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relatorioAnaliticoCredito.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioRepository.save(relatorioAnaliticoCredito);
        }        
    }
    
    private void atualizaRelatorioAnaliticoCreditoDN(Ocorrencia ocorrencia, TipoStatusOcorrencia statusOcorrencia) {
        RelatorioAnaliticoCreditoD1 relatorioEntidade = relatorioD1Repository.findOneByIdOcorrencia(ocorrencia.getIdOcorrencia());
        if (null != ocorrencia.getMesclas()) {
            relatorioEntidade.setStatusConciliacao(StatusConciliacaoEnum.AJUSTE.get());
            relatorioEntidade.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relatorioEntidade.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioD1Repository.save(relatorioEntidade);
        } else {
            relatorioEntidade.setDataStatusOcorrencia(ocorrencia.getDataStatusOcorrencia());
            relatorioEntidade.setStatusOcorrencia(statusOcorrencia.getDescricao());
            relatorioD1Repository.save(relatorioEntidade);

        }        
    }    

    @Override
    public AtividadeDTO aprovar(AtividadeDTO dto) throws Exception {

        AtividadeDTO dtoCriado = null;
        Ocorrencia ocorrencia;
        TipoStatusOcorrencia ocorrenciaAnterior;
        boolean aprovar = false;

        Atividade atividadeAlteracao = atividadeRepository.findOne(dto.getIdAtividade());
        Usuario usuarioCriacao = usuarioRepository.findByIdUsuario(atividadeAlteracao.getIdUsuario());
        Usuario usuarioAlteracao = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());

        String responsavelAprovacao = "";

        if ((usuarioCriacao.getIdUsuario().intValue() == usuarioAlteracao.getIdUsuario().intValue()) 
                && !CodigoPerfilEnum.BPO.get().equals(usuarioCriacao.getIdPerfil())) {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.nao.foi.possivel.aprovar", new Object[]{"Atividade"}));
        }
            
        if (ocorrenciaService.verificarPermissaoPorUsuario(usuarioCriacao, usuarioAlteracao)) {
            aprovar = true;
            responsavelAprovacao = getResponsavel(atividadeAlteracao);
        }


        if (aprovar) {
            ocorrencia = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
            
            
        
            dto.setDataCriacao(Calendar.getInstance().getTime());
            dto.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());

            ocorrenciaAnterior = tipoStatusOcorrenciaRepository.findOne(ocorrencia.getIdTipoStatusOcorrencia());
            TipoStatusOcorrencia statusOcorrencia = tipoStatusOcorrenciaRepository.findOne(4L);

            Atividade atividade = AtividadeConverter.paraEntidade(dto);
            atividade.setIdAtividade(null);
            atividade.setIdUsuario(usuarioCriacao.getIdUsuario());
            atividade.setAtividade("Status alterado de " + ocorrenciaAnterior.getDescricao() + " para "
                    + statusOcorrencia.getDescricao() + ", Observação: " + atividade.getAtividade());
            atividade.setTipoAtividade("M");
            atividade.setIdOcorrencia(dto.getIdOcorrencia());
            atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
            atividade.setIdtTipoOcorrencia(4L);
            atividade.setIdMotivo(dto.getIdMotivo() != null
                    ? dto.getIdMotivo()
                    : tipoMotivoConclusaoRespository.findByDescricao(dto.getMotivoDescricao()).getIdMotivo());
            atividade.setResponsavel(responsavelAprovacao);
            atividade.setDataHorario(Calendar.getInstance().getTime());
            Atividade entidade = atividadeRepository.save(atividade);
            ocorrencia.setIdTipoStatusOcorrencia(4L);
            ocorrencia.setDataStatusOcorrencia(Calendar.getInstance().getTime());
            ocorrenciaRepository.save(ocorrencia);
            
            if(ocorrencia.isIsOcorrenciaD1()) {
                atualizaRelatorioAnaliticoCreditoDN(ocorrencia, statusOcorrencia);
            } else {
                atualizaRelatorioAnaliticoCreditoD0(ocorrencia, statusOcorrencia);
            }
            
            if (entidade != null) {
                dtoCriado = AtividadeConverter.paraDTO(entidade);
                Usuario user = usuarioRepository.findOne(dtoCriado.getIdUsuario());

                if (user != null) {
                    dtoCriado.setUsuario(user.getNome());
                }

                auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[]{entidade.getIdOcorrencia()}), entidade.getIdOcorrencia());
            }
        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.nao.foi.possivel.aprovar", new Object[]{"Atividade"}));
        }
        return dtoCriado;
    }

    @Override
    public AtividadeDTO rejeitar(AtividadeDTO dto) throws Exception {
 
        AtividadeDTO dtoCriado = null;
        Ocorrencia ocorrencia;
        TipoStatusOcorrencia ocorrenciaAnterior;
        Atividade atividadeAlteracao = atividadeRepository.findOne(dto.getIdAtividade());
        Usuario usuarioCriacao = usuarioRepository.findByIdUsuario(atividadeAlteracao.getIdUsuario());
        Usuario usuarioAlteracao = usuarioRepository.findByIdUsuario(UsuarioAutenticado.getIdUsuario());

        if (ocorrenciaService.verificarPermissaoPorUsuario(usuarioCriacao, usuarioAlteracao)) {

            dto.setDataCriacao(Calendar.getInstance().getTime());
            dto.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            ocorrencia = ocorrenciaRepository.findOne(dto.getIdOcorrencia());
            ocorrenciaAnterior = tipoStatusOcorrenciaRepository.findOne(ocorrencia.getIdTipoStatusOcorrencia());

            Atividade atividade = AtividadeConverter.paraEntidade(dto);
            atividade.setIdAtividade(null);
            atividade.setIdUsuario(usuarioCriacao.getIdUsuario());
            atividade.setAtividade("Status alterado de " + ocorrenciaAnterior.getDescricao() + " para " + "Não Aprovado"
                    + ", Observação: " + atividade.getAtividade());
            atividade.setTipoAtividade("M");
            atividade.setIdOcorrencia(dto.getIdOcorrencia());
            atividade.setIdUsuarioCriacao(UsuarioAutenticado.getIdUsuario());
            atividade.setIdtTipoOcorrencia(5L);
            atividade.setIdMotivo(dto.getIdMotivo() != null
                    ? dto.getIdMotivo()
                    : tipoMotivoConclusaoRespository.findByDescricao(dto.getMotivoDescricao()).getIdMotivo());
            atividade
                    .setResponsavel(getResponsavel(atividade));
            atividade.setDataHorario(Calendar.getInstance().getTime());

            Atividade entidade = atividadeRepository.save(atividade);

            if (entidade != null) {
                dtoCriado = AtividadeConverter.paraDTO(entidade);
                Usuario usuario = usuarioRepository.findOne(dtoCriado.getIdUsuario());

                if (usuario != null) {
                    dtoCriado.setUsuario(usuario.getNome());
                }

                auditoriaService.registrar(Mensagem.get(MSG_AUDITORIA_ATIVIDADE_NA_OCORRENCIA, new Object[]{entidade.getIdOcorrencia()}), entidade.getIdOcorrencia());
            }
        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.nao.foi.possivel.rejeitar", new Object[]{"Atividade"}));
        }

        return dtoCriado;
    }

    @Override
    public AtividadeDTO alterar(AtividadeDTO dto) throws Exception {
        Atividade entidadePesquisa = atividadeRepository.findOne(dto.getIdAtividade());
        AtividadeDTO dtoAlterado = null;
        if (entidadePesquisa != null) {
            dto.setDataAlteracao(Calendar.getInstance().getTime());
            dto.setIdUsuarioAlteracao(UsuarioAutenticado.getIdUsuario());
            Atividade entidade = atividadeRepository.save(AtividadeConverter.paraEntidade(dto));
            if (entidade != null) {
                dtoAlterado = AtividadeConverter.paraDTO(entidade);
                Usuario usuario = usuarioRepository.findOne(dtoAlterado.getIdUsuario());
                if (usuario != null) {
                    dtoAlterado.setUsuario(usuario.getNome());
                }
                auditoriaService.registrar(Mensagem.get("msg.auditoria.alterado",
                        new Object[]{"Atividade na " + UTF8.ocorrencia + " de Id ", dtoAlterado.getIdOcorrencia()}), dtoAlterado.getIdOcorrencia());
            }
        } else {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"Atividade"}));
        }
        return dtoAlterado;
    }

    @Override
    public void excluir(Long atividadeId) throws Exception {

        if (atividadeId == null) {
            throw new BadRequestResponseException(
                    Mensagem.get("msg.registro.nao.existe", new Object[]{"Atividade"}));
        }

        atividadeRepository.delete(atividadeId);
    }

    @Override
    public List<AtividadeDTO> listarPorOcorrencia(Long idOcorrencia) throws Exception {

        List<AtividadeDTO> dtos = new ArrayList<>();
        TipoMotivoConclusao tipoMotivoConclusao;

        for (Atividade entidade : atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(idOcorrencia)) {

            AtividadeDTO dtoInner = AtividadeConverter.paraDTO(entidade);
            Usuario usuario = usuarioRepository.findOne(dtoInner.getIdUsuario());
            dtoInner.setStatusOcorrencia(
                    tipoStatusOcorrenciaRepository.findOne(entidade.getIdtTipoOcorrencia()).getDescricao() == null ? ""
                            : tipoStatusOcorrenciaRepository.findOne(entidade.getIdtTipoOcorrencia()).getDescricao());

            if (null != entidade.getIdMotivo()) {
                tipoMotivoConclusao = tipoMotivoConclusaoRespository.findOne(entidade.getIdMotivo());
                dtoInner.setMotivoDescricao(tipoMotivoConclusao.getDescricao());
                dtoInner.setIdMotivo(tipoMotivoConclusao.getIdMotivo());
            }

            if (usuario != null) {
                dtoInner.setUsuario(usuario.getNome());
            }

            dtos.add(dtoInner);

            if (!dtos.isEmpty()) {
                dtos.stream().max(Comparator.comparing(AtividadeDTO::getDataHorario)).ifPresent(atividade -> atividade.setHabilitado(true));
            }
        }
        return dtos;
    }

    @Override
    public List<AtividadeDTO> listarTodos() throws Exception {
        List<AtividadeDTO> dtos = new ArrayList<>();
        for (Atividade entidade : atividadeRepository.findAll()) {
            
            Ocorrencia ocorrencia = ocorrenciaRepository.findOne(entidade.getIdOcorrencia());
            if(!ocorrencia.isIsOcorrenciaD1()) {
                AtividadeDTO dtoInner = AtividadeConverter.paraDTO(entidade);
                dtoInner.setMotivoDescricao(tipoMotivoConclusaoRespository.findOne(entidade.getIdMotivo()).getDescricao());
                Usuario usuario = usuarioRepository.findOne(dtoInner.getIdUsuario());
                if (usuario != null) {
                    dtoInner.setUsuario(usuario.getNome());
                }
                dtos.add(dtoInner);
            }
        }
        return dtos;
    }

}
