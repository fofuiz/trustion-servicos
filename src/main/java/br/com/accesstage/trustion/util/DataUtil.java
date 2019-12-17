package br.com.accesstage.trustion.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataUtil {

    private DataUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Metodo para retornar os anos de referencia
     *
     * @return
     */
    public static List<String> getAnoReferencia() {

        List<String> anoReferencia = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int ano = calendar.get(Calendar.YEAR);

        anoReferencia.add(String.valueOf(ano));
        for (int i = 0; i < 2; i++) {
            ano--;
            anoReferencia.add(String.valueOf(ano));
        }

        return anoReferencia;
    }
}
