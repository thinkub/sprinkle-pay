package com.kakao.sprinklepay.sprinkle.service;

import com.kakao.sprinklepay.exception.ExceptionType;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleDetailEntity;
import com.kakao.sprinklepay.sprinkle.entity.SprinkleEntity;
import com.kakao.sprinklepay.sprinkle.exception.CustomException;
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

import java.util.Optional;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Service
@RequiredArgsConstructor
public class SprinklePayService {
    private final SprinkleRepository sprinkleRepository;
    private final SprinkleDetailRepository sprinkleDetailRepository;

    @Transactional
    public Sprinkle.Response sprinklePay(Sprinkle.Request sprinkle, UserInfo userInfo) {
        sprinkle.validRequest();
        String token = TokenProvider.getToken();
        SprinkleEntity sprinkleEntity = SprinkleEntity.create(userInfo, sprinkle, token);
        sprinkleRepository.save(sprinkleEntity);
        return Sprinkle.Response.of(token);
    }

    @Transactional
    public Receive.Response receivePay(Receive receive, UserInfo userInfo) {
        SprinkleEntity sprinkleEntity =
                sprinkleRepository.findByToken(receive.getToken()).orElseThrow(() -> new CustomException(ExceptionType.SPRINKLE_PAY_NOT_FOUND_ERROR));
        sprinkleEntity.validateReceive(userInfo);

        Optional<SprinkleDetailEntity> detailOptional =
                sprinkleDetailRepository.findBySprinkleAndReceiveUserId(sprinkleEntity, userInfo.getUserId());
        if (detailOptional.isPresent()) {
            throw new CustomException(ExceptionType.ALREADY_RECEIVED_ERROR);
        }

        long distributeAmount =
                DistributeAmountUtil.distributeAmount(sprinkleEntity.getTargetCount(), sprinkleEntity.getRemainCount(), sprinkleEntity.getRemainAmount());
        SprinkleDetailEntity detailEntity = SprinkleDetailEntity.create(sprinkleEntity, userInfo.getUserId(), distributeAmount);
        sprinkleDetailRepository.save(detailEntity);
        sprinkleEntity.received(distributeAmount);

        return Receive.Response.of(distributeAmount);
    }

    @Transactional(readOnly = true)
    public Sprinkle getSprinklePay(String token, UserInfo userInfo) {
        SprinkleEntity entity = sprinkleRepository.findByToken(token).orElseThrow(() -> new CustomException(ExceptionType.SPRINKLE_PAY_NOT_FOUND_ERROR));
        entity.validateSearch(userInfo);
        return Sprinkle.ofEntity(entity);
    }
}
