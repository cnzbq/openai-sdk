package io.github.cnzbq.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zbq
 * @since 1.0.0
 */
@Slf4j
public class Md5Util {
    /**
     * 32位md5
     */
    public static String md5Hex(String str) {
        try {
            byte[] secretBytes = MessageDigest.getInstance("md5").digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code.insert(0, "0");
            }
            return md5code.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error("算法不存在", e);
        }

        return null;
    }
}
