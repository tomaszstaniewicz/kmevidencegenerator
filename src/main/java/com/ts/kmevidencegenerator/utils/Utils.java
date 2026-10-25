package com.ts.kmevidencegenerator.utils;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class Utils {

    public static String getMonthName(Month month) {
        return month.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl", "PL"));
    }

    public static String getFileName(YearMonth yearMonth) {
        return "ewidencja_przebiegu_pojazdu_Staniewicz_DLE37216_" + getMonthName(yearMonth.getMonth()) + "_" + yearMonth.getYear();
    }
}
