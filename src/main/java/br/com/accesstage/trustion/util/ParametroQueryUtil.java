package br.com.accesstage.trustion.util;

public class ParametroQueryUtil {

    private static final String TODOS = "Todos";

    private ParametroQueryUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * metodo para verificar se foi informado o banco especifico para consulta.
     *
     * @param codigoBancoStr codigo banco.
     * @return true / false.
     */
    public static boolean informadoBancoEspecifico(String codigoBancoStr) {
        boolean resultado = false;
        if (codigoBancoStr != null && !TODOS.equals(codigoBancoStr) && !"".equals(codigoBancoStr)) {
            resultado = true;
        }
        return resultado;
    }

    /**
     * metodo para verificar se foi informado a compania especifico para
     * consulta.
     *
     * codigo cia.
     *
     * @param codigoCiaStr
     * @return true / false.
     */
    public static boolean informadoCiaEspecifico(String codigoCiaStr) {
        boolean resultado = false;
        if (codigoCiaStr != null && !TODOS.equals(codigoCiaStr) && !"".equals(codigoCiaStr)) {
            resultado = true;
        }
        return resultado;
    }

    /**
     * metodo para verificar se foi informado o serivco especifico para
     * consulta.
     *
     * codigo servico.
     *
     * @param codigoServicoStr
     * @return true / false.
     */
    public static boolean informadoServicoEspecifico(String codigoServicoStr) {
        boolean resultado = false;
        if (codigoServicoStr != null && !TODOS.equals(codigoServicoStr) && !"".equals(codigoServicoStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoConvenio(String convenioStr) {
        boolean resultado = false;
        if (convenioStr != null && !"".equals(convenioStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoCodigoArquivo(String codigoArquivoStr) {
        boolean resultado = false;
        if (codigoArquivoStr != null && !"".equals(codigoArquivoStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoNomeArquivo(String nomeArquivo) {
        boolean resultado = false;
        if (nomeArquivo != null && !"".equals(nomeArquivo)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoDataArquivo(String dataArquivoStr) {
        boolean resultado = false;
        if (dataArquivoStr != null && !"".equals(dataArquivoStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoModalidade(String codigoModalidadeStr) {
        boolean resultado = false;
        if (codigoModalidadeStr != null && !TODOS.equals(codigoModalidadeStr) && !"".equals(codigoModalidadeStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoValorRecebidoInicial(String valorRecebidoInicialStr) {
        boolean resultado = false;
        if (valorRecebidoInicialStr != null && !"".equals(valorRecebidoInicialStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoValorRecebidoFinal(String valorRecebidoFinalStr) {
        boolean resultado = false;
        if (valorRecebidoFinalStr != null && !"".equals(valorRecebidoFinalStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoStatus(String statusStr) {
        boolean resultado = false;
        if (statusStr != null && !TODOS.equals(statusStr) && !"".equals(statusStr)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoNumeroDocumento(String numeroDoc) {
        boolean resultado = false;
        if (numeroDoc != null && !"".equals(numeroDoc)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoHistorico(String historico) {
        boolean resultado = false;
        if (historico != null && !"".equals(historico)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean informadoCampo(String valorCampo) {
        return (valorCampo != null) && !"".trim().equals(valorCampo);
    }
}
