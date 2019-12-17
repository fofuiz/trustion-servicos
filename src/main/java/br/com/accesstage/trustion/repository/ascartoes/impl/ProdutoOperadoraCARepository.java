package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoOperadoraCARepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo para listar produtos por operadora para o combo box de produto
     * @param codOperadora
     * @return
     */
    public List<ProdutoOperadoraCA> listarProdutoPorOperadora(Long codOperadora) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + codOperadora);

        List<ProdutoOperadoraCA> listProdutoOperadora;

        try {
            String sqlQuery = "select distinct new br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA"
                    + "(pca.id, pca.nome, pca.permiteSelecionar) from ProdutoOperadoraCA p, "
                    + "ProdutoCA pca where p.codOperadora = :codOperadora "
                    + "and pca.id = p.codProduto and pca.permiteSelecionar = :permiteSelecionar "
                    + "order by pca.nome";

            Query query = em.createQuery(sqlQuery);
            query.setParameter("codOperadora", codOperadora);
            query.setParameter("permiteSelecionar", 1);

            listProdutoOperadora = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo ProdutoOperadoraCARepository.listarProdutoPorOperadora " + ex.getMessage());
            throw ex;
        }
        return listProdutoOperadora;
    }

    public List<ProdutoOperadoraCA> buscarProdutoPorOperadoraECodProduto(Long codOperadora, Long codProduto) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + codOperadora);

        List<ProdutoOperadoraCA> listProdutoOperadora;

        try {
            String sqlQuery = "select distinct new br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA"
                    + "(pca.id, pca.nome, pca.permiteSelecionar) "
                    + "from ProdutoOperadoraCA p, "
                    + "ProdutoCA pca where p.codOperadora = :codOperadora and p.codProduto = :codProduto "
                    + "and pca.id = p.codProduto and pca.permiteSelecionar = :permiteSelecionar "
                    + "order by pca.nome";

            Query query = em.createQuery(sqlQuery);
            query.setParameter("codOperadora", codOperadora);
            query.setParameter("codProduto", codProduto);
            query.setParameter("permiteSelecionar", 1);

            listProdutoOperadora = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo ProdutoOperadoraCARepository.buscarProdutoPorOperadoraECodProduto " + ex.getMessage());
            throw ex;
        }
        return listProdutoOperadora;
    }

}
