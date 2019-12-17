package br.com.accesstage.trustion.repository.constantes;

public final class ConstantesAluguelEquipamento {

    public static final String BUSCAR_ALUGUEL = "SELECT f.cod_data_credito AS DATA_CREDITO,"
            + " o.nme_exibicao_portal AS BANDEIRA, SUM(f.vlr_bruto) AS VALOR,"
            + " NVL(l.nme_loja, v.nro_terminal) AS LOJA"
            + " FROM fato_transacao_sum f , adm_operadora o , adm_ponto_venda v , adm_loja l"
            // ALUGUEL POS
            + " WHERE f.cod_natureza  = 3"
            + " AND f.cod_operadora   = o.cod_operadora"
            + " AND f.cod_ponto_venda = v.cod_ponto_venda"
            + " AND v.cod_loja        = l.cod_loja(+)"
            // 2 - PREVISTO / 3 - LIQUIDADO
            + " AND f.cod_status     IN (2, 3)"
            + " AND f.empid          IN(:idsEmp)"
            + " AND f.cod_data_credito BETWEEN :dtaInicio AND :dtaFim"
            + " GROUP BY o.nme_exibicao_portal, f.cod_data_credito, NVL(l.nme_loja, v.nro_terminal)"
            + " ORDER BY f.cod_data_credito , o.nme_exibicao_portal";

    private ConstantesAluguelEquipamento() {
        throw new IllegalAccessError("Utility class");
    }

}
