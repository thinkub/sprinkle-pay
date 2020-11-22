package com.kakao.sprinklepay.sprinkle.repository.redis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/22.
 */

@Getter
@RedisHash("sprinkle")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprinkleRedis implements Serializable {
    private static final long serialVersionUID = 1858831085310077363L;

    @Id
    private String id;
    private List<Long> sprinkleDetailIds;

    public static SprinkleRedis of(String id, List<Long> sprinkleDetailIds) {
        return new SprinkleRedis(id, sprinkleDetailIds);
    }

}
