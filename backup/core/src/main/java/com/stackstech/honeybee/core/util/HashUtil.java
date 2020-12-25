package com.stackstech.honeybee.core.util;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * jadclipse
 * Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
 * Referenced classes of package com.cjhs.framework.util.http:
 * Base64Coder
 * <p>
 * 此类来自GT程序会员密码加密
 */
public class HashUtil {

    public HashUtil() {
    }

    public static String hash(String stringToHash, String algorithm)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return hash(stringToHash, algorithm, DEFAULT_ENCODING);
    }

    public static String hash(String stringToHash, String algorithm, String encoding)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String result = null;
        if (stringToHash != null) {
            result = new String(hash(stringToHash.getBytes(encoding), algorithm));
        }
        return result;
    }

    public static byte[] hash(byte[] bytes, String algorithm)
            throws NoSuchAlgorithmException {
        byte[] resultBytes = null;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        if (bytes != null) {
            resultBytes = md.digest(bytes);
        }
        return resultBytes;
    }

    public static byte[] hashUsingSha1(byte[] bytes) {
        try {
            return hash(bytes, "SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public static String hashUsingSha1AndEncode(String stringToHash)
            throws UnsupportedEncodingException {
        return hashUsingSha1AndEncode(stringToHash, DEFAULT_ENCODING);
    }

    public static String hashUsingSha1AndEncode(String stringToHash, String encoding)
            throws UnsupportedEncodingException {
        String result = null;
        if (stringToHash != null) {
            byte[] bytes = hashUsingSha1(stringToHash.getBytes(encoding));
            if (bytes != null) {
                result = new String(Base64Coder.encode(bytes));
            }
        }
        return result;
    }

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";
    private static final String DEFAULT_ENCODING = "ISO-8859-1";

}
