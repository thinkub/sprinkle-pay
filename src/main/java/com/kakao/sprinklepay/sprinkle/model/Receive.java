package com.kakao.sprinklepay.sprinkle.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/20.
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Receive {
    private String token;

    public static Receive of(String token) {
        return new Receive(token);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private long receivedAmount;

        public static Response of(long receivedAmount) {
            return new Response(receivedAmount);
        }
    }
}
