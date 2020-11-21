package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.DistributeAmountException;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/21.
 */

@ExtendWith(SpringExtension.class)
class SprinklePayTest {
    private static final String ROOM_ID = "ROOM1";
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "aaaa-bbbb-cccc";

    @InjectMocks
    private SprinklePayService service;
    @Mock
    private SprinkleRepository sprinkleRepository;

    @Test
    @DisplayName("뿌린기 - 정상")
    void 뿌리기() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity();
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        Sprinkle.Request request = Sprinkle.Request.builder().amount(1000).targetCount(1).build();
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        String token = service.sprinklePay(request, userInfo);

        // then
        assertThat(token).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("뿌리기 - 금액이 인원수가 적은 경우")
    void 뿌리기_금액이인원수보다적은_Exception() {
        // given
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(11)
                .amount(10)
                .registerDatetime(LocalDateTime.now())
                .userId(USER_ID)
                .sprinkleDetails(new ArrayList<>(1))
                .build();
        List<SprinkleDetailEntity> detailEntities = Collections.singletonList(SprinkleDetailEntity.create(entity, 10));
        entity.getSprinkleDetails().addAll(detailEntities);
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        Sprinkle.Request request = Sprinkle.Request.builder().amount(10).targetCount(11).build();
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        assertThrows(DistributeAmountException.class, () -> service.sprinklePay(request, userInfo));
    }


    private SprinkleEntity makeMockSprinkleEntity() {
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(1)
                .amount(1000)
                .registerDatetime(LocalDateTime.now())
                .userId(USER_ID)
                .sprinkleDetails(new ArrayList<>(1))
                .build();
        List<SprinkleDetailEntity> detailEntities = Collections.singletonList(SprinkleDetailEntity.create(entity, 1000));
        entity.getSprinkleDetails().addAll(detailEntities);
        return entity;
    }
}
