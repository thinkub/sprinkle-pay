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
        List<Long> results = new ArrayList<>(targetCount);
        long randomAmount;
        for (int i = 0; i < targetCount; i++) {
            if (i == targetCount - 1) {
                results.add(amount);
                break;
            }
            randomAmount = (long) (Math.random() * amount + 1);
            amount = amount - randomAmount;
            results.add(randomAmount);
        }
        return results;
    }
}
