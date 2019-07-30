package com.easybuy.sg.grouponebuy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelperClass {
    public static String getCurrentDate() {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}
