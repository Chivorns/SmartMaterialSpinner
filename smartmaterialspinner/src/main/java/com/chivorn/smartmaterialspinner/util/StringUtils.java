package com.chivorn.smartmaterialspinner.util;

import java.text.Normalizer;

public class StringUtils {
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}