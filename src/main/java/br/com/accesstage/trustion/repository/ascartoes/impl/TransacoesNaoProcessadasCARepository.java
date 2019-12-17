package br.com.accesstage.trustion.repository.ascartoes.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.util.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static br.com.accesstage.trustion.util.Utils.isNotEmpty;

/**
 *
 * @author raphael
 */
@Repository
public class TransacoesNaoProcessadasCARepository {

    @Log
    private static Logger LOGGER;

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadas(
            FiltroTransacoesNaoProcessadasDTO filtro) {

        Integer pagina = 0;
        Integer pageSize = 0;

        if (filtro.getPaginacaoDTO() != null) {
            pageSize = filtro.getPaginacaoDTO().getPageSize();
            float round = (filtro.getPaginacaoDTO().getFirst() / filtro.getPaginacaoDTO().getPageSize()) + 1;
            pagina = Math.round(round);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select CONS.* ");

        if (filtro.getPaginacaoDTO() != null) {
            sb.append(" , ceil(CONS.totalregistros / ").append(pageSize).append(" ) totalpaginas");
        }
        sb.append(" from (select count(1)     over() totalregistros ");
        sb.append(" , row_number() over(order by ro.dta_apresentacao, ro.nro_resumo_operacao)   linha");
        sb.append(" , ro.nro_unico_ro ");
        sb.append(" , ro.nro_resumo_operacao ");
        sb.append(" , ro.cod_estab_submissor ");
        sb.append(" , to_char(to_date(ro.dta_apresentacao, 'dd/mm/yy'), 'dd/mm/yyyy') AS DSC_DATA_VENDA_FORMATADA");
        sb.append(" , to_char(to_date(ro.dta_prevista_pgto, 'dd/mm/yy'), 'dd/mm/yyyy') as DSC_DATA_PGTO_FORMATADA");

        sb.append(" , ro.vlr_liquido");
        sb.append(" , ro.qtdo_cvs_aceitos ");
        sb.append(" FROM historico_arquivos_processados HIS, CLOM_RO RO , CLOM_HEADER H ");
        sb.append(" WHERE RO.COD_HEADER = his.cod_header ");
        sb.append(" AND h.cod_arquivo = his.cod_arquivo ");

        if (filtro.getOpcaoExtrato() != null) {
            sb.append(" AND h.cod_opcao_extrato = ");
            sb.append(filtro.getOpcaoExtrato());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   AND his.empid  in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        if (filtro.getDataInicial() != null) {
            sb.append(" AND TRUNC(his.data_inicio) >= ?");
        }

        if (filtro.getDataFinal() != null) {
            sb.append(" AND TRUNC(his.data_fim) <= ?");
        }
        sb.append(" AND ro.status_processamento IS NULL ");

        sb.append("       ) CONS");

        // não tem paginacao essa busca é usada para o exportar
        if (filtro.getPaginacaoDTO() != null) {
            sb.append("  where linha >= ((").append(pagina).append("  - 1) * ").append(pageSize).append(") + 1 ");
            sb.append("   and linha <= (").append(pagina).append(" * ").append(pageSize).append(") ");
        }

        LOGGER.info("Consulta transações não processadas : " + sb.toString());

        List<TransacoesNaoProcessadasDTO> lista = new ArrayList<>();
        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

            pstm.setDate(1, new java.sql.Date(filtro.getDataInicial().getTime()));
            pstm.setDate(2, new java.sql.Date(filtro.getDataFinal().getTime()));

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    TransacoesNaoProcessadasDTO dto = new TransacoesNaoProcessadasDTO();
                    dto.setTotalPaginas(rs.getLong("totalregistros"));
                    dto.setId(rs.getLong("linha"));
                    dto.setCodDataCredito(rs.getString("DSC_DATA_PGTO_FORMATADA"));
                    dto.setCodDataVenda(rs.getString("DSC_DATA_VENDA_FORMATADA"));
                    dto.setCodigoLote(rs.getString("nro_resumo_operacao"));
                    dto.setNumeroUnico(rs.getString("nro_unico_ro"));
                    dto.setQtdCvsAceitos(rs.getString("qtdo_cvs_aceitos"));
                    dto.setValorLiquido(rs.getBigDecimal("vlr_liquido"));
                    dto.setCodigoEstabelecimentoSubmissor(rs.getLong("cod_estab_submissor"));

                    lista.add(dto);
                }
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }

        return lista;
    }

    @Transactional(readOnly = true)
    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadasAntecipacao(
            FiltroTransacoesNaoProcessadasDTO filtro) {

        Integer pagina = 0;
        Integer pageSize = 0;

        if (filtro.getPaginacaoDTO() != null) {
            pageSize = filtro.getPaginacaoDTO().getPageSize();
            float round = (filtro.getPaginacaoDTO().getFirst() / filtro.getPaginacaoDTO().getPageSize()) + 1;
            pagina = Math.round(round);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select CONS.* ");

        if (filtro.getPaginacaoDTO() != null) {
            sb.append(" , ceil(CONS.totalregistros / ").append(pageSize).append(" ) totalpaginas");
        }
        sb.append(" from (select count(1)     over() totalregistros ");
        sb.append(" , row_number() over(order by ro.dta_credito_oper, roa.nro_antecipacao)   linha");
        sb.append(" , to_char(to_date(ro.dta_credito_oper, 'dd/mm/yy'), 'dd/mm/yyyy') as DATA_CREDITO");
        sb.append(" , roa.nro_unico ");
        sb.append(" , roa.vlr_liquido ");
        sb.append(" , roa.vlr_liquido_antec ");
        sb.append(" , roa.parcela_antecipada");
        sb.append(" , roa.nro_antecipacao ");
        sb.append(" , roa.cod_estabelecimento");
        sb.append("  FROM   HISTORICO_ARQUIVOS_PROCESSADOS his ");
        sb.append(" , CLOM_ANTECIPACAO RO");
        sb.append(" , CLOM_HEADER H");
        sb.append(" , CLOM_RO_ANTECIPACAO ROA ");
        sb.append(" WHERE RO.COD_HEADER = his.cod_header ");
        sb.append(" AND h.cod_arquivo  = his.cod_arquivo ");
        sb.append(" AND h.cod_arquivo = his.cod_arquivo ");
        sb.append(" AND roa.cod_antecipacao = ro.cod_antecipacao ");
        sb.append(" AND roa.status_processamento is null ");

        if (filtro.getOpcaoExtrato() != null) {
            sb.append(" AND h.cod_opcao_extrato = ");
            sb.append(filtro.getOpcaoExtrato());
        }

        if (isNotEmpty(filtro.getListaEmpresas())) {
            sb.append("   AND his.empid  in ( ");
            sb.append(Funcoes.formataArray(filtro.getListaEmpresas()));
            sb.append(" ) ");
        }

        if (filtro.getDataInicial() != null) {
            sb.append(" AND TRUNC(his.data_inicio) >= ?");
        }

        if (filtro.getDataFinal() != null) {
            sb.append(" AND TRUNC(his.data_fim) <= ?");
        }

        sb.append("       ) CONS");

        // não tem paginacao essa busca é usada para o exportar
        if (filtro.getPaginacaoDTO() != null) {
            sb.append("  where linha >= ((").append(pagina).append("  - 1) * ").append(pageSize).append(") + 1 ");
            sb.append("   and linha <= (").append(pagina).append(" * ").append(pageSize).append(") ");
        }

        LOGGER.info("Consulta transações não processadas Antecipação: " + sb.toString());

        List<TransacoesNaoProcessadasDTO> lista = new ArrayList<>();
        try (Connection conn = em.unwrap(SessionImpl.class).connection();
                PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

            pstm.setDate(1, new java.sql.Date(filtro.getDataInicial().getTime()));
            pstm.setDate(2, new java.sql.Date(filtro.getDataFinal().getTime()));

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    TransacoesNaoProcessadasDTO dto = new TransacoesNaoProcessadasDTO();
                    dto.setTotalPaginas(rs.getLong("totalregistros"));
                    dto.setId(rs.getLong("linha"));
                    dto.setCodDataCredito(rs.getString("DATA_CREDITO"));
                    dto.setNumeroUnico(rs.getString("nro_unico"));
                    dto.setValorLiquido(rs.getBigDecimal("vlr_liquido"));
                    dto.setValorLiquidoAntecipado(rs.getBigDecimal("vlr_liquido_antec"));
                    dto.setNumeroAntecipacao(rs.getLong("nro_antecipacao"));
                    dto.setCodigoEstabelecimentoSubmissor(rs.getLong("cod_estabelecimento"));

                    lista.add(dto);
                }
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return lista;
    }
}
