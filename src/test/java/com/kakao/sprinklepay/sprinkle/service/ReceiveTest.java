package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.exception.ExceptionType;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.CustomException;
import com.kakao.sprinklepay.sprinkle.model.Receive;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleDetailRepository;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/20.
 */

@ExtendWith(SpringExtension.class)
class ReceiveTest {
    private static final String ROOM_ID = "ROOM1";
    private static final Long USER_ID = 1L;
    private static final Long RECEIVED_USER_ID = 2L;
    private static final String TOKEN = "aaaa-bbbb-cccc";

    @InjectMocks
    private SprinklePayService service;
    @Mock
    private SprinkleRepository sprinkleRepository;
    @Mock
    private SprinkleDetailRepository sprinkleDetailRepository;

    @Test
    @DisplayName("뿌린 페이 받기")
    void 받기() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity();
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of(TOKEN);
        UserInfo receiveUserInfo = UserInfo.of(RECEIVED_USER_ID, ROOM_ID);

        // when
        Receive.Response response = service.receivePay(receive, receiveUserInfo);

        // then
        assertThat(response.getReceivedAmount()).isEqualTo(1000);
    }


    @Test
    @DisplayName("뿌린 페이 받기 - 방 아이디가 다른경우")
    void 받기_다른방아이디_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity();
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of("aaa-aaa-aaa");
        UserInfo receiveUserInfo = UserInfo.of(RECEIVED_USER_ID, "ROOM2");

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.receivePay(receive, receiveUserInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.NOT_SAME_ROOM_ERROR.name());
    }

    @Test
    @DisplayName("뿌린 페이 받기 - 뿌린 사용자가 받기를 하는 경우")
    void 받기_자신이뿌린건받기_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity();
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of(TOKEN);
        UserInfo receiveUserInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.receivePay(receive, receiveUserInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.SPRINKLE_USER_NOT_RECEIVE_ERROR.name());
    }

    @Test
    @DisplayName("뿌린 페이 받기 - 유효시간인 10분이 지난 경우")
    void 받기_유효시간경과_Exception() {
        // given
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(1)
                .amount(1000)
                .registerDatetime(LocalDateTime.now().minusMinutes(10))
                .userId(USER_ID)
                .build();

        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of(TOKEN);
        UserInfo receiveUserInfo = UserInfo.of(RECEIVED_USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.receivePay(receive, receiveUserInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.RECEIVE_VALID_TIME_ERROR.name());
    }


    @Test
    @DisplayName("뿌린 페이 받기 - 이미 받은 사용자")
    void 받기_이미받은사용자_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity();
        SprinkleDetailEntity detailEntity = SprinkleDetailEntity.builder()
                .sprinkle(entity)
                .amount(1000)
                .receiveUserId(RECEIVED_USER_ID)
                .build();

        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.of(detailEntity));
        Receive receive = Receive.of(TOKEN);
        UserInfo receiveUserInfo = UserInfo.of(RECEIVED_USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.receivePay(receive, receiveUserInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.ALREADY_RECEIVED_ERROR.name());
    }

    @Test
    @DisplayName("뿌린 페이 받기 - 더이상 받을 페이가 없을때")
    void 받기_받을페이가없는_Exception() {
        // given
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(1)
                .amount(1000)
                .totalReceivedAmount(1000)
                .totalReceivedCount(1)
                .registerDatetime(LocalDateTime.now())
                .userId(USER_ID)
                .build();

        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of(TOKEN);
        UserInfo receiveUserInfo = UserInfo.of(3L, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.receivePay(receive, receiveUserInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.NO_REMAIN_PAY_RECEIVE_ERROR.name());
    }

    /**
     * 이 TestCase는 정상 동작이 될 경우도 있지만 근본적으로 로직에 대한 정상 테스트가 불가능
     * 그 이유는 받기기능 동작 시 동시에 많은 사용자가 받기(thread) 호출 시 thread safe하도록 DB에 비관적락 (select for update) 을 걸었으나
     * TestCase 자체가 DB의 Dependency를 가지지 않도록 Mock을 사용 하였기 때문에 비관적 락에 대한 기능이 동작 하지 않음.
     * <p>
     * 대신, 여러 사용자에 대한 test 진행은 bash shell에 curl을 이용하여 검증 완료
     * <p>
     * ex) 100 명의 사용자가 받기 api를 호출하는 경우에 대한 shell
     * <p>
     * #!/bin/bash
     * <p>
     * if [ $# -ne 1 ]; then
     * echo "Token 값 입력 필수"
     * exit -1
     * fi
     * <p>
     * SET=$(seq 2 101)
     * for i in $SET
     * do
     * curl 'http://localhost:8081/sprinkle/'$1 -i -X PATCH -H 'Content-Type: application/json;charset=UTF-8' -H 'Accept: application/json' -H 'X-USER-ID: '$i -H 'X-ROOM-ID: ROOM1' &
     * done
     */
    @Test
    @DisplayName("뿌린 페이 받기 - 동시에 여러 사용자가 받는 경우")
    void 받기_동시_여러사용자() {
        // given
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(100)
                .amount(10000)
                .registerDatetime(LocalDateTime.now())
                .userId(USER_ID)
                .build();
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        when(sprinkleDetailRepository.findBySprinkleAndReceiveUserId(any(), any())).thenReturn(Optional.empty());
        Receive receive = Receive.of(TOKEN);
        List<Long> receiveUserIds = LongStream.rangeClosed(2, 101).boxed().collect(toList());

        // when
        List<Receive.Response> responses = receiveUserIds.parallelStream().map(u -> {
            UserInfo receiveUserInfo = UserInfo.of(u, ROOM_ID);
            return service.receivePay(receive, receiveUserInfo);
        }).collect(toList());

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(100),
                () -> assertThat(responses.stream().mapToLong(Receive.Response::getReceivedAmount).sum()).isEqualTo(10000)
        );

    }

    private SprinkleEntity makeMockSprinkleEntity() {
        return SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(1)
                .amount(1000)
                .registerDatetime(LocalDateTime.now())
                .userId(USER_ID)
                .build();
    }
}