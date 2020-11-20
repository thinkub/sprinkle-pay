package com.kakao.sprinklepay.exception;

import com.kakao.sprinklepay.sprinkle.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/19.
 */

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvisor {
    private final ExceptionMessageHelper messageHelper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessageHelper.Result> handleException(Exception e) {
        return new ResponseEntity<>(messageHelper.serverError(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DistributeAmountException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> distributeAmountException() {
        return new ResponseEntity<>(messageHelper.distributeAmountError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyReceivedException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> alreadyReceivedException() {
        return new ResponseEntity<>(messageHelper.alreadyReceivedError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoRemainPayReceivedException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> noRemainPayReceivedException() {
        return new ResponseEntity<>(messageHelper.noRemainPayReceiveError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotSameRoomException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> notSameRoomException() {
        return new ResponseEntity<>(messageHelper.notSameRoomExceptionError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReceiveValidTimeException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> receiveValidTimeException() {
        return new ResponseEntity<>(messageHelper.receiveValidTimeError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SprinklePayNotFoundException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> sprinklePayNotFoundException() {
        return new ResponseEntity<>(messageHelper.sprinklePayNotFoundError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SprinkleUserNotReceiveException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> sprinkleUserNotReceiveException() {
        return new ResponseEntity<>(messageHelper.sprinkleUserNotReceiveError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
