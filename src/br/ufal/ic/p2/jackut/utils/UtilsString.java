package br.ufal.ic.p2.jackut.utils;

import java.util.ArrayList;


/**
 * <p> Classe com métodos utilitários para {@code String}. </p>
 */

public class UtilsString {

    /**
     * <p> Formata um ArrayList para uma {@code String}. </p>
     * <p> O formato é: "{elemento1,elemento2,...,elementoN}". </p>
     *
     * @param arrayList ArrayList a ser formatado.
     * @param <T>       Tipo do ArrayList.
     * @return          String formatada.
     */

    public static <T> String formatArrayList(ArrayList<T> arrayList) {
        if (arrayList.isEmpty()) {
            return "{}";
        }

        StringBuilder formattedString = new StringBuilder("{");
        for (int i = 0; i < arrayList.size(); i++) {
            formattedString.append(arrayList.get(i).toString());
            if (i != arrayList.size() - 1) {
                formattedString.append(",");
            }
        }
        formattedString.append("}");
        return formattedString.toString();
    }
}
