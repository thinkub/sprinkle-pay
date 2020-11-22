package com.kakao.sprinklepay.sprinkle.repository.redis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/22.
 */

@Getter
@RedisHash("sprinkle")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprinkleDetailRedis implements Serializable {
    private static final long serialVersionUID = 3511300767213554499L;

    @Id
    private String id;
    private Long sprinkleDetailId;
    @Setter
    private boolean received;

    public static SprinkleDetailRedis of(String id, Long sprinkleDetailId) {
        return new SprinkleDetailRedis(id, sprinkleDetailId, false);
    }
}
