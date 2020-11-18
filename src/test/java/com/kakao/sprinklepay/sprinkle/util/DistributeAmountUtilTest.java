package com.kakao.sprinklepay.sprinkle.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
class DistributeAmountUtilTest {

    @Test
    @DisplayName("금액 인원 수 별로 랜덤하게 분배하기 테스트")
    void distributeAmount() {
        // given
        long amount = 1000;
        int targetCount = 5;

        // when
        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(amount, targetCount);


        // then
        assertAll(
                () -> assertThat(distributeAmount.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmount.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );

    }
}