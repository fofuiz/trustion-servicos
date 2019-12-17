package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PontoVendaCARepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo para listar produtos por operadora para o combo box de produto
     *
     * @param empId
     * @return
     */
    public List<PontoVendaCA> listarTodosNulos(Long empId) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + empId);

        List<PontoVendaCA> list;

        try {
            String sqlQuery = "select p from PontoVendaCA p where p.idEmpresa = :idEmpresa "
                    + " and p.numeroTerminal not like :nroTerminal "
                    + " and p.idLoja is null "
                    + " order by p.numeroTerminal asc";

            Query query = em.createQuery(sqlQuery);
            query.setParameter("idEmpresa", empId);
            query.setParameter("nroTerminal", "SLVLoja%");

            list = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo PontoVendaCARepository.listarTodosNulos " + ex.getMessage());
            throw ex;
        }
        return list;
    }


    public List<PontoVendaCA> buscarPorCodLoja(Long codLoja) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + codLoja);

        List<PontoVendaCA> list;

        try {
            String sqlQuery = "select p "
                    + " from  PontoVendaCA p "
                    + " where p.numeroTerminal not like :nroTerminal "
                    + " and p.idLoja = :codLoja "
                    + " order by p.numeroTerminal asc ";

            Query query = em.createQuery(sqlQuery);
            query.setParameter("codLoja", codLoja);
            query.setParameter("nroTerminal", "SLVLoja%");

            list = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo PontoVendaCARepository.buscarPorCodLoja " + ex.getMessage());
            throw ex;
        }
        return list;
    }

}
