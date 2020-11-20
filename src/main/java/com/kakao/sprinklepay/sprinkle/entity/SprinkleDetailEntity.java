package com.kakao.sprinklepay.sprinkle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Entity
@Getter
@Table(name = "sprinkle_detail",
        indexes = {
                @Index(columnList = "sprinkle_id")
        })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class SprinkleDetailEntity {
    @Id
    @Column(name = "sprinkle_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprinkleDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id", nullable = false)
    private SprinkleEntity sprinkle;

    @Column(name = "amount")
    private long amount;

    @Column(name = "receive_user_id")
    private Long receiveUserId;

    @Column(name = "receive_datetime")
    private LocalDateTime receiveDatetime;

    private SprinkleDetailEntity(SprinkleEntity sprinkle, long amount) {
        this.sprinkle = sprinkle;
        this.amount = amount;
    }

    public static SprinkleDetailEntity create(SprinkleEntity sprinkle, long amount) {
        return new SprinkleDetailEntity(sprinkle, amount);
    }

    public boolean hasReceived(Long userId) {
        return userId.equals(this.receiveUserId);
    }

    public boolean hasValidReceive() {
        return this.receiveUserId == null;
    }

    public void setReceiveUserInfo(Long userId) {
        this.receiveUserId = userId;
        this.receiveDatetime = LocalDateTime.now();
    }
}
