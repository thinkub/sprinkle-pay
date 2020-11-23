package com.kakao.sprinklepay.exception;

import com.kakao.sprinklepay.sprinkle.exception.CustomException;
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

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionMessageHelper.Result> customException(CustomException e) {
        return new ResponseEntity<>(messageHelper.getExceptionMessage(e.getCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
