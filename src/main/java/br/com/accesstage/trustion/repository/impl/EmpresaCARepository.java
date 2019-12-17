package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.EmpresaCADTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EmpresaCARepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    public Optional<List<EmpresaCADTO>> buscarFiliais(final Long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<EmpresaCA> query = builder.createQuery(EmpresaCA.class);
        Root<EmpresaCA> from = query.from(EmpresaCA.class);
        CriteriaQuery<EmpresaCA> select = query.select(from);
        from.fetch("matriz");
        select.where(
                builder.equal(from.get("matriz"), id)
        ).orderBy(
                builder.asc(from.get("razaoSocial"))
        );
        TypedQuery<EmpresaCA> typedQuery = em.createQuery(select);
        List<EmpresaCA> results = typedQuery.getResultList();
        List<EmpresaCADTO> empresaCADTOList = new ArrayList<>();
        if (results != null) {
            empresaCADTOList = results.stream()
                    .map(empresaCA -> empreCADTO(EmpresaCADTO.builder(), empresaCA).build())
                    .collect(Collectors.toList());
        }

        EmpresaCA empresaCA = getMatriz(id);

        EmpresaCADTO empresaCADTO = empreCADTO(EmpresaCADTO.builder(), empresaCA).build();
        empresaCADTOList.add(empresaCADTO);

        return Optional.of(empresaCADTOList);
    }

    private EmpresaCA getMatriz(Long empId) {
        EmpresaCA empresaCA = new EmpresaCA();
        try {
            empresaCA = (EmpresaCA) em.createQuery("SELECT e FROM EmpresaCA e LEFT JOIN FETCH e.matriz m where e.id = :id and m IS NULL").setParameter("id", empId).getSingleResult();

        } catch (NoResultException e) {
            LOGGER.info("Não Foi Encontrada a matriz com EmpId: " + empId);
        }
        return empresaCA;
    }

    private EmpresaCADTO.EmpresaCADTOBuilder empreCADTO(EmpresaCADTO.EmpresaCADTOBuilder builder, EmpresaCA empresaCA) {
        return builder.cnpj(empresaCA.getCnpj())
                .codigoSegmento(empresaCA.getCodigoSegmento())
                .codMensalSaldoAberto(empresaCA.getCodMensalSaldoAberto())
                .codSemanalSaldoAberto(empresaCA.getCodSemanalSaldoAberto())
                .docTypeExp(empresaCA.getDocTypeExp())
                .docTypeExpVendas(empresaCA.getDocTypeExpVendas())
                .docTypeRetRem(empresaCA.getDocTypeRetRem())
                .dscDocTypeExportAutoRelatVolumetriaVenda(empresaCA.getDscDocTypeExportAutoRelatVolumetriaVenda())
                .dscDocTypeSaldo(empresaCA.getDscDocTypeSaldo())
                .dscDocTypeVendaConciliadaManual(empresaCA.getDscDocTypeVendaConciliadaManual())
                .dscReceiverExportAutoRelatVolumetriaVenda(empresaCA.getDscReceiverExportAutoRelatVolumetriaVenda())
                .dscReceiverSaldo(empresaCA.getDscReceiverSaldo())
                .dscReceiverVendaConciliadaManual(empresaCA.getDscReceiverVendaConciliadaManual())
                .dscSenderExportAutoRelatVolumetriaVenda(empresaCA.getDscSenderExportAutoRelatVolumetriaVenda())
                .dscSenderSaldo(empresaCA.getDscSenderSaldo())
                .dscSenderVendaConciliadaManual(empresaCA.getDscSenderVendaConciliadaManual())
                .gerarHashVenda(empresaCA.getGerarHashVenda())
                .horaExec(empresaCA.getHoraExec())
                .id(empresaCA.getId())
                .nroDiasFlexData(empresaCA.getNroDiasFlexData())
                .nroDiasRelCust(empresaCA.getNroDiasRelCust())
                .nroHoraExportAutoRelatVolumetriaVenda(empresaCA.getNroHoraExportAutoRelatVolumetriaVenda())
                .nroHoraSaldo(empresaCA.getNroHoraSaldo())
                .nroHoraVendaConciliadaManual(empresaCA.getNroHoraVendaConciliadaManual())
                .nroHoraVendas(empresaCA.getNroHoraVendas())
                .razaoSocial(empresaCA.getRazaoSocial())
                .receiverExp(empresaCA.getReceiverExp())
                .receiverExpVendas(empresaCA.getReceiverExpVendas())
                .receiverRetRem(empresaCA.getReceiverRetRem())
                .senderExp(empresaCA.getSenderExp())
                .senderExpVendas(empresaCA.getSenderExpVendas())
                .senderRetRem(empresaCA.getSenderRetRem())
                .staConciliacao(empresaCA.getStaConciliacao())
                .staConciliaFlexData(empresaCA.getStaConciliaFlexData())
                .staEmpresa(empresaCA.getStaEmpresa())
                .staExportacao(empresaCA.getStaConciliaFlexData())
                .staExportAutoRelatVolumetriaVenda(empresaCA.getStaExportAutoRelatVolumetriaVenda())
                .staExportVendas(empresaCA.getStaExportAutoRelatVolumetriaVenda())
                .staImplantacao(empresaCA.getStaImplantacao())
                .staRetornoRemessa(empresaCA.getStaConciliaFlexData())
                .staSaldoAberto(empresaCA.getStaSaldoAberto())
                .staVendaConciliadaManual(empresaCA.isStaVendaConciliadaManual())
                .tpoConciliacao(empresaCA.getTpoConciliacao())
                .tpoPeriodoSaldoAberto(empresaCA.getTpoPeriodoSaldoAberto())
                .tpoRemessa(empresaCA.getTpoRemessa())
                .ultSeqArqBaixas(empresaCA.getUltSeqArqBaixas())
                .matriz(empresaCA.getMatriz() == null ? null : empreCADTO(EmpresaCADTO.builder(), empresaCA.getMatriz()).build());
    }

    public EmpresaCA findByCnpj(String cnpj) {
        EmpresaCA empresaCA = new EmpresaCA();
        try {

            empresaCA = (EmpresaCA) em.createQuery("SELECT e FROM EmpresaCA e where e.cnpj = :cnpj").setParameter("cnpj", cnpj).getSingleResult();

        } catch (NoResultException e) {
            LOGGER.info("Não foi encontrado a Empresa com esse CNPJ:" + cnpj);
        }
        return empresaCA;
    }

    public EmpresaCA find(Long id) {
        return em.find(EmpresaCA.class, id);
    }
    
    @Transactional
    public EmpresaCA merge(EmpresaCA empresaCA) {
        return em.merge(empresaCA);
    }
}
