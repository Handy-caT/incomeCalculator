package com.incomeCalculator.core.wallet.money.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static final SimpleDateFormat webFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sqlFormatter = new SimpleDateFormat("dd_MM_yyyy");

    public static String sqlFormat(Date date) {
       return sqlFormatter.format(date);
    }
    public static String webFormat(Date date) {
        return webFormatter.format(date);
    }

    public static Date sqlParse(String dateString) throws ParseException {
        return sqlFormatter.parse(dateString);
    }
    public static Date webParse(String dateString) throws ParseException {
        return webFormatter.parse(dateString);
    }

}
