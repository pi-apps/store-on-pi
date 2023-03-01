package com.api.service.serviceReturn;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class AESUtil {

    /**
     * AES算法128位加密
     *
     * @param cleartext
     * @return
     */
    public static String encrypt(String cleartext, String aesKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(cleartext.getBytes("UTF-8"));

            return parseByte2HexStr(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES算法128位解密
     *
     * @param encrypMsg
     * @return
     */
    public static String decrypt(String encrypMsg, String aesKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decbbdt = cipher.doFinal(Base64.getDecoder().decode(encrypMsg));

            return new String(decbbdt, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
