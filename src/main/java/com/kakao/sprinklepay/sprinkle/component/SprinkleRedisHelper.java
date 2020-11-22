package com.kakao.sprinklepay.sprinkle.component;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.exception.NoRemainPayReceivedException;
import com.kakao.sprinklepay.sprinkle.exception.SprinklePayNotFoundException;
import com.kakao.sprinklepay.sprinkle.repository.redis.SprinkleDetailRedis;
import com.kakao.sprinklepay.sprinkle.repository.redis.SprinkleRedis;
import com.kakao.sprinklepay.sprinkle.repository.redis.SprinkleRedisDetailRepository;
import com.kakao.sprinklepay.sprinkle.repository.redis.SprinkleRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/22.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SprinkleRedisHelper {
    private static final String SPRINKLE_REDIS_KEY = "SPRINKLE";
    private static final String SPRINKLE_DETAIL_REDIS_KEY = "SPRINKLE_DETAIL";

    private final SprinkleRedisRepository redisRepository;
    private final SprinkleRedisDetailRepository redisDetailRepository;

    public void saveSprinkle(List<SprinkleDetailEntity> details, String token) {
        List<Long> detailIds = details.stream().map(SprinkleDetailEntity::getSprinkleDetailId).collect(toList());
        SprinkleRedis sprinkle = SprinkleRedis.of(getSprinkleKey(token), detailIds);
        redisRepository.save(sprinkle);

        List<SprinkleDetailRedis> detailRedis = details.stream()
                .map(d -> SprinkleDetailRedis.of(getSprinkleDetailKey(d.getSprinkleDetailId()), d.getSprinkleDetailId()))
                .collect(toList());
        redisDetailRepository.saveAll(detailRedis);
    }

    public Long getValidSprinkleDetail(String token) {
        SprinkleRedis sprinkleRedis = redisRepository.findById(getSprinkleKey(token)).orElseThrow(SprinklePayNotFoundException::new);
        List<Long> detailIds = sprinkleRedis.getSprinkleDetailIds();

        for (Long detailId : detailIds) {
            Optional<SprinkleDetailRedis> detailOptional = redisDetailRepository.findById(getSprinkleDetailKey(detailId));
            if (detailOptional.isEmpty()) {
                continue;
            }
            SprinkleDetailRedis detailRedis = detailOptional.get();
            if (!detailRedis.isReceived()) {
                log.info(" ================= : {}", detailRedis.getSprinkleDetailId());
                detailRedis.setReceived(true);
                redisDetailRepository.save(detailRedis);
                return detailRedis.getSprinkleDetailId();
            }
        }
        throw new NoRemainPayReceivedException();
    }

    private String getSprinkleKey(String token) {
        return SPRINKLE_REDIS_KEY + "_" + token;
    }

    private String getSprinkleDetailKey(Long sprinkleDetailId) {
        return SPRINKLE_DETAIL_REDIS_KEY + "_" + sprinkleDetailId;
    }
}
