package ubb.scs.map.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {
    public static String hashPassword(String plainTextPassword) {
        return DigestUtils.sha256Hex(plainTextPassword);
    }
}