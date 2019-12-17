package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.util.Funcoes;
import br.com.accesstage.trustion.util.Utils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Repository
public class TaxaAntecipacaoRepository {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo responsavel por realizar a consulta conforme sql
     * @param filtro
     * @return
     */
    public List<TaxaAntecipacaoDTO> consulta(FiltroTaxaAntecipacaoDTO filtro) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        List<TaxaAntecipacaoDTO> listaTaxasAntecipacao;

        try {
            String sqlQuery = Funcoes
                    .getSqlString(this.getClass().getClassLoader()
                            .getResourceAsStream("br/com/accesstage/ascartoes/sql/TaxaAntecipacao.sql"))
                    .toString();

            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter("idsEmp", filtro.getIdsEmp());
            query.setParameter("dataInicial", filtro.getDataInicial());
            query.setParameter("dataFinal", filtro.getDataFinal());

            query.unwrap(SQLQuery.class)
                    .addScalar("loja", StringType.INSTANCE)
                    .addScalar("operadora", StringType.INSTANCE)
                    .addScalar("produto", StringType.INSTANCE)
                    .addScalar("plano", StringType.INSTANCE)
                    .addScalar("taxa", DoubleType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TaxaAntecipacaoDTO.class));

            listaTaxasAntecipacao = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo TaxaAntecipacaoRepository.consulta " + ex.getMessage());
            throw ex;
        }
        return listaTaxasAntecipacao;
    }

    /**
     * Metodo responsavel por realizar a consulta paginada conforme sql
     * @param filtro
     * @param pageable
     * @return
     */
    public Page<TaxaAntecipacaoDTO> consultaPage(FiltroTaxaAntecipacaoDTO filtro, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        List<TaxaAntecipacaoDTO> listaTaxaAntecipacao;
        Page<TaxaAntecipacaoDTO> listaPageTaxaAntecipacao;

        try {
            String sqlQuery = Funcoes
                    .getSqlString(this.getClass().getClassLoader()
                            .getResourceAsStream("br/com/accesstage/ascartoes/sql/TaxaAntecipacaoPage.sql"))
                    .toString();

            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter("idsEmp", filtro.getIdsEmp());
            query.setParameter("dataInicial", filtro.getDataInicial());
            query.setParameter("dataFinal", filtro.getDataFinal());
            query.setParameter("pageNumber", pageable.getPageNumber());
            query.setParameter("pageSize", pageable.getPageSize());

            query.unwrap(SQLQuery.class)
                    .addScalar("loja", StringType.INSTANCE)
                    .addScalar("operadora", StringType.INSTANCE)
                    .addScalar("produto", StringType.INSTANCE)
                    .addScalar("plano", StringType.INSTANCE)
                    .addScalar("taxa", DoubleType.INSTANCE)
                    .addScalar("totalRegistros", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TaxaAntecipacaoDTO.class));

            listaTaxaAntecipacao = query.getResultList();
            Integer totalRegistros = 0;
            if (!listaTaxaAntecipacao.isEmpty()) {
                totalRegistros = listaTaxaAntecipacao.get(0).getTotalRegistros();
            }
            listaPageTaxaAntecipacao = new PageImpl<>(listaTaxaAntecipacao, pageable, totalRegistros);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo TaxaAntecipacaoRepository.consultaPage " + ex.getMessage());
            throw ex;
        }
        return listaPageTaxaAntecipacao;
    }

}
