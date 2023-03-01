package com.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UUID {

    public static String randomUUID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HH'H'mm'M'ss'S'SSS");
        String id = sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000000) + (int) ((Math.random() * 9 + 1) * 10);
        return id;
    }

    public static String randomQr() {
        String id = (int) ((Math.random() * 9 + 1) * 1000) + "-" + (int) ((Math.random() * 9 + 1) * 1000) + "-" + (int) ((Math.random() * 9 + 1) * 1000);
        return id;
    }

}
