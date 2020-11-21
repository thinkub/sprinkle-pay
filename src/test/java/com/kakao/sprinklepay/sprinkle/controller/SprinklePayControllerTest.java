package com.kakao.sprinklepay.sprinkle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sprinklepay.exception.ExceptionAdvisor;
import com.kakao.sprinklepay.exception.ExceptionMessageHelper;
import com.kakao.sprinklepay.sprinkle.model.Receive;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.service.SprinklePayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.kakao.sprinklepay.sprinkle.ApiDocumentUtil.getDocumentRequest;
import static com.kakao.sprinklepay.sprinkle.ApiDocumentUtil.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/21.
 */

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(SprinklePayController.class)
@AutoConfigureRestDocs
class SprinklePayControllerTest {
    private static final String X_USER_ID = "X-USER-ID";
    private static final String X_ROOM_ID = "X-ROOM-ID";
    private static final String ROOM_ID = "ROOM1";
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "aaaa-bbbb-cccc";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SprinklePayService service;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ExceptionAdvisor exceptionAdvisor;
    @MockBean
    private ExceptionMessageHelper exceptionMessageHelper;

    @Test
    void sprinklePay() throws Exception {
        // given
        Sprinkle.Request request = Sprinkle.Request.builder().targetCount(1).amount(1000).build();
        Sprinkle.Response response = Sprinkle.Response.of(TOKEN);
        given(service.sprinklePay(any(), any())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sprinkle")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(X_USER_ID, USER_ID)
                .header(X_ROOM_ID, ROOM_ID)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document(
                        "sprinklePay",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(X_ROOM_ID).description("대화방 아이디"),
                                headerWithName(X_USER_ID).description("사용자 아이디")
                        ),
                        requestFields(
                                fieldWithPath("targetCount").type(JsonFieldType.NUMBER).description("뿌리기 대상 수"),
                                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("뿌리기 금액")
                        ),
                        responseFields(
                                fieldWithPath("token").type(JsonFieldType.STRING).description("뿌리기 토큰")
                        )
                ));
    }

    @Test
    void receivedPay() throws Exception {
        // given
        Receive.Response response = Receive.Response.of(1000);
        given(service.receivePay(any(), any())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.patch("/sprinkle/{token}", TOKEN)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(X_USER_ID, USER_ID)
                .header(X_ROOM_ID, ROOM_ID))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document(
                        "sprinklePay-Receive",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(X_ROOM_ID).description("대화방 아이디"),
                                headerWithName(X_USER_ID).description("사용자 아이디")
                        ),
                        pathParameters(parameterWithName("token").description("뿌리기 토큰")),
                        responseFields(
                                fieldWithPath("receivedAmount").type(JsonFieldType.NUMBER).description("받는 금액")
                        )
                ));
    }

    @Test
    void getSprinklePay() throws Exception {
        // given
        Sprinkle.Detail detail1 = Sprinkle.Detail.builder()
                .receivedAmount(900)
                .receivedUserId(2L)
                .build();
        Sprinkle.Detail detail2 = Sprinkle.Detail.builder()
                .receivedAmount(100)
                .receivedUserId(null)
                .build();
        Sprinkle response = Sprinkle.builder()
                .receivedAmount(900)
                .sprinkleAmount(1000)
                .sprinkleDatetime(LocalDateTime.now())
                .details(Arrays.asList(detail1, detail2))
                .build();
        given(service.getSprinklePay(any(), any())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/sprinkle/{token}", TOKEN)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(X_USER_ID, USER_ID)
                .header(X_ROOM_ID, ROOM_ID))
                .andDo(MockMvcResultHandlers.print());

        // then
        // then
        resultActions.andExpect(status().isOk())
                .andDo(document(
                        "sprinklePay-Get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName(X_ROOM_ID).description("대화방 아이디"),
                                headerWithName(X_USER_ID).description("사용자 아이디")
                        ),
                        pathParameters(parameterWithName("token").description("뿌리기 토큰")),
                        responseFields(
                                fieldWithPath("sprinkleDatetime").type(JsonFieldType.STRING).description("뿌린 시간"),
                                fieldWithPath("sprinkleAmount").type(JsonFieldType.NUMBER).description("뿌린 금액"),
                                fieldWithPath("receivedAmount").type(JsonFieldType.NUMBER).description("받기 완료된 금액"),
                                fieldWithPath("details[].receivedAmount").type(JsonFieldType.NUMBER).description("받은 금액"),
                                fieldWithPath("details[].receivedUserId").type(JsonFieldType.NUMBER).description("받은 사용자 아이디").optional()
                        )
                ));

    }
}