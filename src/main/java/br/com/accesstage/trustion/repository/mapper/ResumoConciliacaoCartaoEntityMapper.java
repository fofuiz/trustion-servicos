package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.dto.FiltroTelasConciliacaoCartaoResumoDTO;
import br.com.accesstage.trustion.util.ParametroQueryUtil;

public class ResumoConciliacaoCartaoEntityMapper {

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    public String complementaResumoQuery2(String sql, FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");

        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && !ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && !ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getAnoMesRefStr())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroAgencia())
                && ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroConta())
                && ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(
                    //queryBuscarRegistrosExtratoResumidosConciliados
                    "SELECT "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY') AS DTA_EXTR_PAGAMENTO, "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(A.VLR_EXTR_TOTAL) AS VLR_EXTR_TOTAL, SUM(null) AS VLR_PGTO_TOTAL, A.STA_CONCILIADO "
                    + "FROM "
                    + "FIN_CONTABIL A, ADM_OPERADORA B, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = A.COD_OPERADORA (+) "
                    + "AND A.COD_CONTA_BANCO = D.COD_CONTA_BANCO "
                    + "AND A.COD_EMPRESA = ? "
                    + "AND A.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') >= ? "
                    + "AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'YYYYMMDD') <= ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY  "
                    + "A.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(A.DTA_EXTR_PAGAMENTO,'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, A.STA_CONCILIADO "
                    + "UNION "
                    + //queryBuscarRegistrosCartaoResumidosConciliados
                    "SELECT "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO AS COD_BANCO, D.NRO_AGENCIA AS COD_AGENCIA, D.NRO_CONTA_CORRENTE AS COD_CONTA, "
                    + "SUM(null) AS VLR_EXTR_TOTAL, SUM(C.VLR_LIQUIDO) AS VLR_PGTO_TOTAL, C.STA_CONCILIADO "
                    + "FROM "
                    + "ADM_OPERADORA B, FATO_TRANSACAO_SUM C, DIM_CONTA_BANCO D "
                    + "WHERE "
                    + "B.COD_OPERADORA = C.COD_OPERADORA (+) "
                    + "AND C.COD_CONTA_BANCO  = D.COD_CONTA_BANCO "
                    + "AND C.COD_STATUS IN (3, 4, 6 ,7 ,8) "
                    + "AND C.STA_CONCILIADO = 'S' "
                    + "AND D.STA_ATIVO = 'SIM' "
                    + "AND C.COD_DATA_CREDITO >= ? "
                    + "AND C.COD_DATA_CREDITO <= ? "
                    + "AND C.EMPID = ? "
                    + "AND D.NRO_BANCO = ? "
                    + "AND D.NRO_AGENCIA = ? "
                    + "AND D.NRO_CONTA_CORRENTE = ? "
                    + "AND B.COD_OPERADORA = ? "
                    + "GROUP BY "
                    + "C.COD_OPERADORA, B.NME_EXIBICAO_PORTAL, TO_CHAR(TO_DATE(C.COD_DATA_CREDITO,'YYYYMMDD'),'MM/YYYY'), "
                    + "D.NRO_BANCO, D.NRO_AGENCIA, D.NRO_CONTA_CORRENTE, C.STA_CONCILIADO    "
                    + ") "
                    + "GROUP BY "
                    + "COD_OPERADORA, NME_EXIBICAO_PORTAL, DTA_EXTR_PAGAMENTO, COD_BANCO, COD_AGENCIA, COD_CONTA, STA_CONCILIADO ");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }
        return query.toString();
    }

    /**
     *
     * @param filtro
     * @return
     */
    public Object[] obtemParametrosResumoQuery1(FiltroTelasConciliacaoCartaoResumoDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosResumoQuery1(filtro)];
        int indiceAtivo = 0;

        //queryBuscarRegistrosExtratoResumidosConciliados
        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "01";
            indiceAtivo++;

            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "31";
            indiceAtivo++;

        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroBanco());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroAgencia());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            parametros[indiceAtivo] = filtro.getNumeroConta();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            parametros[indiceAtivo] = filtro.getStatusConciliado();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "01";
            indiceAtivo++;

            parametros[indiceAtivo] = filtro.getAnoMesRefStr() + "31";
            indiceAtivo++;

        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            parametros[indiceAtivo] = filtro.getCodigoOperadora();
            indiceAtivo++;
        }

        //queryBuscarRegistrosCartaoResumidosConciliados
        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroBanco());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            parametros[indiceAtivo] = Long.parseLong(filtro.getNumeroAgencia());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            parametros[indiceAtivo] = filtro.getNumeroConta();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            parametros[indiceAtivo] = filtro.getCodigoOperadora();
            indiceAtivo++;
        }

        return parametros;
    }

    /**
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosResumoQuery1(FiltroTelasConciliacaoCartaoResumoDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        //queryBuscarRegistrosExtratoResumidosConciliados
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getAnoMesRefStr())) {
            paramentrosQtd++;
            paramentrosQtd++;
            paramentrosQtd++;
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            paramentrosQtd++;
        }
        //queryBuscarRegistrosCartaoResumidosConciliados
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            paramentrosQtd++;
        }
        paramentrosQtd++;

        return paramentrosQtd;
    }

}
