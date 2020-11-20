package com.kakao.sprinklepay.sprinkle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Getter
@Builder
public class Sprinkle {
    @NonNull
    private long amount;
    @NonNull
    private int targetCount;
}
