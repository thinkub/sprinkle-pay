package com.kakao.sprinklepay.sprinkle.entity;

import com.kakao.sprinklepay.exception.ExceptionType;
import com.kakao.sprinklepay.sprinkle.exception.CustomException;
import com.kakao.sprinklepay.sprinkle.model.Sprinkle;
import com.kakao.sprinklepay.sprinkle.model.UserInfo;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Entity
@Getter
@Builder
@Table(name = "sprinkle",
        indexes = {
                @Index(columnList = "room_id, token")
        })
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SprinkleEntity {
    @Id
    @Column(name = "sprinkle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprinkleId;

    @Column(name = "room_id", nullable = false, length = 20)
    private String roomId;

    @Column(name = "token", nullable = false, unique = true, length = 15)
    private String token;

    @Column(name = "target_count", nullable = false)
    private int targetCount;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "total_received_amount", nullable = false)
    private long totalReceivedAmount;

    @Column(name = "total_received_count", nullable = false)
    private int totalReceivedCount;

    @CreatedDate
    @Column(name = "register_datetime", nullable = false)
    private LocalDateTime registerDatetime;

    @Column(name = "register_user_id", nullable = false)
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id")
    @Builder.Default
    private List<SprinkleDetailEntity> sprinkleDetails = new ArrayList<>();

    private SprinkleEntity(String roomId, String token, int targetCount, long amount, Long userId) {
        this.roomId = roomId;
        this.token = token;
        this.targetCount = targetCount;
        this.amount = amount;
        this.userId = userId;
        this.totalReceivedAmount = 0;
        this.totalReceivedCount = 0;
    }

    public static SprinkleEntity create(UserInfo userInfo, Sprinkle.Request sprinkle, String token) {
        return new SprinkleEntity(userInfo.getRoomId(), token, sprinkle.getTargetCount(), sprinkle.getAmount(), userInfo.getUserId());
    }

    public long getRemainAmount() {
        return this.amount - this.totalReceivedAmount;
    }

    public int getRemainCount() {
        return this.targetCount - this.totalReceivedCount;
    }

    public void validateReceive(UserInfo userInfo) {
        if (!userInfo.getRoomId().equals(this.roomId)) {
            throw new CustomException(ExceptionType.NOT_SAME_ROOM_ERROR);
        }
        if (userInfo.getUserId().equals(this.userId)) {
            throw new CustomException(ExceptionType.SPRINKLE_USER_NOT_RECEIVE_ERROR);
        }
        if (this.registerDatetime.isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new CustomException(ExceptionType.RECEIVE_VALID_TIME_ERROR);
        }
        if (this.getRemainCount() == 0) {
            throw new CustomException(ExceptionType.NO_REMAIN_PAY_RECEIVE_ERROR);
        }
    }

    public void validateSearch(UserInfo userInfo) {
        if (!userInfo.getUserId().equals(this.userId)) {
            throw new CustomException(ExceptionType.SPRINKLE_PAY_USER_ACCESS_DENIED);
        }
        if (this.registerDatetime.isBefore(LocalDateTime.now().minusDays(7))) {
            throw new CustomException(ExceptionType.SPRINKLE_PAY_SEARCH_VALID_DATE_ERROR);
        }
    }

    public void received(long receivedAmount) {
        this.totalReceivedAmount = this.totalReceivedAmount + receivedAmount;
        this.totalReceivedCount++;
    }
}
