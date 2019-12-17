package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OperadoraCARepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;


    public List<OperadoraCA> buscarOperadoraPorEmpId(Long empId) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + empId);

        List<OperadoraCA> list;

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

}
