package br.com.accesstage.trustion.repository.ascartoes.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import br.com.accesstage.trustion.configs.log.Log;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class FatoTransacaoRepository {

    public static final long serialVersionUID = 3684601946132047633L;

    @Log
    private Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public FatoTransacao listarHash(String hashValue) {
        LOGGER.info(">> Inicio do metodo - FatoTransacaoRepository.listarHash");

        FatoTransacao fatoTransacao = (FatoTransacao) em.createNamedQuery("FatoTransacao.findByHash")
                .setParameter("hashValue", hashValue).getSingleResult();

        LOGGER.info("<< Fim do metodo - FatoTransacaoRepository.listarHash");

        return fatoTransacao;
    }

    @Transactional
    public FatoTransacao merge(FatoTransacao fatoTransacao) {
        em.joinTransaction();
        return em.merge(fatoTransacao);
    }
}
