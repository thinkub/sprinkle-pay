package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.exception.ExceptionType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Sprinkle.Request request = Sprinkle.Request.builder().amount(1000).targetCount(1).build();
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Sprinkle.Response response = service.sprinklePay(request, userInfo);

        // then
        assertThat(response.getToken()).isNotEmpty();
    }

    @Test
    @DisplayName("뿌리기 - 금액이 인원수가 적은 경우")
    void 뿌리기_금액이인원수보다적은_Exception() {
        // given
        Sprinkle.Request request = Sprinkle.Request.builder().amount(10).targetCount(11).build();
        UserInfo userInfo = UserInfo.of(USER_ID, ROOM_ID);

        // when
        Throwable customException = assertThrows(CustomException.class, () -> service.sprinklePay(request, userInfo));
        assertThat(customException.getMessage()).isEqualTo(ExceptionType.DISTRIBUTE_AMOUNT_ERROR.name());
    }
}
