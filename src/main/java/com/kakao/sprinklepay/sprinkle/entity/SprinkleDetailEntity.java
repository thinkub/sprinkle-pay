package com.kakao.sprinklepay.sprinkle.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Entity
@Getter
@Builder
@Table(name = "sprinkle_detail",
        indexes = {
                @Index(columnList = "sprinkle_id, receive_user_id"),
        })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @CreatedDate
    @Column(name = "receive_datetime")
    private LocalDateTime receiveDatetime;

    private SprinkleDetailEntity(SprinkleEntity sprinkle, Long receiveUserId, long amount) {
        this.sprinkle = sprinkle;
        this.receiveUserId = receiveUserId;
        this.amount = amount;
    }

    public static SprinkleDetailEntity create(SprinkleEntity sprinkle, Long receiveUserId, long amount) {
        return new SprinkleDetailEntity(sprinkle, receiveUserId, amount);
    }
}
