package com.itheima.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String Data2String(Date date, String patt){
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        String str = sdf.format(date);
        return str;
    }

    public static Date String2Date(String str, String patt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        Date date = sdf.parse(str);
        return date;
    }
}
