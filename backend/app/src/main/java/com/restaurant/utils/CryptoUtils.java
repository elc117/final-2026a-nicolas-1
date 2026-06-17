package com.restaurant.utils;

import org.mindrot.jbcrypt.BCrypt;

public class CryptoUtils {
    
    private static final int COST = 12;

    public static String generateHash(char[] purePassword) {
        String password = new String(purePassword);

        String salt = BCrypt.gensalt(COST);
        return BCrypt.hashpw(password, salt);
    }


    public static boolean checkPassword(char[] purePassword, String databaseHash) {
        String password = new String(purePassword);
        try {
            return BCrypt.checkpw(password, databaseHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
