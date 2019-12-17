package br.com.accesstage.trustion.repository.constantes;

public class ConstantesDomicilioBancario {

    /**
     * QUERY para buscar domicilio bancario ativo por código do banco e código
     * da empresa
     */
    public static final String QUERE_PESQUISA_DOMICILIO_BANCARIO_POR_EMPRESA = "SELECT DSC_CONTA_BANCO, NRO_BANCO, NRO_CONTA_CORRENTE, NRO_AGENCIA, STA_ATIVO, EMPID FROM DIM_CONTA_BANCO WHERE EMPID = ?";

    /**
     * Nova QUERY para buscar todos os bancos ativos.
     */
    public static final String QUERY_NOVA_PESQUISA_TODOS_BANCOS = "SELECT DISTINCT NRO_BANCO FROM DIM_CONTA_BANCO WHERE EMPID = ? ORDER BY NRO_BANCO";

    private ConstantesDomicilioBancario() {
        throw new IllegalAccessError("Utility class");
    }
}
