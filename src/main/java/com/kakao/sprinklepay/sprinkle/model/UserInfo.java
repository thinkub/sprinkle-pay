package com.kakao.sprinklepay.sprinkle.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {
    private Long userId;
    private String roomId;

    public static UserInfo of(Long userId, String roomId) {
        return new UserInfo(userId, roomId);
    }
}
