package com.foodexpress.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    public static String hashPassword(String password) {
        try {
            // Get an instance of the SHA-256 MessageDigest
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Perform hashing
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occurred while hashing password", e);
        }
    }
}
