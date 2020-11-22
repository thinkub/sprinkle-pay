package com.kakao.sprinklepay.sprinkle.repository;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */
public interface SprinkleDetailRepository extends JpaRepository<SprinkleDetailEntity, Long> {
    Optional<SprinkleDetailEntity> findBySprinkleAndReceiveUserId(SprinkleEntity sprinkle, Long receiveUserId);
}
