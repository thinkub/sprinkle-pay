package com.kakao.sprinklepay.sprinkle.controller;

import com.kakao.sprinklepay.sprinkle.model.Receive;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import com.kakao.sprinklepay.sprinkle.service.SprinklePayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/18.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/sprinkle")
public class SprinklePayController {
    private static final String X_USER_ID = "X-USER-ID";
    private static final String X_ROOM_ID = "X-ROOM-ID";
    private final SprinklePayService sprinklePayService;

    @PostMapping
    public ResponseEntity<Sprinkle.Response> sprinklePay(@RequestHeader(X_USER_ID) Long userId,
                                                         @RequestHeader(X_ROOM_ID) String roomId,
                                                         @RequestBody Sprinkle.Request request) {
        UserInfo userInfo = UserInfo.of(userId, roomId);
        Sprinkle.Response response = sprinklePayService.sprinklePay(request, userInfo);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{token}")
    public ResponseEntity<Receive.Response> receivedPay(@RequestHeader(X_USER_ID) Long userId,
                                                        @RequestHeader(X_ROOM_ID) String roomId,
                                                        @PathVariable String token) {
        UserInfo userInfo = UserInfo.of(userId, roomId);
        Receive receive = Receive.of(token);
        Receive.Response response = sprinklePayService.receivePay(receive, userInfo);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{token}")
    public ResponseEntity<Sprinkle> getSprinklePay(@RequestHeader(X_USER_ID) Long userId,
                                                   @RequestHeader(X_ROOM_ID) String roomId,
                                                   @PathVariable String token) {
        UserInfo userInfo = UserInfo.of(userId, roomId);
        Sprinkle sprinkle = sprinklePayService.getSprinklePay(token, userInfo);
        return ResponseEntity.ok().body(sprinkle);
    }
}
