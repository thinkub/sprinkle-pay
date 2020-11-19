package com.kakao.sprinklepay.sprinkle.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
public class DistributeAmountUtil {
    public static List<Long> distributeAmount(long amount, int targetCount) {
        if (targetCount == 1) {
            return Collections.singletonList(amount);
        }
        if (targetCount > amount) {
            throw new RuntimeException();
        }

        List<Long> results = new ArrayList<>(targetCount);
        long minAmount = 1L;
        long randomAmount;
        long remainAmount = amount - targetCount;
        long cumulativeAmount = 0L;
        for (int i = 0; i < targetCount; i++) {
            if (i == targetCount - 1) {
                results.add(remainAmount < 0L ? minAmount : amount - cumulativeAmount);
                break;
            }
            randomAmount = (long) (Math.random() * remainAmount + 1);
            remainAmount = remainAmount - randomAmount;
            randomAmount = i == 0 ? randomAmount : randomAmount + minAmount;
            results.add(randomAmount);
            cumulativeAmount = cumulativeAmount + randomAmount;
        }
        return results;
    }
}
