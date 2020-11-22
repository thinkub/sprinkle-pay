package com.kakao.sprinklepay.sprinkle.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
class DistributeAmountUtilTest {

    @Test
    @DisplayName("금액 인원 수 별로 랜덤하게 분배하기 테스트")
    void 인원수별_금액_랜덤분배() {
        // given
        long amount = 1000;
        long remainAmount = 1000;
        int targetCount = 5;
        int remainCount = 5;

        // when
        List<Long> distributeAmounts = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
            long distributeAmount = DistributeAmountUtil.distributeAmount(targetCount, remainCount, remainAmount);
            remainAmount = remainAmount - distributeAmount;
            distributeAmounts.add(distributeAmount);
            remainCount--;
        }

        // then
        assertAll(
                () -> assertThat(distributeAmounts.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmounts.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );
    }

    @Test
    @DisplayName("인원 수가 1명인 경우")
    void 인원수별_금액_랜덤분배_1명() {
        // given
        long amount = 1000;
        long remainAmount = 1000;
        int targetCount = 1;
        int remainCount = 1;

        // when
        List<Long> distributeAmounts = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
            long distributeAmount = DistributeAmountUtil.distributeAmount(targetCount, remainCount, remainAmount);
            remainAmount = remainAmount - distributeAmount;
            distributeAmounts.add(distributeAmount);
            remainCount--;
        }

        // then
        assertAll(
                () -> assertThat(distributeAmounts.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmounts.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );
    }

    @Test
    @DisplayName("인원 수가 많은 경우")
    void 인원수별_금액_랜덤분배_인원수가많은경우() {
        // given
        long amount = 1000;
        long remainAmount = 1000;
        int targetCount = 900;
        int remainCount = 900;

        // when
        List<Long> distributeAmounts = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
            long distributeAmount = DistributeAmountUtil.distributeAmount(targetCount, remainCount, remainAmount);
            remainAmount = remainAmount - distributeAmount;
            distributeAmounts.add(distributeAmount);
            remainCount--;
        }

        // then
        assertAll(
                () -> assertThat(distributeAmounts.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmounts.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount)
        );
    }

    @Test
    @DisplayName("인원 수가 금액과 같은 경우 - 모든 사람이 1원을 분배 받아야 한다.")
    void 인원수별_금액_랜덤분배_인원수와금액이같은경우() {
        // given
        long amount = 1000;
        long remainAmount = 1000;
        int targetCount = 1000;
        int remainCount = 1000;

        // when
        List<Long> distributeAmounts = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
            long distributeAmount = DistributeAmountUtil.distributeAmount(targetCount, remainCount, remainAmount);
            remainAmount = remainAmount - distributeAmount;
            distributeAmounts.add(distributeAmount);
            remainCount--;
        }

        // then
        assertAll(
                () -> assertThat(distributeAmounts.size()).isEqualTo(targetCount),
                () -> assertThat(distributeAmounts.stream().mapToLong(Long::longValue).sum()).isEqualTo(amount),
                () -> assertThat(distributeAmounts.stream().allMatch(d -> d == 1)).isTrue()
        );
    }
}