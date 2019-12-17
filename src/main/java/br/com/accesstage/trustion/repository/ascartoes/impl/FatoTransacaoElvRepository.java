package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoElvCA;
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
public class FatoTransacaoElvRepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public FatoTransacaoElvCA listarHashElv(String hashValue) {
        return (FatoTransacaoElvCA) em.createNamedQuery("FatoTransacaoElv.findByHash")
                .setParameter("hashValue", hashValue)
                .getSingleResult();
    }
    
    @Transactional
    public FatoTransacaoElvCA merge(FatoTransacaoElvCA fatoTransacaoElv) {
        em.joinTransaction();
        return em.merge(fatoTransacaoElv);
    }
}
