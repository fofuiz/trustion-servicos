package br.com.accesstage.trustion.util.ascartoes;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {
        throw new IllegalAccessError("Utility class");
    }

    public static Long formataDataRelatorio(Date data) {
        SimpleDateFormat formatDataRel = new SimpleDateFormat("yyyyMMdd");
        String dtFormatada = formatDataRel.format(data);
        return Long.parseLong(dtFormatada);
    }

    public static String formatFields(String pattern, Object value) throws ParseException {
        MaskFormatter mask = new MaskFormatter(pattern);
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(value);
    }
}
