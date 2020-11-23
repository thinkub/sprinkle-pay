package com.kakao.sprinklepay.sprinkle.exception;

import com.kakao.sprinklepay.exception.ExceptionType;
import lombok.Getter;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/19.
 */

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -817760015319298877L;
    private String code;

    public CustomException() {
        super();
    }

    public CustomException(ExceptionType code) {
        super(code.name());
        this.code = code.getCode();
    }
}
