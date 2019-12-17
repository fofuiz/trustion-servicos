package br.com.accesstage.trustion.repository.constantes;

public final class ConstantesDB {

    public static final String CONTEXTO_SQL = "br/com/accesstage/ascartoes/sql/";

    public static final String CONTEXTO_SQL_REMESSA_MANUAL = CONTEXTO_SQL + "remessaManual/";

    public static final String CONTEXTO_SQL_VENDAS_CONCILIADAS = CONTEXTO_SQL + "vendasConciliadas/";

    public static final String CONTEXTO_SQL_DIVERGENCIA = CONTEXTO_SQL + "divergencia/";

    public static final String AGRUPAMENTO_BANCO = "AGRUPAMENTO_BANCO";

    public static final String AGRUPAMENTO_CIA = "AGRUPAMENTO_CIA";

    public static final String CONTEXTO_SQL_NSU = CONTEXTO_SQL + "nsu/";

    /**
     * query base para buscar os dados de todos os bancos para a tela de resumo
     * banco agrupado por banco.
     */
    public static final String QUERY_RESUMO_BANCO_TODOS_GROUP_BY_BANCO = "SELECT v.COD_BANCO,v.NME_BANCO,"
            + "COD_SERVICO,NME_SERVICO,SUM(v.QTD_TOTAL) AS QTD_TOTAL,SUM(v.QTD_SIM)  "
            + " AS QTD_SIM,SUM(v.QTD_NAO)   AS QTD_NAO, SUM(v.PC_QTD_SIM) AS PC_QTD_SIM, SUM(v.PC_QTD_NAO) AS PC_QTD_NAO,"
            + " SUM(v.VLR_TOTAL) AS VLR_TOTAL,  SUM(v.VLR_SIM)   AS VLR_SIM, "
            + " SUM(v.VLR_NAO)   AS VLR_NAO , SUM(v.PC_VLR_SIM)   AS PC_VLR_SIM, SUM(v.PC_VLR_NAO)   AS PC_VLR_NAO"
            + " FROM "
            + " V_RESUMO_REF_BCO v"
            + " WHERE v.MESREF = ?"
            + " AND v.EMPID    = ? "
            + " GROUP BY COD_SERVICO,NME_SERVICO, NME_BANCO, COD_BANCO"
            + " ORDER BY COD_BANCO";

    /**
     * query base para buscar os dados para a tela de resumo banco agrupado por
     * banco.
     */
    public static final String QUERY_BASE_RESUMO_BANCO_GROUP_BY_BANCO = "SELECT v.COD_BANCO AS COD_BANCO_CIA, v.NME_BANCO AS NME_BANCO_CIA,"
            + " v.COD_SERVICO,v.NME_SERVICO,SUM(v.QTD_TOTAL) AS QTD_TOTAL,SUM(v.QTD_SIM)  "
            + " AS QTD_SIM,SUM(v.QTD_NAO)   AS QTD_NAO, SUM(v.PC_QTD_SIM) AS PC_QTD_SIM, SUM(v.PC_QTD_NAO) AS PC_QTD_NAO,"
            + " SUM(v.VLR_TOTAL) AS VLR_TOTAL,  SUM(v.VLR_SIM)   AS VLR_SIM, "
            + " SUM(v.VLR_NAO)   AS VLR_NAO , SUM(v.PC_VLR_SIM)   AS PC_VLR_SIM, SUM(v.PC_VLR_NAO)   AS PC_VLR_NAO, "
            + " V.MESREF, v.EMPID "
            + " FROM "
            + " V_RESUMO_REF_BCO v"
            + " WHERE v.EMPID = ?"
            + " AND  v.MESREF   = ? ";

    public static final String GROUP_BY_BANCO_FINAL = " GROUP BY v.COD_SERVICO,v.NME_SERVICO, v.NME_BANCO, v.COD_BANCO, V.MESREF, v.EMPID"
            + " ORDER BY v.COD_BANCO";

    /**
     * query base para buscar os dados todas as Cias para a tela de resumo banco
     * agrupado por CIA.
     */
    public static final String QUERY_BASE_RESUMO_BANCO_GROUP_BY_CIA = "SELECT v.COD_CIA AS COD_BANCO_CIA ,v.NME_CIA AS NME_BANCO_CIA,"
            + " v.COD_SERVICO,v.NME_SERVICO,SUM(v.QTD_TOTAL) AS QTD_TOTAL,SUM(v.QTD_SIM)  "
            + " AS QTD_SIM,SUM(v.QTD_NAO)   AS QTD_NAO, SUM(v.PC_QTD_SIM) AS PC_QTD_SIM, SUM(v.PC_QTD_NAO) AS PC_QTD_NAO,"
            + " SUM(v.VLR_TOTAL) AS VLR_TOTAL,  SUM(v.VLR_SIM)   AS VLR_SIM, "
            + " SUM(v.VLR_NAO)   AS VLR_NAO , SUM(v.PC_VLR_SIM)   AS PC_VLR_SIM, SUM(v.PC_VLR_NAO)   AS PC_VLR_NAO, "
            + " V.MESREF, v.EMPID "
            + " FROM "
            + " V_RESUMO_REF_BCO v"
            + " WHERE v.EMPID = ?"
            + " AND  v.MESREF   = ? ";

    public static final String GROUP_BY_CIA_FINAL = " GROUP BY v.COD_SERVICO,v.NME_SERVICO, v.NME_CIA, v.COD_CIA, V.MESREF, v.EMPID"
            + " ORDER BY v.COD_CIA";

    /**
     * query base para buscar os dados todas as cias para a tela de resumo banco
     * agrupado por CIA.
     */
    public static final String QUERY_RESUMO_BANCO_TODOS_GROUP_BY_CIA = "SELECT v.COD_CIA,v.NME_CIA,"
            + "COD_SERVICO,NME_SERVICO,SUM(v.QTD_TOTAL) AS QTD_TOTAL,SUM(v.QTD_SIM)  "
            + " AS QTD_SIM,SUM(v.QTD_NAO)   AS QTD_NAO, SUM(v.PC_QTD_SIM) AS PC_QTD_SIM, SUM(v.PC_QTD_NAO) AS PC_QTD_NAO,"
            + " SUM(v.VLR_TOTAL) AS VLR_TOTAL,  SUM(v.VLR_SIM)   AS VLR_SIM, "
            + " SUM(v.VLR_NAO)   AS VLR_NAO , SUM(v.PC_VLR_SIM)   AS PC_VLR_SIM, SUM(v.PC_VLR_NAO)   AS PC_VLR_NAO"
            + " FROM "
            + " V_RESUMO_REF_BCO v"
            + " WHERE v.MESREF = ?"
            + " AND v.EMPID    = ? "
            + " GROUP BY COD_SERVICO,NME_SERVICO, NME_CIA, COD_CIA"
            + " ORDER BY COD_CIA";
    /**
     * query base para buscar os dados para a tela de resumo banco agrupado por
     * CIA.
     */
    public static final String QUERY_RESUMO_BANCO_GROUP_BY_CIA = "SELECT v.COD_CIA,v.NME_CIA,"
            + "COD_SERVICO,NME_SERVICO,SUM(v.QTD_TOTAL) AS QTD_TOTAL,SUM(v.QTD_SIM)  "
            + " AS QTD_SIM,SUM(v.QTD_NAO)   AS QTD_NAO, SUM(v.PC_QTD_SIM) AS PC_QTD_SIM, SUM(v.PC_QTD_NAO) AS PC_QTD_NAO,"
            + " SUM(v.VLR_TOTAL) AS VLR_TOTAL,  SUM(v.VLR_SIM)   AS VLR_SIM, "
            + " SUM(v.VLR_NAO)   AS VLR_NAO , SUM(v.PC_VLR_SIM)   AS PC_VLR_SIM, SUM(v.PC_VLR_NAO)   AS PC_VLR_NAO"
            + " FROM "
            + " V_RESUMO_REF_BCO v"
            + " WHERE v.MESREF = ?"
            + " AND v.EMPID    = ? "
            + " AND V.COD_CIA = ? "
            + " GROUP BY COD_SERVICO,NME_SERVICO, NME_CIA, COD_CIA"
            + " ORDER BY COD_CIA";

    /**
     * QUERY para buscar todos os bancos ativos.
     */
    public static final String QUERY_PESQUISA_TODOS_BANCOS = "SELECT DISTINCT COD_BANCO, NME_BANCO, NME_RESUMIDO FROM BANCO WHERE STA_ATIVO = 'S' order by NME_RESUMIDO ";

    //Sprint IV - Parte I
    /**
     * Nova QUERY para buscar todos os bancos ativos.
     */
    public static final String QUERY_NOVA_PESQUISA_TODOS_BANCOS = "SELECT DISTINCT NRO_BANCO FROM DIM_CONTA_BANCO WHERE EMPID = ? ORDER BY NRO_BANCO";

    //Sprint IV - Parte II
    /**
     * QUERY para buscar domicilio bancario ativo por c�digo do banco e c�digo
     * da empresa
     */
    public static final String QUERY_PESQUISA_DOMICILIO_BANCARIO_POR_EMPRESA = "SELECT DSC_CONTA_BANCO, NRO_BANCO, NRO_CONTA_CORRENTE, NRO_AGENCIA, STA_ATIVO, EMPID FROM DIM_CONTA_BANCO WHERE EMPID = ?";

    /**
     * QUERY para buscar banco ativo por codigo.
     */
    public static final String QUERY_PESQUISA_BANCO_POR_CODIGO = " SELECT  COD_BANCO, NME_BANCO, NME_RESUMIDO FROM BANCO WHERE STA_ATIVO = 'S' AND COD_BANCO = ?";

    /**
     * QUERY PARA BUSCAR todos bancos RESUMO CONTABIL AGRUPADO POR BANCOS
     *
     */
    public static final String QUERY_RESUMO_CONTABIL_TODOS_GROUP_BY_BANCO = "SELECT COD_BANCO,NME_BANCO, COD_BANCO_ANT, "
            + " NME_BANCO_ANT,COD_CIA, NME_CIA, SUM(QTD_TOTAL) as QTD_TOTAL,SUM(QTD_SIM) as QTD_SIM,"
            + " SUM(QTD_NAO) as QTD_NAO, SUM(VLR_TOTAL) as VLR_TOTAL, SUM(VLR_SIM) as VLR_SIM,"
            + " SUM(VLR_NAO) as VLR_NAO FROM V_RESUMO_CIA_BCO "
            + " WHERE EMPID = ? "
            + " AND MESREF = ? "
            + " GROUP BY COD_BANCO,NME_BANCO,COD_BANCO_ANT,NME_BANCO_ANT,COD_CIA, NME_CIA";

    /**
     * QUERY PARA BUSCAR RESUMO CONTABIL AGRUPADO POR BANCOS
     *
     */
    public static final String QUERY_RESUMO_CONTABIL_GROUP_BY_BANCO = "SELECT COD_BANCO,NME_BANCO, COD_BANCO_ANT, "
            + " NME_BANCO_ANT,COD_CIA,NME_CIA,SUM(QTD_TOTAL) as QTD_TOTAL,SUM(QTD_SIM) as QTD_SIM,"
            + " SUM(QTD_NAO) as QTD_NAO, SUM(VLR_TOTAL) as VLR_TOTAL, SUM(VLR_SIM) as VLR_SIM,"
            + " sum(VLR_NAO) as VLR_NAO  FROM V_RESUMO_CIA_BCO "
            + " WHERE EMPID = ? "
            + " AND MESREF = ? "
            + " AND COD_BANCO = ? "
            + " GROUP BY COD_BANCO,NME_BANCO,COD_BANCO_ANT,NME_BANCO_ANT,COD_CIA,NME_CIA";

    /**
     * QUERY PARA BUSCAR RESUMO CONTABIL AGRUPADO POR BANCOS
     *
     */
    public static final String QUERY_RESUMO_CONTABIL_TODOS_GROUP_BY_CIA = "SELECT COD_CIA,NME_CIA, "
            + " COD_BANCO,NME_BANCO, COD_BANCO_ANT,NME_BANCO_ANT, "
            + " SUM(QTD_TOTAL) as QTD_TOTAL,SUM(QTD_SIM) as QTD_SIM,"
            + " SUM(QTD_NAO) as QTD_NAO, SUM(VLR_TOTAL) as VLR_TOTAL, SUM(VLR_SIM) as VLR_SIM,"
            + " sum(VLR_NAO) as VLR_NAO FROM V_RESUMO_CIA_BCO "
            + " WHERE EMPID = ? "
            + " AND MESREF = ? "
            + " GROUP BY COD_CIA,NME_CIA,COD_BANCO,NME_BANCO, COD_BANCO_ANT,NME_BANCO_ANT";

    /**
     * QUERY PARA BUSCAR RESUMO CONTABIL AGRUPADO POR BANCOS
     *
     */
    public static final String QUERY_RESUMO_CONTABIL_BY_CIA = "SELECT COD_CIA,NME_CIA, "
            + " COD_BANCO,NME_BANCO, COD_BANCO_ANT,NME_BANCO_ANT, "
            + " SUM(QTD_TOTAL) as QTD_TOTAL,SUM(QTD_SIM) as QTD_SIM,"
            + " SUM(QTD_NAO) as QTD_NAO, SUM(VLR_TOTAL) as VLR_TOTAL, SUM(VLR_SIM) as VLR_SIM,"
            + " sum(VLR_NAO) as VLR_NAO FROM V_RESUMO_CIA_BCO "
            + " WHERE EMPID = ? "
            + " AND MESREF = ? "
            + " AND COD_CIA = ? "
            + " GROUP BY COD_CIA,NME_CIA,COD_BANCO,NME_BANCO, COD_BANCO_ANT,NME_BANCO_ANT";

    /**
     * Query responsável por obter os dados do usuário de login
     */
    public static final String QUERY_OBTER_USUARIO = "SELECT "
            + "COD_USUARIO, "
            + "EMPID, "
            + "NME_USUARIO, "
            + "NME_LOGIN,  "
            + "TO_CHAR(DTA_ULTIMO_LOGIN, 'DD/MM/YYYY HH24:MI:SS') AS DTA_ULTIMO_LOGIN "
            + "FROM "
            + " AUTH_USUARIO "
            + "WHERE "
            + "  NME_LOGIN=?";

    public static final String QUERY_OBTER_USUARIO_EMPRESA = "SELECT "
            + "EMPID,  "
            + "CNPJ,  "
            + "DSC_RAZAO_SOCIAL,  "
            + "STA_EMPRESA,  "
            + "COD_SEGMENTO,  "
            + "COD_AGRUPADOR  "
            + "FROM  "
            + " ADM_EMPRESA  "
            + "WHERE  "
            + " EMPID = ? ";

    private ConstantesDB() {
        throw new IllegalAccessError("Utility class");
    }

}
