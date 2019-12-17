package br.com.accesstage.trustion.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    private static final String[] SENHA_CARACTERES = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
        "V", "W", "X", "Y", "Z", "!", "@", "*", "?"};

    private static final String RETICENCIAS = "...";

    public static String gerarSenhaAleatoria() {

        StringBuilder senha = new StringBuilder();

        for (int x = 0; x < 8; x++) {
            int j = (int) (Math.random() * SENHA_CARACTERES.length);

            senha.append(SENHA_CARACTERES[j]);
        }

        return senha.toString();
    }

    public static boolean isSenhaValida(String senha) {

        boolean isSenhaValida = false;

        if (senha.length() < 8) {
            return false;
        }

        for (String numero : SENHA_CARACTERES) {

            if (senha.contains(numero)) {
                isSenhaValida = true;
                break;
            }
        }

        return isSenhaValida;
    }

    public static String getInicioMetodo() {
        // .getStackTrace()[1] //Pega o método que chamou
        // .getStackTrace()[2] //Pega o método atual
        return "Iniciando metodo " + Thread.currentThread().getStackTrace()[2].getMethodName() + RETICENCIAS;
    }

    public static String getFimMetodo() {
        return "Finalizando metodo " + Thread.currentThread().getStackTrace()[2].getMethodName() + RETICENCIAS;
    }

    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

    public static List<Date> getDatesBetweenAndZeroTime(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar) || calendar.equals(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        List<Date> datasHorasZeradas = new ArrayList<>();
        for (Date datasZerar: datesInRange) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(datasZerar);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            datasHorasZeradas.add(cal.getTime());
        }
        return datasHorasZeradas;
    }

    public static LocalDate getConvertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static boolean isNotEmpty(Collection coll) {
        if (coll != null && !coll.isEmpty()) {
            coll.removeIf(Objects::isNull);
            return !coll.isEmpty();
        }
        return false;
    }

    public static boolean validarString(String texto) {
        return !Pattern.matches("^[a-zA-Z0-9 \\.\\-\\/]*$", texto);
    }

    public static String retirarCaracteresEspeciaisString(String texto){
        return texto.replaceAll("(\\W|^_)*", "");
    }

}
