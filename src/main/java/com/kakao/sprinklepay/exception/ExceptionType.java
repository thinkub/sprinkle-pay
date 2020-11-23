package com.kakao.sprinklepay.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/23.
 */

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    SERVER_ERROR("-9999", "serverError.msg"),
    DISTRIBUTE_AMOUNT_ERROR("-1000", "distributeAmountError.msg"),
    ALREADY_RECEIVED_ERROR("-1001", "alreadyReceivedError.msg"),
    NO_REMAIN_PAY_RECEIVE_ERROR("-1002", "notSameRoomExceptionError.msg"),
    NOT_SAME_ROOM_ERROR("-1003", "notSameRoomError.msg"),
    RECEIVE_VALID_TIME_ERROR("-1004", "receiveValidTimeError.msg"),
    SPRINKLE_PAY_NOT_FOUND_ERROR("-1005", "sprinklePayNotFoundError.msg"),
    SPRINKLE_USER_NOT_RECEIVE_ERROR("-1006", "sprinkleUserNotReceiveError.msg"),
    SPRINKLE_PAY_SEARCH_VALID_DATE_ERROR("-1007", "sprinklePaySearchValidDateError.msg"),
    SPRINKLE_PAY_USER_ACCESS_DENIED("-1008", "sprinklePayUserAccessDenied.msg");

    private static final Map<String, ExceptionType> enumMap = new HashMap<>(values().length);

    static {
        Stream.of(values()).forEach(v -> enumMap.put(v.getCode(), v));
    }

    private final String code;
    private final String message;

    public static ExceptionType codeOf(String code) {
        return Optional.ofNullable(enumMap.get(code)).orElseThrow(IllegalArgumentException::new);
    }
}
