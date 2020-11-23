package com.kakao.sprinklepay.sprinkle.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kakao.sprinklepay.exception.ExceptionType;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.CustomException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Getter
@Builder
public class Sprinkle {
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sprinkleDatetime;
    private long sprinkleAmount;
    private long receivedAmount;
    private List<Detail> details;

    public static Sprinkle ofEntity(SprinkleEntity entity) {
        return Sprinkle.builder()
                .sprinkleDatetime(entity.getRegisterDatetime())
                .sprinkleAmount(entity.getAmount())
                .receivedAmount(entity.getTotalReceivedAmount())
                .details(entity.getSprinkleDetails().stream().map(Detail::ofEntity).collect(toList()))
                .build();
    }

    @Getter
    @Builder
    public static class Request {
        private long amount;
        private int targetCount;

        public void validRequest() {
            if (amount < targetCount) {
                throw new CustomException(ExceptionType.DISTRIBUTE_AMOUNT_ERROR);
            }
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private String token;

        public static Response of(String token) {
            return new Response(token);
        }
    }

    @Getter
    @Builder
    public static class Detail {
        private long receivedAmount;
        private Long receivedUserId;

        public static Detail ofEntity(SprinkleDetailEntity entity) {
            return Detail.builder()
                    .receivedAmount(entity.getAmount())
                    .receivedUserId(entity.getReceiveUserId())
                    .build();
        }
    }
}
