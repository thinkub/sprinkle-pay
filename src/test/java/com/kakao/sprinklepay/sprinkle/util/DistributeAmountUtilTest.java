package com.kakao.sprinklepay.sprinkle.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("인원 수가 1명인 경우")
    void distributeAmountTargetOne() {
        // given
        long amount = 1000;
        int targetCount = 1;

        // when
        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(amount, targetCount);

        // then
        assertAll(
                () -> assertThat(distributeAmount.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmount.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );
    }

    @Test
    @DisplayName("인원 수 보다 금액이 작은 경우")
    void distributeAmountException() {
        // given
        long amount = 10;
        int targetCount = 100;

        // when
        assertThrows(RuntimeException.class, () -> DistributeAmountUtil.distributeAmount(amount, targetCount));
    }

    @Test
    @DisplayName("인원 수가 많은 경우")
    void distributeAmountTargetMany() {
        // given
        long amount = 1000;
        int targetCount = 900;

        // when
        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(amount, targetCount);

        // then
        assertAll(
                () -> assertThat(distributeAmount.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmount.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );
    }

    @Test
    @DisplayName("인원 수가 금액과 같은 경우 - 모든 사람이 1원을 분배 받아야 한다.")
    void distributeAmountTargetCountSame() {
        // given
        long amount = 1000;
        int targetCount = 1000;

        // when
        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(amount, targetCount);

        // then
        assertAll(
                () -> assertThat(distributeAmount.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmount.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount),
                () -> assertThat(distributeAmount.stream().allMatch(a -> a == 1)).isTrue()
        );
    }
}