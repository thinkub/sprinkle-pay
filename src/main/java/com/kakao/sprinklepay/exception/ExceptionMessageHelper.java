package com.kakao.sprinklepay.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/19.
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionMessageHelper {
    private final MessageSource messageSource;

    Result serverError(Exception e) {
        String[] args = {e.getMessage()};
        String message = getMessage("serverError.msg", args);
        log.error("Error Occurs! ", e);
        return Result.of(getMessage("serverError.code"), message);
    }

    Result distributeAmountError() {
        String message = getMessage("distributeAmountError.msg");
        log.error("DistributeAmountException: {}", message);
        return Result.of(getMessage("distributeAmountError.code"), message);
    }

    Result alreadyReceivedError() {
        String message = getMessage("alreadyReceivedError.msg");
        log.error("AlreadyReceivedException: {}", message);
        return Result.of(getMessage("alreadyReceivedError.code"), message);
    }

    Result noRemainPayReceiveError() {
        String message = getMessage("noRemainPayReceiveError.msg");
        log.error("NoRemainPayReceivedException: {}", message);
        return Result.of(getMessage("noRemainPayReceiveError.code"), message);
    }

    Result notSameRoomExceptionError() {
        String message = getMessage("notSameRoomExceptionError.msg");
        log.error("NotSameRoomException: {}", message);
        return Result.of(getMessage("notSameRoomExceptionError.code"), message);
    }

    Result receiveValidTimeError() {
        String message = getMessage("receiveValidTimeError.msg");
        log.error("ReceiveValidTimeException: {}", message);
        return Result.of(getMessage("receiveValidTimeError.code"), message);
    }

    Result sprinklePayNotFoundError() {
        String message = getMessage("sprinklePayNotFoundError.msg");
        log.error("SprinklePayNotFoundException: {}", message);
        return Result.of(getMessage("sprinklePayNotFoundError.code"), message);
    }

    Result sprinkleUserNotReceiveError() {
        String message = getMessage("sprinkleUserNotReceiveError.msg");
        log.error("SprinkleUserNotReceiveException: {}", message);
        return Result.of(getMessage("sprinkleUserNotReceiveError.code"), message);
    }

    Result sprinklePaySearchValidError() {
        String message = getMessage("sprinklePaySearchValidDateError.msg");
        log.error("SprinklePaySearchValidException: {}", message);
        return Result.of(getMessage("sprinklePaySearchValidDateError.code"), message);
    }

    Result sprinklePayUserAccessDeniedError() {
        String message = getMessage("sprinklePayUserAccessDenied.msg");
        log.error("SprinklePayUserAccessDeniedException: {}", message);
        return Result.of(getMessage("sprinklePayUserAccessDenied.code"), message);
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, String[] strings) {
        return messageSource.getMessage(code, strings, LocaleContextHolder.getLocale());
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    static class Result {

        private String code;
        private String message;

        private static Result of(String code, String message) {
            return Result.builder().code(code).message(message).build();
        }
    }
}
