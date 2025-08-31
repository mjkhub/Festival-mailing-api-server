package kori.tour.member.application.port;

import kori.tour.member.adapter.in.api.in.SubscriptionUpdate;
import kori.tour.member.domain.Member;
import kori.tour.tour.domain.Tour;
import org.springframework.data.domain.Slice;

public interface MemberUseCase {

    Member getMemberWithSubscriptions(Long memberId);

    void updateSubscription(Long memberId, SubscriptionUpdate subscriptionUpdate);

    Slice<Tour> getSubscribingRegionTours(Long memberId, int page);

}
