package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.sprinkle.repository.SprinkleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

//    @Test
//    @DisplayName("뿌리기 조회 - 정상")
//    void 뿌리기조회() {
//        // given
//        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now());
//        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
//        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);
//
//        // when
//        Sprinkle sprinkle = service.getSprinklePay(TOKEN, userInfo);
//
//        // then
//        assertAll(
//                () -> assertThat(sprinkle.getDetails().size()).isEqualTo(1),
//                () -> assertThat(sprinkle.getDetails().stream().mapToLong(Sprinkle.Detail::getReceivedAmount).sum()).isEqualTo(1000)
//        );
//    }

//    @Test
//    @DisplayName("뿌리기 조회 - 뿌린 사용자와 조회 사용자가 다른 경우")
//    void 뿌리기조회_사용자다른경우_Exception() {
//        // given
//        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now());
//        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
//        UserInfo userInfo = UserInfo.of(2L, ROOM_ID);
//
//        // when
//        assertThrows(SprinklePayUserAccessDeniedException.class, () -> service.getSprinklePay(TOKEN, userInfo));
//    }

//    @Test
//    @DisplayName("뿌리기 조회 - 뿌리기 7일 지난 건")
//    void 뿌리기조회_조회기간지난경우_Exception() {
//        // given
//        SprinkleEntity entity = makeMockSprinkleEntity(LocalDateTime.now().minusDays(7));
//        when(sprinkleRepository.findByToken(any())).thenReturn(Optional.of(entity));
//        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);
//
//        // when
//        assertThrows(SprinklePaySearchValidException.class, () -> service.getSprinklePay(TOKEN, userInfo));
//    }

//    private SprinkleEntity makeMockSprinkleEntity(LocalDateTime registerDatetime) {
//        SprinkleEntity entity = SprinkleEntity.builder()
//                .sprinkleId(1L)
//                .roomId(ROOM_ID)
//                .token(TOKEN)
//                .targetCount(1)
//                .amount(1000)
//                .registerDatetime(registerDatetime)
//                .userId(USER_ID)
//                .sprinkleDetails(new ArrayList<>(1))
//                .build();
//        List<SprinkleDetailEntity> detailEntities = Collections.singletonList(SprinkleDetailEntity.create(entity, 1000));
//        entity.getSprinkleDetails().addAll(detailEntities);
//        return entity;
//    }
}
