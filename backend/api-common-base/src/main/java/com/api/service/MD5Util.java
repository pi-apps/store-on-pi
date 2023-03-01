package com.api.service;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Optional;

public class MD5Util {
    public MD5Util() {
    }

    public static String md5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            return bytesToHex(md5.digest());
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);

        String hashtext;
        for (hashtext = bigInt.toString(16); hashtext.length() < 32; hashtext = "0" + hashtext) {
            ;
        }

        return hashtext;
    }

    public static String MD5Encode(String origin, Charset charset) {
        return DigestUtils.md5Hex(origin.getBytes(Optional.ofNullable(charset).orElse(Charset.forName("UTF-8"))));
    }

    public static String MD5Encode(String origin) {

        return MD5Encode(origin, Charset.forName("UTF-8"));
    }

}