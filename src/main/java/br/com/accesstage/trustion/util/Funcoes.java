package br.com.accesstage.trustion.util;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.client.cartoes.pci.ws.PciWs;
import br.com.accesstage.trustion.configs.log.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class Funcoes {

    @Log
    private static Logger LOGGER;

    private Funcoes() {
        throw new IllegalAccessError("Utility class");
    }

    public static StringBuilder getSqlString(InputStream streamSqlFile) {

        StringBuilder builder = null;

        if (streamSqlFile != null) {
            int pos = 0;
            char[] bytesLidos = new char[2 * 1024];

            try (InputStream inputStream = streamSqlFile;
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);) {
                builder = new StringBuilder();
                while ((pos = inputStreamReader.read(bytesLidos)) != -1) {
                    builder.append(bytesLidos, 0, pos);
                }
            } catch (IOException e) {
                LOGGER.error("Ocorreu um erro no metodo Funcoes.getSqlString: ", e.getMessage(), e);
            }
        }

        return builder;
    }

    /**
     * Busca um recurso a partir de seu nome.
     *
     * @param parentDir caso o arquivo esteja num diretório diferente do padrão.
     * @param resourceName o nome do recurso
     * @return o conteúdo SQL do arquivo.
     */
    public static String getSqlString(String parentDir, String resourceName) {
        String resource = "";
        if (StringUtils.isNotBlank(parentDir)) {
            resource = parentDir + resourceName;
        }
        InputStream resourceAsStream = Funcoes.class.getClassLoader().getResourceAsStream(resource);
        return getSqlString(resourceAsStream).toString();
    }

    public static String removeZerosEsquerda(String linha) {
        return linha.replaceFirst("0*", "");
    }

    public static String insereZerosEsquerda(String pValue, int pLength) {
        for (int i = pValue.length(); i < pLength; i++) {
            pValue = "0" + pValue;
        }
        return pValue;
    }

    /**
     * *
     * Verifica se a String é nula ou vazia ou só tem espaços em branco
     *
     * @param l
     * @return
     */
    public static boolean isNullOrZero(Long l) {
        return (l == null || l == 0);
    }

    /**
     * Converte uma lista em uma string com itens separados por vírgula.
     * <b>Exemplo:</b> 1,2,3
     *
     * @param list
     * @return string
     */
    public static String formataArray(List<?> list) {
        if (Utils.isNotEmpty(list)) {
            return list.toString().replace("[", "").replace("]", "");
        }
        return "";
    }

    /**
     * Recebe a data e retorna um long da data no formato yyyyMMdd
     *
     * @param data
     * @return data em milissegundos
     */
    public static Long formataDataRelatorio(Date data) {
        SimpleDateFormat formatDataRel = new SimpleDateFormat("yyyyMMdd");
        String dtFormatada = formatDataRel.format(data);
        return Long.parseLong(dtFormatada);
    }

    /**
     * Recebe a long no formato yyyyMMdd e faz o parse para data
     *
     * @param data
     * @return o parse para data do formato acima
     */
    public static Date formataDataRelatorio(Long data) {
        SimpleDateFormat formatDataRel = new SimpleDateFormat("yyyyMMdd");
        Date dtFormatada;
        try {
            dtFormatada = formatDataRel.parse(String.valueOf(data));
        } catch (ParseException e) {
            dtFormatada = null;
        }
        return dtFormatada;
    }

    /**
     * Recebe a long no formato yyyyMMdd e faz o parse para data
     *
     * @param data
     * @return o parse para data do formato acima
     */
    public static Date formataDataRelatorioStr2(String data) {
        SimpleDateFormat formatDataRel = new SimpleDateFormat("dd/MM/yyyy");
        Date dtFormatada;
        try {
            dtFormatada = formatDataRel.parse(String.valueOf(data));
        } catch (ParseException e) {
            dtFormatada = null;
        }
        return dtFormatada;
    }

    /**
     * Converte uma lista de empresas em uma string preenchida com as
     * respectivas razões sociais separadas por vírgula.
     *
     * @param empresaList
     * @return cláusula IN
     */
    public static String montaClausulaIn(Set<EmpresaCA> empresaList) {
        List<String> list = new ArrayList<>();

        empresaList.forEach((empresaCA) -> {
            list.add(empresaCA.getId().toString());
        });
        return Funcoes.formataArray(list);
    }

    /**
     * Divide um bigDecimal por cem.
     *
     * @param bigDecimal
     * @return bigDecimal
     */
    public static BigDecimal dividePorCem(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            bigDecimal = bigDecimal.divide(new BigDecimal(100D));
        }
        return bigDecimal;
    }

    /**
     * Divide um double por cem.
     *
     * @param d
     * @return bigDecimal
     */
    public static Double dividePorCem(Double d) {
        if (d != null) {
            d = d / 100D;
        }
        return d;
    }

    /**
     * Recebe a data e retorna um long da data no formato yyyyMMdd
     *
     * @param data
     * @return data em milissegundos
     */
    public static String formataDataPesquisaDataProcessamento(Date data) {
        SimpleDateFormat formatDataRel = new SimpleDateFormat("dd/MM/yyyy");
        String dtFormatada = formatDataRel.format(data);
        return dtFormatada;
    }

    public static String formataNroCartao(String nroCartao, String sglOperadora) {
        String nroFormatado;
        // Caso ocorra erro na formatacao do cartão, retorna o próprio nro
        try {
            if (nroCartao != null && !"".equals(nroCartao) && nroCartao.length() > 26) {
                try {
                    // TODO: Lopes - Ver como vai funcionar o esquema para
                    // esse serviço
                    nroCartao = PciWs.getIntance().obterValor(nroCartao);
                } catch (Exception e) {
                    nroCartao = "Servi\u00E7o PCI indispon\u00EDvel";
                }

                if ("AMEX".equals(sglOperadora)) {
                    nroFormatado = nroCartao.substring(0, 4) + "*****"
                            + nroCartao.substring(9, (nroCartao.length() >= 15 ? 15 : nroCartao.length()));
                } else if ("TCKT".equals(sglOperadora)) {
                    nroFormatado = nroCartao;
                } else if ("GLBP".equals(sglOperadora)) {

                    LOGGER.info("[GLBP]Removendo zeros a Esquerda - " + sglOperadora);

                    String cartaoGlobalpay = removeZerosEsquerda(nroCartao);

                    if (cartaoGlobalpay.length() >= 16) {

                        nroFormatado = cartaoGlobalpay.substring(0, 6) + "******"
                                + cartaoGlobalpay.substring(cartaoGlobalpay.length() - 4, (cartaoGlobalpay.length() >= 19 ? 19 : cartaoGlobalpay.length()));

                    } else if (cartaoGlobalpay.length() < 16 && cartaoGlobalpay.length() >= 13) {

                        nroFormatado = cartaoGlobalpay.substring(0, 4) + "******"
                                + cartaoGlobalpay.substring(cartaoGlobalpay.length() - 4, (cartaoGlobalpay.length() >= 19 ? 19 : cartaoGlobalpay.length()));

                    } else {
                        nroFormatado = cartaoGlobalpay;
                    }

                } else {
                    nroFormatado = nroCartao.substring(0, 8) + "****"
                            + nroCartao.substring(12, (nroCartao.length() >= 16 ? 16 : nroCartao.length()));
                }
            } else {
                nroFormatado = nroCartao;
            }

        } catch (Exception e) {
            nroFormatado = nroCartao.substring(0, (nroCartao.length() >= 25 ? 25 : nroCartao.length()));
        }

        return nroFormatado;
    }

    public static String capitalize(String s) {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

}
