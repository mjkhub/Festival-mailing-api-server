package kori.tour.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Subscription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subscription_id")
    private Long id;

    @JoinColumn(name="member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    private SubscriptionInfo subscriptionInfo;

}
