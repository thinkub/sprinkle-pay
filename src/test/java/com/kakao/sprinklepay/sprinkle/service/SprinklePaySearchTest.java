package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.exception.ExceptionType;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.CustomException;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/21.
 */

@ExtendWith(SpringExtension.class)
class SprinklePaySearchTest {
    private static final String ROOM_ID = "ROOM1";
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "aaaa-bbbb-cccc";

    @InjectMocks
    private SprinklePayService service;
    @Mock
    private SprinkleRepository sprinkleRepository;

    @Test
    @DisplayName("뿌리기 조회 - 정상")
    void 뿌리기조회() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now());
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Sprinkle sprinkle = service.getSprinklePay(TOKEN, userInfo);

        // then
        assertAll(
                () -> assertThat(sprinkle.getDetails().size()).isEqualTo(1),
                () -> assertThat(sprinkle.getDetails().stream().mapToLong(Sprinkle.Detail::getReceivedAmount).sum()).isEqualTo(1000)
        );
    }

    @Test
    @DisplayName("뿌리기 조회 - 뿌린 사용자와 조회 사용자가 다른 경우")
    void 뿌리기조회_사용자다른경우_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now());
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        UserInfo userInfo = UserInfo.of(2L, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.getSprinklePay(TOKEN, userInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.SPRINKLE_PAY_USER_ACCESS_DENIED.name());
    }

    @Test
    @DisplayName("뿌리기 조회 - 뿌리기 7일 지난 건")
    void 뿌리기조회_조회기간지난경우_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now().minusDays(7));
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.getSprinklePay(TOKEN, userInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.SPRINKLE_PAY_SEARCH_VALID_DATE_ERROR.name());
    }

    @Test
    @DisplayName("뿌리기 조회 - token 정보가 잘못된 경우")
    void 뿌리기조회_TOKEN정보오류_Exception() {
        // given
        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now());
        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.ofNullable(null));
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.getSprinklePay(TOKEN, userInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.SPRINKLE_PAY_NOT_FOUND_ERROR.name());
    }

    private SprinkleEntity makeMockSprinkleEntity(LocalDateTime registerDatetime) {
        SprinkleEntity entity = SprinkleEntity.builder()
                .sprinkleId(1L)
                .roomId(ROOM_ID)
                .token(TOKEN)
                .targetCount(1)
                .amount(1000)
                .registerDatetime(registerDatetime)
                .userId(USER_ID)
                .sprinkleDetails(new ArrayList<>(1))
                .build();
        List<SprinkleDetailEntity> detailEntities = Collections.singletonList(SprinkleDetailEntity.create(entity, 2L, 1000L));
        entity.getSprinkleDetails().addAll(detailEntities);
        return entity;
    }
}
