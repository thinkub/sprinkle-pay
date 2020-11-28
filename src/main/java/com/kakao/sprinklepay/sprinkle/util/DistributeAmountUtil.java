package com.kakao.sprinklepay.sprinkle.util;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */
public class DistributeAmountUtil {
    public static long distributeAmount(int targetCount, int remainCount, long remainAmount) {
        if (targetCount == 1) {
            return remainAmount;
        }
        if (remainCount == 1) {
            return remainAmount;
        }
        remainAmount = remainAmount - remainCount;
        if (remainAmount == 0) {
            return 1;
        }
        long randomAmount = (long) (Math.random() * (remainAmount / remainCount) + 1);
        return randomAmount + 1L;
    }
}
