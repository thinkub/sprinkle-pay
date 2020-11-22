package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.sprinkle.component.SprinkleRedisHelper;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.SprinklePayNotFoundException;
import com.kakao.sprinklepay.sprinkle.model.Receive;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleDetailRepository;
import com.kakao.sprinklepay.sprinkle.repository.SprinkleRepository;
import com.kakao.sprinklepay.sprinkle.util.DistributeAmountUtil;
import com.kakao.sprinklepay.sprinkle.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Service
@RequiredArgsConstructor
public class SprinklePayService {
    private final SprinkleRepository sprinkleRepository;
    private final SprinkleDetailRepository sprinkleDetailRepository;
    private final SprinkleRedisHelper sprinkleRedisHelper;

    @Transactional
    public Sprinkle.Response sprinklePay(Sprinkle.Request sprinkle, UserInfo userInfo) {
        String token = TokenProvider.getToken();
        SprinkleEntity sprinkleEntity = SprinkleEntity.create(userInfo, sprinkle, token);
        sprinkleRepository.save(sprinkleEntity);

        List<Long> distributeAmount = DistributeAmountUtil.distributeAmount(sprinkle.getAmount(), sprinkle.getTargetCount());
        List<SprinkleDetailEntity> detailEntities = distributeAmount.stream()
                .map(d -> SprinkleDetailEntity.create(sprinkleEntity, d))
                .collect(Collectors.toList());
        sprinkleDetailRepository.saveAll(detailEntities);
        sprinkleRedisHelper.saveSprinkle(detailEntities, token);
        return Sprinkle.Response.of(token);
    }

    @Transactional
    public Receive.Response receivePay(Receive receive, UserInfo userInfo) {
        SprinkleEntity sprinkleEntity =
                sprinkleRepository.findByToken(receive.getToken()).orElseThrow(SprinklePayNotFoundException::new);
        sprinkleEntity.validateReceive(userInfo);

        Long sprinkleDetailId = sprinkleRedisHelper.getValidSprinkleDetail(sprinkleEntity.getToken());
        SprinkleDetailEntity detailEntity = sprinkleDetailRepository.findById(sprinkleDetailId).orElseThrow(SprinklePayNotFoundException::new);
        detailEntity.setReceiveUserInfo(userInfo.getUserId());

        return Receive.Response.of(detailEntity.getAmount());
    }

    @Transactional(readOnly = true)
    public Sprinkle getSprinklePay(String token, UserInfo userInfo) {
        SprinkleEntity entity = sprinkleRepository.findByToken(token).orElseThrow(SprinklePayNotFoundException::new);
        entity.validateSearch(userInfo);
        return Sprinkle.ofEntity(entity);
    }
}
