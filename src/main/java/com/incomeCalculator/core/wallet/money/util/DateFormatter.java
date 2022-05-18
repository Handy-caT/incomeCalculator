package com.incomeCalculator.core.wallet.money.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat sqlFormatter = new SimpleDateFormat("dd_MM_yyyy");

    public static String sqlFormat(Date date) {
       return sqlFormatter.format(date);
    }
    public static String webFormat(Date date) {
        return webFormatter.format(date);
    }
}
