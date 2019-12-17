package br.com.accesstage.trustion.util;

import java.math.BigDecimal;

/**
 *
 * @author raphael
 */
public class Format {

    public static String insereZerosEsquerda(String pValue, int pLength) {

        if (pValue == null) {
            pValue = "";
        }
        pValue = pValue.trim();

        StringBuilder sb = new StringBuilder();
        int size = pLength - pValue.length();

        for (int i = 0; i < size; i++) {
            sb.append("0");
        }
        sb.append(pValue);

        return sb.toString();
    }

    public static String insereZerosDireita(String pValue, int pLength) {

        if (pValue == null) {
            pValue = "";
        }
        pValue = pValue.trim();

        StringBuilder sb = new StringBuilder();
        int size = pLength - pValue.length();

        sb.append(pValue);
        for (int i = 0; i < size; i++) {
            sb.append("0");
        }

        return sb.toString();
    }

    public static String insereEspacosDireita(String pValue, int pLength) {

        if (pValue == null) {
            pValue = "";
        }

        if (pValue.length() > pLength) {
            return pValue.substring(0, pLength);
        }

        StringBuilder sb = new StringBuilder();
        int size = pLength - pValue.length();

        sb.append(pValue);
        for (int i = 0; i < size; i++) {
            sb.append(" ");
        }

        return sb.toString();
    }

    public static String arredondaDecimalString(double valor, int casasDecimais) {
        return new BigDecimal(valor).setScale(casasDecimais, BigDecimal.ROUND_HALF_DOWN).toString();
    }

}
