package com.kakao.sprinklepay.sprinkle.util;

import java.util.Random;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
public class TokenProvider {
    public static String getToken() {
        return String.join("-", makeRandomString(), makeRandomString(), makeRandomString());
    }

    private static String makeRandomString() {
        int leftLimit = 48;
        int rightLimit = 123;
        int targetStringLength = 4;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
