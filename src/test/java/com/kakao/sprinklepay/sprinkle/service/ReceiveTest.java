package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.*;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThrows(NotSameRoomException.class, () -> service.receivePay(receive, receiveUserInfo));
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
        assertThrows(SprinkleUserNotReceiveException.class, () -> service.receivePay(receive, receiveUserInfo));
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
        assertThrows(ReceiveValidTimeException.class, () -> service.receivePay(receive, receiveUserInfo));
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
        assertThrows(AlreadyReceivedException.class, () -> service.receivePay(receive, receiveUserInfo));
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
        assertThrows(NoRemainPayReceivedException.class, () -> service.receivePay(receive, receiveUserInfo));
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