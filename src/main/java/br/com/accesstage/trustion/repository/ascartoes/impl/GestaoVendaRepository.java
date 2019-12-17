package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import br.com.accesstage.trustion.repository.constantes.ConstantesDB;
import br.com.accesstage.trustion.util.Funcoes;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

/**
 *
 * @author raphael
 */
@Repository
public class GestaoVendaRepository {

    private static final int FIRST_PAGE = 1;
    private static final int PAGE_SIZE = 75;

    public static final int OPCAO_TODOS = 5;
    private static final int OPCAO_NAO_CONCILIADO = 3;

    private static final List<String> FLG_TODOS;
    private static final List<String> FLG_CONCILIADOS;

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    static {
        FLG_TODOS = new ArrayList<>(3);
        FLG_TODOS.add("0");
        FLG_TODOS.add("1");
        FLG_TODOS.add("2");

        FLG_CONCILIADOS = new ArrayList<>(2);
        FLG_CONCILIADOS.add("1");
        FLG_CONCILIADOS.add("2");
    }

    /**
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> buscarDivergenciaLoja(FiltroGestaoVendaDTO filtro) {
        Integer pagina = FIRST_PAGE;
        Integer pageSize = PAGE_SIZE;

        StringBuilder sb = new StringBuilder();
        sb.append("select CONS.* ");

        if (filtro.getPaginacaoDTO() != null) {
            pageSize = filtro.getPaginacaoDTO().getPageSize();
            pagina = filtro.getPaginacaoDTO().getFirst() + 1;

            sb.append(" , ceil(CONS.totalregistros / ").append(pageSize).append(" ) totalpaginas");
        }
        sb.append(" from (select count(1)     over() totalregistros ");
        sb.append("     , row_number() over(order by x.dta_venda, x.cod_nsu, x.dsc_autorizacao)   linha");
        sb.append("     , x.dta_venda                 as DATA");
        sb.append("     , to_char(to_date(x.dta_venda, 'yyyymmdd'), 'dd/mm/yyyy') as DSC_DATA_VENDA_FORMATADA ");
        sb.append("     , nvl(j.nme_loja, replace(p.nro_terminal, 'SLVLoja:', '' )) as LOJA");
        sb.append("     , o.nme_operadora             as BANDEIRA");
        sb.append("     , t.nme_produto               as PRODUTO");
        sb.append("     , x.nro_parcela               as PARCELA");
        sb.append("     , x.nro_plano                 as PLANO");
        sb.append("     , x.vlr_bruto                 as VALOR");
        sb.append("     , x.cod_nsu                   as NSU");
        sb.append("     , x.dsc_autorizacao           as AUTORIZACAO");
        sb.append("     , x.nro_caixa                 as CAIXA");
        sb.append("     , x.cod_detalhe               as ID_DETALHE");
        sb.append("     , t.cod_produto               as COD_PRODUTO");
        sb.append("     , o.cod_operadora             as COD_OPERADORA   ");
        sb.append("     , o.nme_operadora             as NME_OPERADORA   ");
        sb.append("     , x.dta_venda				  as DATA_VENDA");
        sb.append("     , x.flg_conciliacao			  as STATUS_CONCILIACAO");
        sb.append("     , x.hash_value				  as HASH_VALUE");
        sb.append("     , x.dsc_area_cliente		  as ID_CONCILIACAO");
        sb.append("     , x.cod_arquivo				  as COD_ARQUIVO");
        sb.append("     , p.cod_ponto_venda");
        sb.append("  from remessa_conciliacao_detalhe x");
        sb.append("     , adm_operadora               o");
        sb.append("     , adm_produto                 t");
        sb.append("     , adm_ponto_venda             p");
        sb.append("     , adm_loja                    j");

        sb.append(" where (1 = 1)");
        sb.append("   and (x.sta_conciliacao is null or x.sta_conciliacao='PENDENTE') ");

        sb.append("   and p.cod_loja                  = j.cod_loja(+)    ");
        sb.append("   and x.cod_ponto_venda           = p.cod_ponto_venda");

        sb.append("   and x.cod_operadora             = o.cod_operadora");
        sb.append("   and x.cod_produto               = t.cod_produto");
        sb.append("   and x.flg_exclusao              in (null, 0)");

        if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
            sb.append("   and x.dta_venda                 between ? and ?   ");
        } else {
            sb.append("   and x.cod_arquivo           = ");
            sb.append(filtro.getCodArquivo());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   and x.empid                     in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() != 0L) {
            sb.append("AND (j.cod_loja = ");
            sb.append(filtro.getCodLojaOuPv());
            sb.append(" OR x.cod_ponto_venda = ");
            sb.append(filtro.getCodLojaOuPv());
            sb.append(")");
        }

        sb.append("   and x.flg_exclusao              in (null, 0)");

        appendStatusConciliacao(sb, filtro);

        filtrosTelaConsultaLoja(filtro, sb);

        sb.append("       ) CONS");

        if (filtro.getPaginacaoDTO() != null) {
            sb.append("  where linha >= ((").append(pagina).append("  - 1) * ").append(pageSize).append(") + 1 ");
            sb.append("   and linha <= (").append(pagina).append(" * ").append(pageSize).append(") ");
        }

        LOGGER.info("Consulta Loja: " + sb.toString());

        List<GestaoVendasDTO> listaGestaoVenda = new ArrayList<>();

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {
            int status;

            if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));
            }
            pstm.setFetchSize(300);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    GestaoVendasDTO dto = new GestaoVendasDTO();
                    if (filtro.getPaginacaoDTO() != null) {
                        dto.setTotalPaginas(rs.getLong("totalpaginas"));
                    }
                    dto.setTotalRegistros(rs.getLong("totalregistros"));
                    dto.setId(rs.getLong("linha"));
                    try {
                        dto.setData(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(rs.getInt("DATA"))));
                    } catch (ParseException ex) {
                        LOGGER.error(ex.getMessage());
                    }

                    dto.setLoja(rs.getString("LOJA"));
                    dto.setCodPontoVenda(rs.getLong("COD_PONTO_VENDA"));
                    dto.setBandeira(rs.getString("BANDEIRA"));
                    dto.setProduto(rs.getString("PRODUTO"));
                    dto.setParcela(rs.getInt("PARCELA"));
                    dto.setPlano(rs.getString("PLANO"));
                    dto.setValor(rs.getBigDecimal("VALOR"));
                    dto.setNsu(rs.getString("NSU"));
                    dto.setCodAutorizacao(rs.getString("AUTORIZACAO"));
                    dto.setCaixa(rs.getString("CAIXA"));
                    dto.setIdDetalhe(rs.getLong("ID_DETALHE"));
                    dto.setCodProduto(rs.getLong("COD_PRODUTO"));
                    dto.setCodOperadora(rs.getLong("COD_OPERADORA"));
                    dto.setDscOperadora(rs.getString("NME_OPERADORA"));
                    dto.setDtaVendaDt(Funcoes.formataDataRelatorio(rs.getLong("DATA_VENDA")));
                    dto.setDataVenda(rs.getInt("DATA_VENDA"));
                    dto.setDataVendaFormatada(rs.getString("DSC_DATA_VENDA_FORMATADA"));
                    dto.setDscAreaCliente(rs.getString("ID_CONCILIACAO"));
                    dto.setHashValue(rs.getString("HASH_VALUE"));
                    dto.setCodArquivo(rs.getString("COD_ARQUIVO"));
                    dto.setListaEmpresas(filtro.getListaEmpresas());

                    status = rs.getInt("STATUS_CONCILIACAO");

                    if (status == 2) {
                        dto.setStatusConciliacao(1);
                    } else {
                        dto.setStatusConciliacao(status);
                    }

                    listaGestaoVenda.add(dto);
                }
                rs.close();
                pstm.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return listaGestaoVenda;
    }

    /**
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> buscarDivergenciaLojaSemaforo(FiltroGestaoVendaDTO filtro) {
        StringBuilder sb = new StringBuilder();

        sb.append(" 	select count(1)     over() totalregistros ");
        sb.append("     , row_number() over(order by x.dta_venda, x.cod_nsu, x.dsc_autorizacao)   linha");
        sb.append("     , x.dta_venda                 as DATA");
        sb.append("     , nvl(j.nme_loja, replace(p.nro_terminal, 'SLVLoja:', '' )) as LOJA");
        sb.append("     , o.nme_operadora             as BANDEIRA");
        sb.append("     , t.nme_produto               as PRODUTO");
        sb.append("     , x.nro_parcela               as PARCELA");
        sb.append("     , x.nro_plano                 as PLANO");
        sb.append("     , x.vlr_bruto                 as VALOR");
        sb.append("     , x.cod_nsu                   as NSU");
        sb.append("     , x.dsc_autorizacao           as AUTORIZACAO");
        sb.append("     , x.nro_caixa                 as CAIXA");
        sb.append("     , x.cod_detalhe               as ID_DETALHE");
        sb.append("     , t.cod_produto               as COD_PRODUTO");
        sb.append("     , o.cod_operadora             as COD_OPERADORA   ");
        sb.append("     , o.nme_operadora             as NME_OPERADORA   ");
        sb.append("     , x.dta_venda				  as DATA_VENDA");
        sb.append("     , x.flg_conciliacao			  as STATUS_CONCILIACAO");
        sb.append("     , x.dsc_area_cliente		  as ID_CONCILIACAO");
        sb.append("     , p.cod_ponto_venda");
        sb.append("  from remessa_conciliacao_detalhe x");
        sb.append("     , adm_operadora               o");
        sb.append("     , adm_produto                 t");
        sb.append("     , adm_ponto_venda             p");
        sb.append("     , adm_loja                    j");

        sb.append(" where (1 = 1)");
        sb.append("   and (x.sta_conciliacao is null or x.sta_conciliacao='PENDENTE') ");

        sb.append("   and p.cod_loja                  = j.cod_loja(+)    ");
        sb.append("   and x.cod_ponto_venda           = p.cod_ponto_venda");

        sb.append("   and x.cod_operadora             = o.cod_operadora");
        sb.append("   and x.cod_produto               = t.cod_produto");
        sb.append("   and x.flg_exclusao              in (null, 0)");

        if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
            sb.append("   and x.dta_venda                 between ? and ?   ");
        } else {
            sb.append("   and x.cod_arquivo           = ");
            sb.append(filtro.getCodArquivo());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   and x.empid                     in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() != 0L) {
            sb.append("AND (j.cod_loja = ");
            sb.append(filtro.getCodLojaOuPv());
            sb.append(" OR x.cod_ponto_venda = ");
            sb.append(filtro.getCodLojaOuPv());
            sb.append(")");
        }

        sb.append("   and x.flg_exclusao              in (null, 0)");

        appendStatusConciliacao(sb, filtro);

        filtrosTelaConsultaLoja(filtro, sb);

        LOGGER.info("Atualizando Semáforo: " + sb.toString());

        List<GestaoVendasDTO> listaGestaoVenda = new ArrayList<>();

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {
            int status;

            if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));
            }

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    GestaoVendasDTO dto = new GestaoVendasDTO();

                    dto.setValor(rs.getBigDecimal("VALOR"));
                    status = rs.getInt("STATUS_CONCILIACAO");

                    if (status == 2) {
                        dto.setStatusConciliacao(1);
                    } else {
                        dto.setStatusConciliacao(status);
                    }

                    listaGestaoVenda.add(dto);
                }
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }

        return listaGestaoVenda;
    }

    /**
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> buscarDivergenciaLojaSemaforoCIAS(FiltroGestaoVendaDTO filtro) {
        StringBuilder sb = new StringBuilder();

        sb.append(" 	select count(1)     over() totalregistros ");
        sb.append("     , x.vlr_bruto                 as VALOR");
        sb.append("     , x.flg_conciliacao			  as STATUS_CONCILIACAO");
        sb.append("     , x.dsc_area_cliente		  as ID_CONCILIACAO");
        sb.append("  from remessa_conciliacao_detalhe x");
        sb.append("     , adm_produto                 t");

        sb.append(" where (1 = 1)");
        sb.append("   and (x.sta_conciliacao is null or x.sta_conciliacao='PENDENTE') ");
        sb.append("   and x.cod_produto               = t.cod_produto");
        sb.append("   and x.flg_exclusao              in (null, 0)");

        if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
            sb.append("   and x.dta_venda                 between ? and ?   ");
        } else {
            sb.append("   and x.cod_arquivo           = ");
            sb.append(filtro.getCodArquivo());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   and x.empid                     in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        sb.append("   and x.flg_exclusao              in (null, 0)");

        appendStatusConciliacao(sb, filtro);

        filtrosTelaConsultaLoja(filtro, sb);

        LOGGER.info("Consulta buscarDivergenciaLojaSemaforoCIAS: " + sb.toString());

        List<GestaoVendasDTO> listaGestaoVenda = new ArrayList<>();

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {
            int status;

            if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));
            }

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    GestaoVendasDTO dto = new GestaoVendasDTO();

                    dto.setValor(rs.getBigDecimal("VALOR"));
                    status = rs.getInt("STATUS_CONCILIACAO");

                    if (status == 2) {
                        dto.setStatusConciliacao(1);
                    } else {
                        dto.setStatusConciliacao(status);
                    }

                    listaGestaoVenda.add(dto);
                }
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return listaGestaoVenda;
    }

    /**
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> buscarDivergenciaLojaCiasAereas(FiltroGestaoVendaDTO filtro) {
        Integer pagina = FIRST_PAGE;
        Integer pageSize = PAGE_SIZE;

        StringBuilder sb = new StringBuilder();
        sb.append("select CONS.* ");

        if (filtro.getPaginacaoDTO() != null) {
            pageSize = filtro.getPaginacaoDTO().getPageSize();
            pagina = filtro.getPaginacaoDTO().getFirst() + 1;

            sb.append(" , ceil(CONS.totalregistros / ").append(pageSize).append(" ) totalpaginas");
        }
        sb.append(" from (select count(1)     over() totalregistros ");
        sb.append("     , row_number() over(order by x.dta_venda, x.cod_nsu, x.dsc_autorizacao)   linha");
        sb.append("     , x.dta_venda                 as DATA");
        sb.append("     , to_char(to_date(x.dta_venda, 'yyyymmdd'), 'dd/mm/yyyy') as DSC_DATA_VENDA_FORMATADA ");
        sb.append("     , t.nme_produto               as PRODUTO");
        sb.append("     , x.nro_parcela               as PARCELA");
        sb.append("     , x.nro_plano                 as PLANO");
        sb.append("     , x.vlr_bruto                 as VALOR");
        sb.append("     , x.cod_nsu                   as NSU");
        sb.append("     , x.dsc_autorizacao           as AUTORIZACAO");
        sb.append("     , x.nro_caixa                 as CAIXA");
        sb.append("     , x.cod_detalhe               as ID_DETALHE");
        sb.append("     , t.cod_produto               as COD_PRODUTO");
        sb.append("     , x.dta_venda				  as DATA_VENDA");
        sb.append("     , x.flg_conciliacao			  as STATUS_CONCILIACAO");
        sb.append("     , x.hash_value				  as HASH_VALUE");
        sb.append("     , x.dsc_area_cliente		  as ID_CONCILIACAO");
        sb.append("     , x.cod_arquivo				  as COD_ARQUIVO");
        sb.append("  from remessa_conciliacao_detalhe x");
        sb.append("     , adm_produto                 t");
        sb.append(" where (1 = 1)");
        sb.append("   and (x.sta_conciliacao is null or x.sta_conciliacao='PENDENTE') ");
        sb.append("   and x.cod_produto               = t.cod_produto");
        sb.append("   and x.flg_exclusao              in (null, 0)");

        if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
            sb.append("   and x.dta_venda                 between ? and ?   ");
        } else {
            sb.append("   and x.cod_arquivo           = ");
            sb.append(filtro.getCodArquivo());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   and x.empid                     in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        sb.append("   and x.flg_exclusao              in (null, 0)");

        appendStatusConciliacao(sb, filtro);

        filtrosTelaConsultaLoja(filtro, sb);

        sb.append("       ) CONS");

        if (filtro.getPaginacaoDTO() != null) {
            sb.append("  where linha >= ((").append(pagina).append("  - 1) * ").append(pageSize).append(") + 1 ");
            sb.append("   and linha <= (").append(pagina).append(" * ").append(pageSize).append(") ");
        }

        LOGGER.info("Consulta Loja CIA: " + sb.toString());

        List<GestaoVendasDTO> listaGestaoVenda = new ArrayList<>();

        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {
            int status;

            if (Funcoes.isNullOrZero(filtro.getCodArquivo())) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));
            }

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    GestaoVendasDTO dto = new GestaoVendasDTO();
                    if (filtro.getPaginacaoDTO() != null) {
                        dto.setTotalPaginas(rs.getLong("totalpaginas"));
                    }
                    dto.setTotalRegistros(rs.getLong("totalregistros"));
                    dto.setId(rs.getLong("linha"));
                    try {
                        dto.setData(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(rs.getInt("DATA"))));
                    } catch (ParseException ex) {
                        LOGGER.error(ex.getMessage());
                    }

                    dto.setProduto(rs.getString("PRODUTO"));
                    dto.setParcela(rs.getInt("PARCELA"));
                    dto.setPlano(rs.getString("PLANO"));
                    dto.setValor(rs.getBigDecimal("VALOR"));
                    dto.setNsu(rs.getString("NSU"));
                    dto.setCodAutorizacao(rs.getString("AUTORIZACAO"));
                    dto.setCaixa(rs.getString("CAIXA"));
                    dto.setIdDetalhe(rs.getLong("ID_DETALHE"));
                    dto.setCodProduto(rs.getLong("COD_PRODUTO"));
                    dto.setDtaVendaDt(Funcoes.formataDataRelatorio(rs.getLong("DATA_VENDA")));
                    dto.setDataVenda(rs.getInt("DATA_VENDA"));
                    dto.setDataVendaFormatada(rs.getString("DSC_DATA_VENDA_FORMATADA"));
                    dto.setDscAreaCliente(rs.getString("ID_CONCILIACAO"));
                    dto.setHashValue(rs.getString("HASH_VALUE"));
                    dto.setCodArquivo(rs.getString("COD_ARQUIVO"));
                    dto.setListaEmpresas(filtro.getListaEmpresas());

                    status = rs.getInt("STATUS_CONCILIACAO");

                    if (status == 2) {
                        dto.setStatusConciliacao(1);
                    } else {
                        dto.setStatusConciliacao(status);
                    }

                    listaGestaoVenda.add(dto);
                }
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return listaGestaoVenda;
    }

    /**
     * Consulta bandeira com paginação.
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeiraPaginada(FiltroGestaoVendaDTO filtro) {
        Integer pagina = FIRST_PAGE;
        if (filtro.getPaginacaoDTO() != null) {
            pagina = filtro.getPaginacaoDTO().getFirst() + 1;
        }
        if (!Funcoes.isNullOrZero(filtro.getCodLojaOuPv())) {
            return consultaBandeiraPaginadaLoja(filtro, pagina.toString());
        } else {
            return consultaBandeiraPaginada(filtro, pagina.toString());
        }
    }

    /**
     * Consulta bandeira de loja com paginação.
     *
     * @param filtro
     * @param numeroPagina
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeiraPaginadaLoja(FiltroGestaoVendaDTO filtro, String numeroPagina) {
        List<GestaoVendasDTO> result = new ArrayList<>();
        int status;

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraLojaPaginado.sql");

            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));
            if (numeroPagina != null) {
                query = query.replaceAll(":pPagina", numeroPagina);
            } else {
                query = query.replaceAll(":pPagina", "1");
            }

            if (filtro.getCodLojaOuPv() != null && filtro.getCodLojaOuPv() != -1L) {
                query = query.replace(":pvOuLoja", " AND (j.cod_loja = " + filtro.getCodLojaOuPv() + " OR f.cod_ponto_venda = " + filtro.getCodLojaOuPv()
                        + ")");
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            query = replaceFlgConciliacao(query, filtro.getStatusConciliacao());

            query = query.replace(":filtroConsulta", filtrosTelaConsultaOperadora(filtro));

            LOGGER.info("Consulta consultaBandeiraPaginada com LOJA: " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstm.executeQuery()) {

                    while (rs.next()) {
                        GestaoVendasDTO dto = new GestaoVendasDTO();
                        dto.setTotalPaginas(rs.getLong("totalpaginas"));
                        dto.setTotalRegistros(rs.getLong("totalregistros"));
                        dto.setId(rs.getLong("linha"));
                        dto.setData(Funcoes.formataDataRelatorio(rs.getLong("DATA")));
                        dto.setDtaVendaDt(Funcoes.formataDataRelatorio(rs.getLong("DATA")));
                        dto.setDataVendaFormatada(rs.getString("DSC_DATA_VENDA_FORMATADA"));
                        dto.setLoja(rs.getString("LOJA"));
                        dto.setBandeira(rs.getString("BANDEIRA"));
                        dto.setProduto(rs.getString("PRODUTO"));
                        dto.setPlano(rs.getString("PLANO"));
                        dto.setValor(Funcoes.dividePorCem(rs.getBigDecimal("VALOR")));
                        dto.setNsu(rs.getString("NSU"));
                        dto.setCodAutorizacao(rs.getString("AUTORIZACAO"));
                        dto.setCaptura(rs.getString("CAPTURA"));
                        dto.setCodPontoVenda(rs.getLong("COD_PONTO_VENDA"));
                        dto.setDscOperadora(rs.getString("OPERADORA"));
                        dto.setCodOperadora(rs.getLong("COD_OPERADORA"));
                        dto.setListaEmpresas(filtro.getListaEmpresas());

                        status = rs.getInt("STATUS_CONCILIACAO");

                        if (status == 0) {
                            dto.setStatusConciliacao(3);
                        } else {
                            dto.setStatusConciliacao(status);
                        }

                        result.add(dto);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return result;
    }

    /**
     * Consulta bandeira com paginação a partir do número da página.
     *
     * @param filtro
     * @param numeroPagina
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeiraPaginada(FiltroGestaoVendaDTO filtro, String numeroPagina) {
        List<GestaoVendasDTO> result = new ArrayList<>();

        int status;

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraPaginado.sql");

            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));
            if (numeroPagina != null) {
                query = query.replaceAll(":pPagina", numeroPagina);
            } else {
                query = query.replaceAll(":pPagina", "1");
            }

            query = replaceFlgConciliacao(query, filtro.getStatusConciliacao());

            query = query.replace(":filtroConsulta", filtrosTelaConsultaOperadora(filtro));

            LOGGER.info("Consulta consultaBandeiraPaginada: " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstm.executeQuery()) {

                    while (rs.next()) {
                        GestaoVendasDTO dto = new GestaoVendasDTO();
                        dto.setTotalPaginas(rs.getLong("totalpaginas"));
                        dto.setTotalRegistros(rs.getLong("totalregistros"));
                        dto.setId(rs.getLong("linha"));
                        dto.setData(Funcoes.formataDataRelatorio(rs.getLong("DATA")));
                        dto.setDtaVendaDt(Funcoes.formataDataRelatorio(rs.getLong("DATA")));
                        dto.setDataVenda(rs.getInt("DATA"));
                        dto.setDataVendaFormatada(rs.getString("DSC_DATA_VENDA_FORMATADA"));
                        dto.setCodOperadora(rs.getLong("COD_OPERADORA"));
                        dto.setDscOperadora(rs.getString("OPERADORA"));
                        dto.setStatusConciliacao(rs.getInt("STATUS"));
                        dto.setLoja(rs.getString("LOJA"));
                        dto.setBandeira(rs.getString("BANDEIRA"));
                        dto.setProduto(rs.getString("PRODUTO"));
                        dto.setPlano(rs.getString("PLANO"));
                        dto.setValor(Funcoes.dividePorCem(rs.getBigDecimal("VALOR")));
                        dto.setNsu(rs.getString("NSU"));
                        dto.setCodAutorizacao(rs.getString("AUTORIZACAO"));
                        dto.setCaptura(rs.getString("CAPTURA"));
                        dto.setCodPontoVenda(rs.getLong("cod_ponto_venda"));
                        dto.setDscAreaCliente(rs.getString("ID_BILHETE"));
                        dto.setListaEmpresas(filtro.getListaEmpresas());

                        status = rs.getInt("STATUS_CONCILIACAO");

                        if (status == 0) {
                            dto.setStatusConciliacao(3);
                        } else {
                            dto.setStatusConciliacao(status);
                        }

                        result.add(dto);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return result;
    }

    /**
     * Consulta bandeira.
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeira(FiltroGestaoVendaDTO filtro) {
        if (filtro.getCodLojaOuPv() != null) {
            return consultaBandeiraCodLoja(filtro);
        } else {
            return consultaBandeiraSemCodLoja(filtro);
        }
    }

    /**
     * Atualiza o semáforo.
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> atualizaSemaforoConsultaOperadora(FiltroGestaoVendaDTO filtro) {
        List<GestaoVendasDTO> result = new ArrayList<>();
        int status;

        try {
            String query;

            if (Funcoes.isNullOrZero(filtro.getCodLojaOuPv())) {
                query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraPaginadoSemaforo.sql");
            } else {
                query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraPaginadoSemaforoLoja.sql");
            }

            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (!Funcoes.isNullOrZero(filtro.getCodLojaOuPv())) {
                query = query.replace(":pvOuLoja", " AND (j.cod_loja = " + filtro.getCodLojaOuPv() + " OR f.cod_ponto_venda = " + filtro.getCodLojaOuPv()
                        + ")");
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            query = replaceFlgConciliacao(query, filtro.getStatusConciliacao());

            if (Funcoes.isNullOrZero(filtro.getCodLojaOuPv())) {
                LOGGER.info("DivergenciasBandeiraPaginadoSemaforo.sql: " + query);
            } else {
                LOGGER.info("DivergenciasBandeiraPaginadoSemaforoLoja.sql: " + query);
            }

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet rs = pstm.executeQuery()) {

                    while (rs.next()) {
                        GestaoVendasDTO dto = new GestaoVendasDTO();

                        dto.setValor(Funcoes.dividePorCem(rs.getBigDecimal("VALOR")));
                        status = rs.getInt("STATUS_CONCILIACAO");

                        if (status == 0) {
                            dto.setStatusConciliacao(3);
                        } else {
                            dto.setStatusConciliacao(status);
                        }

                        result.add(dto);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return result;
    }

    /**
     * Carrega as informações do painel do semáforo.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<SemaforoDTO> carregarSemaforoZerado() {

        List<SemaforoDTO> listaSemaforo = new ArrayList<>();
        int i = 2;
        int contador = 0;

        for (int j = 0; j <= i; j++) {
            SemaforoDTO dto = new SemaforoDTO();

            int param;

            switch (contador) {
                case 0:
                    param = 1;
                    break;
                case 1:
                    param = 0;
                    break;
                default:
                    param = contador;
                    break;
            }

            dto.setQuantidade(0);
            dto.setStatus(param);
            dto.setValor(BigDecimal.ZERO);

            listaSemaforo.add(dto);

            contador++;
        }
        return listaSemaforo;
    }

    /**
     * Consulta bandeira usando o código de loja.
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeiraCodLoja(FiltroGestaoVendaDTO filtro) {
        List<GestaoVendasDTO> result = new ArrayList<>();
        int status;

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraPaginadoSemaforoLoja.sql");

            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            if (!Funcoes.isNullOrZero(filtro.getCodLojaOuPv())) {
                query = query.replace(":pvOuLoja", " AND (j.cod_loja = " + filtro.getCodLojaOuPv() + " OR f.cod_ponto_venda = " + filtro.getCodLojaOuPv() + ")");
            } else {
                query = query.replace(":pvOuLoja", "");
            }

            query = replaceFlgConciliacao(query, filtro.getStatusConciliacao());

            LOGGER.info("Consulta DivergenciasBandeiraPaginadoSemaforoLoja.sql: " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet resultSet = pstm.executeQuery()) {

                    while (resultSet.next()) {
                        GestaoVendasDTO dto = new GestaoVendasDTO();
                        dto.setData(Funcoes.formataDataRelatorio(resultSet.getLong("DATA")));
                        dto.setDataVendaLong(resultSet.getLong("DATA"));
                        dto.setLoja(resultSet.getString("LOJA"));
                        dto.setBandeira(resultSet.getString("BANDEIRA"));
                        dto.setProduto(resultSet.getString("PRODUTO"));
                        dto.setPlano(resultSet.getString("PLANO"));
                        dto.setValor(Funcoes.dividePorCem(resultSet.getBigDecimal("VALOR")));
                        dto.setNsu(resultSet.getString("NSU"));
                        dto.setCodAutorizacao(resultSet.getString("AUTORIZACAO"));
                        dto.setCaptura(resultSet.getString("CAPTURA"));
                        dto.setDscOperadora(resultSet.getString("OPERADORA"));
                        dto.setDscAreaCliente(resultSet.getString("ID_BILHETE"));

                        status = resultSet.getInt("STATUS");

                        if (status == 0) {
                            dto.setStatusConciliacao(3);
                        } else {
                            dto.setStatusConciliacao(status);
                        }
                        result.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * Consulta bandeira sem usar o código de loja.
     *
     * @param filtro
     * @return
     */
    @Transactional(readOnly = true)
    public List<GestaoVendasDTO> consultaBandeiraSemCodLoja(FiltroGestaoVendaDTO filtro) {
        List<GestaoVendasDTO> result = new ArrayList<>();
        int status;

        try {
            String query = Funcoes.getSqlString(ConstantesDB.CONTEXTO_SQL_DIVERGENCIA, "DivergenciasBandeiraPaginadoSemaforo.sql");

            query = query.replace(":idsEmp", Funcoes.formataArray(filtro.getListaEmpresas()));

            query = replaceFlgConciliacao(query, filtro.getStatusConciliacao());

            LOGGER.info("Consulta DivergenciasBandeiraPaginadoSemaforo.sql: " + query);

            try (Connection conn = em.unwrap(SessionImpl.class).connection();
                    PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setLong(1, Funcoes.formataDataRelatorio(filtro.getDataInicial()));
                pstm.setLong(2, Funcoes.formataDataRelatorio(filtro.getDataFinal()));

                try (ResultSet resultSet = pstm.executeQuery()) {
                    while (resultSet.next()) {
                        GestaoVendasDTO dto = new GestaoVendasDTO();
                        dto.setData(Funcoes.formataDataRelatorio(resultSet.getLong("DATA")));
                        dto.setDataVendaLong(resultSet.getLong("DATA"));
                        dto.setLoja(resultSet.getString("LOJA"));
                        dto.setBandeira(resultSet.getString("BANDEIRA"));
                        dto.setProduto(resultSet.getString("PRODUTO"));
                        dto.setPlano(resultSet.getString("PLANO"));
                        dto.setValor(Funcoes.dividePorCem(resultSet.getBigDecimal("VALOR")));
                        dto.setNsu(resultSet.getString("NSU"));
                        dto.setCodAutorizacao(resultSet.getString("AUTORIZACAO"));
                        dto.setCaptura(resultSet.getString("CAPTURA"));
                        dto.setDscOperadora(resultSet.getString("OPERADORA"));
                        dto.setDscAreaCliente(resultSet.getString("ID_BILHETE"));

                        status = resultSet.getInt("STATUS");

                        if (status == 0) {
                            dto.setStatusConciliacao(3);
                        } else {
                            dto.setStatusConciliacao(status);
                        }
                        result.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * Adiciona uma cláusula AND para cada filtro da grid.
     *
     * @param filtro
     * @param sb
     */
    @Transactional(readOnly = true)
    public void filtrosTelaConsultaLoja(FiltroGestaoVendaDTO filtro, StringBuilder sb) {
        clausulaFiltrosTelaConsultaLoja(sb, "o.nme_operadora", filtro.getFiltroOperadora());
        clausulaFiltrosTelaConsultaLoja(sb, "t.nme_produto", filtro.getFiltroProduto());
        clausulaFiltrosTelaConsultaLoja(sb, "j.nme_loja", filtro.getFiltroLoja());
        clausulaFiltrosTelaConsultaLoja(sb, "x.vlr_bruto", filtro.getFiltroValor());
        clausulaFiltrosTelaConsultaLoja(sb, "x.cod_nsu", filtro.getFiltroNsu());
        clausulaFiltrosTelaConsultaLoja(sb, "x.dsc_autorizacao", filtro.getFiltroAutorizacao());
        clausulaFiltrosTelaConsultaLoja(sb, "x.dsc_area_cliente", filtro.getFiltroAreaCliente());
        clausulaFiltrosTelaConsultaLoja(sb, "x.nro_plano", filtro.getFiltroPlano());
    }

    /**
     * Método conveniente usado pelo #filtrosTelaConsultaLoja.
     *
     * @param sb
     * @param campo
     * @param valor
     * @param andClause
     */
    private void clausulaFiltrosTelaConsultaLoja(StringBuilder sb, String campo, Object valor) {
        if (valor != null) {
            if (valor instanceof String && StringUtils.isBlank(valor.toString())) {
                return;
            }

            sb.append(" AND ");

            if (campo.equals("j.nme_loja")) {
                sb.append(" ( ");
            }
            if (campo.contains("vlr_")) {
                valor = valor.toString().replace(".", ",");
            }

            sb.append("UPPER(").append(campo).append(") LIKE  '%' ||");
            sb.append("UPPER('").append(valor).append("') || '%'");

            if (campo.equals("j.nme_loja")) {
                sb.append(" OR ");
                sb.append(" p.nro_terminal LIKE '%' " + "||");
                sb.append("UPPER('").append(valor).append("') || '%')");
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private String filtrosTelaConsultaOperadora(FiltroGestaoVendaDTO filtro) {
        String tabela;
        String campo;
        String valor;

        if (StringUtils.isNotBlank(filtro.getFiltroOperadora())) {
            tabela = "o.";
            campo = "nme_operadora";
            valor = filtro.getFiltroOperadora();
        } else if (StringUtils.isNotBlank(filtro.getFiltroProduto())) {
            tabela = "t.";
            campo = "nme_produto";
            valor = filtro.getFiltroProduto();
        } else if (StringUtils.isNotBlank(filtro.getFiltroLoja())) {
            tabela = "j.";
            campo = "nme_loja";
            valor = filtro.getFiltroLoja();
        } else if (StringUtils.isNotBlank(filtro.getFiltroValor())) {
            tabela = "f.";
            campo = "vlr_bruto";
            valor = filtro.getFiltroValor().replace(".", "");
        } else if (StringUtils.isNotBlank(filtro.getFiltroNsu())) {
            tabela = "f.";
            campo = "nro_nsu";
            valor = filtro.getFiltroNsu();
        } else if (StringUtils.isNotBlank(filtro.getFiltroAutorizacao())) {
            tabela = "f.";
            campo = "cod_autorizacao";
            valor = filtro.getFiltroAutorizacao();
        } else if (StringUtils.isNotBlank(filtro.getFiltroAreaCliente())) {
            tabela = "f.";
            campo = "dsc_area_cliente";
            valor = filtro.getFiltroAreaCliente();
        } else if (filtro.getFiltroPlano() != null) {
            tabela = "f.";
            campo = "nro_plano";
            valor = filtro.getFiltroPlano();
        } else {
            return " AND 1 = 1";
        }

        return " AND "
                + "UPPER(" + tabela + campo + ")" + " LIKE  '%' " + "||"
                + "UPPER('" + valor + "') || " + "'%'";
    }

    /**
     * Concatena a cláusula do status de conciliação à query.
     *
     * @param sb
     * @param filtro
     */
    private void appendStatusConciliacao(StringBuilder sb, FiltroGestaoVendaDTO filtro) {
        if (filtro.getStatusConciliacao() == OPCAO_TODOS) {
            sb.append("   and x.flg_conciliacao          in (0,1,2,3) ");

        } else {
            if (filtro.getStatusConciliacao() == 1) {
                sb.append("   and x.flg_conciliacao in (1,2) ");
            } else {
                sb.append("   and x.flg_conciliacao = ");
                sb.append(filtro.getStatusConciliacao());
            }
        }
    }

    /**
     * Parametriza, na query, o status de conciliação.
     *
     * @param sql
     * @param flagConciliacao
     * @return
     */
    private String replaceFlgConciliacao(String sql, Integer flagConciliacao) {
        switch (flagConciliacao) {
            case OPCAO_TODOS:
                sql = sql.replaceAll(":flgConciliacao", Funcoes.formataArray(FLG_TODOS));
                break;
            case OPCAO_NAO_CONCILIADO:
                sql = sql.replaceAll(":flgConciliacao", "0");
                break;
            default:
                sql = sql.replaceAll(":flgConciliacao", Funcoes.formataArray(FLG_CONCILIADOS));
        }
        return sql;
    }
}
