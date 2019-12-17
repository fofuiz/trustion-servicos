package br.com.accesstage.trustion.repository.constantes;

public final class ConstantesFiltroComboBox {

    public static final String QUERY_PESQUISA_MOTIVO_CONCILIACAO_POR_EMPID = "SELECT  "
            + "COD_MOTIVO_CONC_MANUAL,  "
            + "DSC_MOTIVO_CONC_MANUAL  "
            + "FROM  "
            + " MOTIVO_CONC_MANUAL  "
            + "WHERE "
            + " STA_ATIVO = ? "
            + "AND "
            + " EMPID = ?";

    /**
     * QUERY para buscar todos os servicos por empID.
     */
    public static final String QUERY_PESQUISA_SERVICOS_POR_EMPID = " SELECT  COD_SERVICO, DSC_SERVICO  FROM SERVICO WHERE STA_ATIVO = 'S' AND EMPID = ?";

    /**
     * QUERY para buscar todas as companias por empID.
     */
    public static final String QUERY_PESQUISA_CIA_POR_EMPID = " SELECT  COD_CIA, NME_CIA FROM COMPANHIA WHERE EMPID = ?";

    /**
     * QUERY para buscar todas as modalidades de tarifa.
     */
    public static final String QUERY_PESQUISA_MODALIDADES_TARIFA = " SELECT COD_MODALIDADE, TPO_ARREC, DSC_MODALIDADE FROM MODALIDADE_TARIFA WHERE STA_ATIVO = 'S' order by COD_MODALIDADE";

    /**
     * QUERY para buscar todos os status da concilia��o.
     */
    public static final String QUERY_PESQUISA_STATUS_CONCILIACAO = "SELECT STA_CONCILIADO, DSC_STA_CONCILIADO FROM STATUS_CONCILIADO";

    /**
     * QUERY para listar todos adquirentes.
     */
    public static final String QUERY_PESQUISA_ADQUIRENTES = "SELECT COD_OPERADORA, NME_EXIBICAO_PORTAL FROM ADM_OPERADORA ORDER BY NME_EXIBICAO_PORTAL";

    private ConstantesFiltroComboBox() {
        throw new IllegalAccessError("Utility class");
    }

}
