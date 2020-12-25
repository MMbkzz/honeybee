package com.stackstech.honeybee.core.util;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordCrypt {

    @Deprecated
    public static String encode(String source, String salt) {
        SimpleHash simpleHash = new SimpleHash(
                DefaultPasswordService.DEFAULT_HASH_ALGORITHM, source, salt,
                DefaultPasswordService.DEFAULT_HASH_ITERATIONS);
        return simpleHash.toBase64();
    }

    public static String encode(String source, String salt, String status) {
        SimpleHash simpleHash = new SimpleHash(
                DefaultPasswordService.DEFAULT_HASH_ALGORITHM, source, salt,
                2);
        return simpleHash.toBase64();
    }

}
