package com.kakao.sprinklepay.sprinkle.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
class TokenProviderTest {

    @Test
    @DisplayName("Token 생성 테스트")
    void getToken() {
        // given

        // then
        String token = TokenProvider.getToken();

        // then
        assertAll("Token은 14자리로 생성", () -> assertThat(token.length()).isEqualTo(14));
    }
}