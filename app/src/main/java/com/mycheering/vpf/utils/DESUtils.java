package com.mycheering.vpf.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DESUtils {


    public static byte[] DESTransform(byte[] data, String desKey, boolean encrypt) {

        byte[] finalData = null;

        try {
            byte[] ivbyte = {1, 2, 3, 4, 5, 6, 7, 8};
            IvParameterSpec iv = new IvParameterSpec(ivbyte);
            AlgorithmParameterSpec paramSpec = iv;
            DESKeySpec dks = new DESKeySpec(desKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                    secretKey, paramSpec);
            finalData = cipher.doFinal(data);
        } catch (Exception e) {
            L.e(e.getMessage());
            e.printStackTrace();
        }

        return finalData;
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException();
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;

    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();

    }

    public final static String encrypt(String data, String key) {
        if (data != null) {
            try {
                return byte2hex(DESTransform(data.getBytes(), key, true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public final static String decrypt(String data, String key) {
        return new String(DESTransform(hex2byte(data.getBytes()), key, false));
    }
}
