package com.kakao.sprinklepay.sprinkle.controller;

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
    public ResponseEntity<?> sprinklePay(@RequestHeader(X_USER_ID) Long userId, @RequestHeader(X_ROOM_ID) String roomId,
                                         @RequestBody Sprinkle request) {
        UserInfo userInfo = UserInfo.of(userId, roomId);
        String token = sprinklePayService.sprinklePay(request, userInfo);
        return ResponseEntity.ok().body(token);
    }
}
