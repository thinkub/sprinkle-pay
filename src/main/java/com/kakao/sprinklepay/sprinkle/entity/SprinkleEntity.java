package com.kakao.sprinklepay.sprinkle.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author by Ming(thinkub0219@gmail.com) on 2020/11/17.
 */

@Entity
@Getter
@Table(name = "sprinkle",
        indexes = {
                @Index(columnList = "room_id, token")
        })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class SprinkleEntity {
    @Id
    @Column(name = "sprinkle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprinkleId;

    @Column(name = "room_id", nullable = false, length = 20)
    private String roomId;

    @Column(name = "token", nullable = false, length = 3)
    private String token;

    @Column(name = "target_count", nullable = false)
    private int targetCount;

    @Column(name = "amount", nullable = false)
    private long amount;

    @CreatedDate
    @Column(name = "register_datetime", nullable = false)
    private LocalDateTime registerDatetime;

    @Column(name = "register_user_id", nullable = false)
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id")
    private List<SprinkleDetailEntity> sprinkleDetails;

}
