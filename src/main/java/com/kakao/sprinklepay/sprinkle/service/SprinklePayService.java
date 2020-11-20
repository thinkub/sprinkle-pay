package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleRepository;
import com.kakao.sprinklepay.sprinkle.util.DistributeAmountUtil;
import com.kakao.sprinklepay.sprinkle.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Service
@RequiredArgsConstructor
public class SprinklePayService {
    private final SprinkleRepository sprinkleRepository;

    @Transactional
    public String sprinklePay(Sprinkle sprinkle, UserInfo userInfo) {
        String token = TokenProvider.getToken();
        SprinkleEntity sprinkleEntity = SprinkleEntity.create(userInfo, sprinkle, token);
        sprinkleRepository.save(sprinkleEntity);

        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(sprinkle.getAmount(), sprinkle.getTargetCount());
        List<SprinkleDetailEntity> detailEntities = distributeAmount.stream()
                .map(d -> SprinkleDetailEntity.create(sprinkleEntity, d))
                .collect(Collectors.toList());
        sprinkleEntity.getSprinkleDetails().addAll(detailEntities);

        return token;
    }
}