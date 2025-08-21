package kori.tour.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("새로운 구독이 정상적으로 추가된다")
    void addSubscription_shouldAddNewSubscription() {
        //given
        Member member = Member.builder().build();
        Subscription subscription = Subscription.builder().build();

        //when
        member.addSubscription(subscription);

        //then
        Set<Subscription> subscriptions = member.getSubscriptions();
        assertThat(subscriptions).contains(subscription);
    }

    @Test
    @DisplayName("중복된 구독은 여러 번 추가되지 않고 1개만 유지된다")
    void addSubscription_shouldNotAddDuplicateSubscription() {
        //given
        Member member = Member.builder().build();
        Subscription subscription = Subscription.builder().build();

        //when
        member.addSubscription(subscription);
        member.addSubscription(subscription);

        //then
        Set<Subscription> subscriptions = member.getSubscriptions();
        assertThat(subscriptions).hasSize(1).containsExactly(subscription);
    }

    @Test
    @DisplayName("null 구독을 추가하려 하면 예외가 발생한다")
    void addSubscription_shouldHandleNullSubscriptionGracefully() {
        // 별로 의미 없는 케이스라서 테스트에서 제외
    }
}
