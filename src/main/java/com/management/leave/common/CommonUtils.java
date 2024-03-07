package com.management.leave.common;

import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

/** some common utils
 * @author zh
 */
public class CommonUtils {
    /**
     * Randomly generate a string of custom content
     *
     * @param length The length of the string that needs to be generated
     * @param s      The character pool from which the string is generated
     * @return String
     */
    public static String getRandomStr(int length, String s) {

        String base = "";

        if (StringUtils.isEmpty(s)) {
            base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        } else {
            base = s;
        }

        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();

    }
}
