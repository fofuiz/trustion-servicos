package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoRdcCA;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Caio Felipe Bispo Moraes
 * @author raphael oliveira
 * @since 17/09/2015
 */
@Repository
public class FatoTransacaoRdcRepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public FatoTransacaoRdcCA listarHashRdc(String hashValue) {
        return (FatoTransacaoRdcCA) em.createNamedQuery("FatoTransacaoRdc.findByHash").setParameter("hashValue", hashValue).getSingleResult();
    }

    @Transactional
    public FatoTransacaoRdcCA merge(FatoTransacaoRdcCA fatoTransacaoRdc) {
        em.joinTransaction();
        return em.merge(fatoTransacaoRdc);
    }
}
