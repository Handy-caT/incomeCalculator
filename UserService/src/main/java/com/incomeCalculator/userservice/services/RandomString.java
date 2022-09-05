package com.incomeCalculator.userservice.services;

import java.security.SecureRandom;

public class RandomString {

    public final static SecureRandom random = new SecureRandom();
    public final static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String lower = "abcdefghijklmnopqrstuvwxyz";
    public final static String digits = "0123456789";
    public final static String alphanum = upper + lower + digits;

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphanum.charAt(random.nextInt(alphanum.length())));
        }
        return sb.toString();
    }

}
