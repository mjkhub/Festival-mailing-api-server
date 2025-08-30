package kori.tour.member.application.port;

import kori.tour.member.adapter.in.api.in.SubscriptionUpdate;
import kori.tour.member.domain.Member;

public interface MemberUseCase {

    Member getMemberWithSubscriptions(Long memberId);

    void updateSubscription(Long memberId, SubscriptionUpdate subscriptionUpdate);

}
