package br.com.accesstage.trustion.util;

public class ConstantUtil {

    public static final long COD_STEP_CONCILIACAO_MANUAL = 30L;

    public static final String OPERACAO_CONCILIACAO_MANUAL = "CM";

    public static final String OPERACAO_DESFAZER_CONCILIACAO = "DC";

    public static final String MSG_CONCILIACAO_SUCESSO_CONCILIAR_MANUAL = "Concilia\u00E7\u00E3o manual realizada com sucesso.";

    public static final String MSG_CONCILIACAO_ERRO_CONCILIAR_MANUAL = "Falhou ao conciliar.";

    public static final String MSG_CONCILIACAO_DESFAZER_ERROR = "Falhou ao desfazer concilia\u00E7\u00E3o.";

    public static final String MSG_CONCILIACAO_DESFAZER_SUCCESS = "Concilia\u00E7\u00E3o alterada com sucesso.";

    public static final Integer EMP_ID_USUARIO_NET = 19477;

    public static final Long CODIGO_AGRUPADOR = 2l;

    public static final String MSG_SUCESSO_SALVAR_SOLICITACAO = "Solicita\u00E7\u00E3o de relat\u00f3rio DCC gerado com sucesso.";

    public static final String MSG_ERRO_SALVAR_SOLICITACAO_REL_DCC = "Falhou ao salvar solicita\u00E7\u00E3o de relat\u00f3rio DCC.";

    public static final String MSG_ERRO_SALVAR_SOLICITACAO_REL_DCC_DUP = "Solicita\u00E7\u00E3o de relat\u00f3rio DCC Duplicado.";

    public static final String RELATORIO_DCC_CONFIG = "/data/ASConciliacao/netrelatoriosdcc/config/config.properties";

    // public static final String RELATORIO_DCC_CONFIG = "C:\\data\\ASConciliacao\\netrelatoriosdcc\\config\\config.properties";

    public static final String MSG_ERRO_DOWNLOAD_REL_FILE_NOT_FOUND = "Falhou ao fazer o download do relat\u00f3rio, arquivo n\u00E3o existe em disco.";

    public static final String MSG_ERRO_DOWNLOAD_REL = "Falhou ao fazer o download do relat\u00f3rio.";

    public static final String RELATORIO_DCC_PATH_DETALHADO = "path.out.detalhado";

    public static final String RELATORIO_DCC_PATH_SUMARIZADO = "path.out.sumarizado";

    //Sprint IV - Parte
    public static final String MSG_NOVA_CONCILIACAO_SUCESSO_CONCILIAR_MANUAL = "Nova Concilia\u00E7\u00E3o manual realizada com sucesso.";

    public static final String MSG_NOVA_CONCILIACAO_ERRO_CONCILIAR_MANUAL = "Falhou ao conciliar nova manual.";

    public static final String MSG_NOVA_CONCILIACAO_DESFAZER_SUCCESS = "Nova Concilia\u00E7\u00E3o alterada com sucesso.";

    public static final String MSG_NOVA_CONCILIACAO_DESFAZER_ERROR = "Falhou ao desfazer nova concilia\u00E7\u00E3o.";

    private ConstantUtil() {
        throw new IllegalAccessError("Utility class");
    }

}