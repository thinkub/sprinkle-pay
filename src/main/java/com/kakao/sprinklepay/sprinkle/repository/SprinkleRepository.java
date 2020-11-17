package com.kakao.sprinklepay.sprinkle.repository;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */
public interface SprinkleRepository extends JpaRepository<SprinkleEntity, Long> {
}
