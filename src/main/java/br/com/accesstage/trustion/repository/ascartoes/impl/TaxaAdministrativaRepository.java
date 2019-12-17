package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.ascartoes.model.ProdutoCA;
import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import br.com.accesstage.trustion.util.Format;
import br.com.accesstage.trustion.util.Funcoes;
import br.com.accesstage.trustion.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TaxaAdministrativaRepository {

    @Autowired
    @Qualifier(value = "cartoesDataSource")
    DataSource dataSource;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    private static final Double VARIACAO_TAXA = 0.05;

    private static final String C = "C";
    private static final String D = "D";
    private static final String[] tipoArray = {C, D};

    /**
     * Metodo responsavel por realizar a consulta conforme sql
     *
     * @param filtro
     * @return
     */
    public List<TaxaAdministrativaDTO> consulta(FiltroTaxaAdministrativaDTO filtro) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        List<TaxaAdministrativaDTO> lstTaxasPraticadas;

        try {
            String sqlQuery = Funcoes
                    .getSqlString(this.getClass().getClassLoader()
                            .getResourceAsStream("br/com/accesstage/ascartoes/sql/TaxasAdministrativasPraticadas.sql"))
                    .toString();

            if (filtro.getCodLoja() != null && filtro.getCodLoja() != 0) {
                sqlQuery = sqlQuery.replace(":pvOuLoja", " AND l.cod_loja = " + filtro.getCodLoja());
            } else {
                sqlQuery = sqlQuery.replace(":pvOuLoja", "");
            }

            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter("idsEmp", filtro.getIdsEmp());
            query.setParameter("dataInicial", filtro.getDataInicial());
            query.setParameter("dataFinal", filtro.getDataFinal());
            if (!C.equals(filtro.getTipo()) && !D.equals(filtro.getTipo())) {
                query.setParameter("tipo", Arrays.asList(tipoArray));
            } else {
                query.setParameter("tipo", filtro.getTipo());
            }

            query.unwrap(SQLQuery.class)
                    .addScalar("loja", StringType.INSTANCE)
                    .addScalar("pontoVenda", StringType.INSTANCE)
                    .addScalar("operadora", StringType.INSTANCE)
                    .addScalar("produto", StringType.INSTANCE)
                    .addScalar("plano", BigDecimalType.INSTANCE)
                    .addScalar("taxa", DoubleType.INSTANCE)
                    .addScalar("txCadastrada", DoubleType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TaxaAdministrativaDTO.class));

            lstTaxasPraticadas = ajustarListagem(query.getResultList(), filtro);

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo TaxaAdministrativaRepository.consulta " + ex.getMessage());
            throw ex;
        }
        return lstTaxasPraticadas;
    }

    /**
     * Metodo responsavel por realizar a consulta paginada conforme sql
     *
     * @param filtro
     * @param pageable
     * @return
     */
    public List<TaxaAdministrativaDTO> consultaPage(FiltroTaxaAdministrativaDTO filtro, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + filtro);

        List<TaxaAdministrativaDTO> listaTaxaAdministrativa;

        try {
            String sqlQuery = Funcoes
                    .getSqlString(this.getClass().getClassLoader()
                            .getResourceAsStream("br/com/accesstage/ascartoes/sql/TaxasAdministrativasPraticadasPage.sql"))
                    .toString();

            if (filtro.getCodLoja() != null && filtro.getCodLoja() != 0) {
                sqlQuery = sqlQuery.replace(":pvOuLoja", " AND l.cod_loja = " + filtro.getCodLoja());
            } else {
                sqlQuery = sqlQuery.replace(":pvOuLoja", "");
            }

            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter("idsEmp", filtro.getIdsEmp());
            query.setParameter("dataInicial", filtro.getDataInicial());
            query.setParameter("dataFinal", filtro.getDataFinal());
            query.setParameter("pageNumber", pageable.getPageNumber());
            query.setParameter("pageSize", pageable.getPageSize());
            if (!C.equals(filtro.getTipo()) && !D.equals(filtro.getTipo())) {
                query.setParameter("tipo", Arrays.asList(tipoArray));
            } else {
                query.setParameter("tipo", filtro.getTipo());
            }

            query.unwrap(SQLQuery.class)
                    .addScalar("loja", StringType.INSTANCE)
                    .addScalar("pontoVenda", StringType.INSTANCE)
                    .addScalar("operadora", StringType.INSTANCE)
                    .addScalar("produto", StringType.INSTANCE)
                    .addScalar("plano", BigDecimalType.INSTANCE)
                    .addScalar("taxa", DoubleType.INSTANCE)
                    .addScalar("txCadastrada", DoubleType.INSTANCE)
                    .addScalar("totalRegistros", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TaxaAdministrativaDTO.class));

            // Esse ajuste foi movido para a query para gerar corretamente a paginacao.
            //listaTaxaAdministrativa = ajustarListagem(query.getResultList(), filtro);
            listaTaxaAdministrativa = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo TaxaAdministrativaRepository.consultaPage " + ex.getMessage());
            throw ex;
        }
        return listaTaxaAdministrativa;
    }

    /**
     * Metodo responsavel por realizar a consulta conforme sql
     *
     * @param dto
     * @return
     */
    public List<TaxaAdministrativa> consultarCadastro(FiltroTaxasAdministrativasCadastroDTO dto) {

        List<TaxaAdministrativa> lstTaxaAdministrativa = new ArrayList<>();

        try {

            StringBuilder sqlQuery = new StringBuilder();

            sqlQuery.append(Funcoes
                    .getSqlString(this.getClass().getClassLoader()
                            .getResourceAsStream("br/com/accesstage/ascartoes/sql/TaxasAdministrativasCadastro.sql"))
                    .toString());

            if (dto.getEmpId() != null && dto.getEmpId() != 0) {
                sqlQuery.append(" AND p.empid  = ").append(dto.getEmpId());
            }

            if (dto.getCodLoja() != null && dto.getCodLoja() != 0) {
                sqlQuery.append(" AND l.cod_loja = ").append(dto.getCodLoja());
            }

            if (dto.getCodPontoVenda() != null && dto.getCodPontoVenda() != 0) {
                sqlQuery.append(" AND t.cod_ponto_venda = ").append(dto.getCodPontoVenda());
            }

            if (dto.getCodOperadora() != null && dto.getCodOperadora() != 0) {
                sqlQuery.append(" AND t.cod_operadora = ").append(dto.getCodOperadora());
            }

            if (dto.getCodProduto() != null && dto.getCodProduto() != 0) {
                sqlQuery.append(" AND t.cod_produto = ").append(dto.getCodProduto());
            }
            
            sqlQuery.append(" ORDER BY l.nme_loja asc, p.nro_terminal asc, o.nme_operadora asc, pr.nme_produto asc");
            
            Query query = em.createNativeQuery(sqlQuery.toString());

            query.unwrap(SQLQuery.class)
                    .addScalar("codTaxaAdministrativa", LongType.INSTANCE)
                    .addScalar("codEmp", LongType.INSTANCE)
                    .addScalar("codPontoVenda", LongType.INSTANCE)
                    .addScalar("codOperadora", LongType.INSTANCE)
                    .addScalar("codProduto", LongType.INSTANCE)
                    .addScalar("nroPlano", LongType.INSTANCE)
                    .addScalar("txAdmCadastrada", BigDecimalType.INSTANCE)
                    .addScalar("codUsuario", LongType.INSTANCE)
                    .addScalar("nmeLoja", StringType.INSTANCE)
                    .addScalar("nmePontoVenda", StringType.INSTANCE)
                    .addScalar("nmeOperadora", StringType.INSTANCE)
                    .addScalar("nome", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TaxaAdministrativa.class));

            lstTaxaAdministrativa = query.getResultList();

            for (TaxaAdministrativa taxa : lstTaxaAdministrativa) {

                taxa.setLoja(new PontoVendaCA());
                taxa.getLoja().setNmePontoVenda(taxa.getNmePontoVenda());

                taxa.setOperadora(new OperadoraCA());
                taxa.getOperadora().setNmeOperadora(taxa.getNmeOperadora());

                taxa.setProduto(new ProdutoCA());
                taxa.getProduto().setNome(taxa.getNome());
            }

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo TaxaAdministrativaRepository.consultarCadastro " + ex.getMessage(), ex);
            throw ex;
        }

        return lstTaxaAdministrativa;
    }

    private List<TaxaAdministrativaDTO> ajustarListagem(List<TaxaAdministrativaDTO> lista, FiltroTaxaAdministrativaDTO filtro) {

        //analisando o tipo das taxas
        for (TaxaAdministrativaDTO tp : lista) {
            String tipo = analiseTXConciliadaDivergente(tp);
            tp.setTipo(tipo);
        }

        // deixando os dados de acordo com o tipo informado pelo usuário. 
        List<TaxaAdministrativaDTO> listaWrite;
        listaWrite = new CopyOnWriteArrayList<>(lista);

        for (TaxaAdministrativaDTO tx : listaWrite) {
            if ("C".equalsIgnoreCase(filtro.getTipo()) && !"C".equalsIgnoreCase(tx.getTipo())) {
                listaWrite.remove(tx);
            }

            if ("D".equalsIgnoreCase(filtro.getTipo()) && "C".equalsIgnoreCase(tx.getTipo())) {
                listaWrite.remove(tx);
            }
        }

        return listaWrite;

    }

    private String analiseTXConciliadaDivergente(TaxaAdministrativaDTO taxa) {

        if (taxa.getTxCadastrada() == null) {
            return "";
        } else {
            if (Double.parseDouble(Format.arredondaDecimalString(Math.abs(taxa.getTaxa() - (100 * taxa.getTxCadastrada())), 2)) <= VARIACAO_TAXA) {
                return "C";
            } else {
                return "D";
            }
        }

    }

}
