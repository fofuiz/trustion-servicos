package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoCloCA;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Caio Felipe Bispo Moraes
 * @since 17/09/2015
 */
@Repository
public class FatoTransacaoCloRepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public FatoTransacaoCloCA listarHashClo(String hashValue) {
        return (FatoTransacaoCloCA) em.createNamedQuery("FatoTransacaoClo.findByHash")
                .setParameter("hashValue", hashValue).getSingleResult();
    }

    @Transactional
    public FatoTransacaoCloCA merge(FatoTransacaoCloCA fatoTransacaoClo) {
        em.joinTransaction();
        return em.merge(fatoTransacaoClo);
    }

}
