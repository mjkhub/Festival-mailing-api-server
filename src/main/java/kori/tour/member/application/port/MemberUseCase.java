package kori.tour.member.application.port;

import kori.tour.member.domain.Subscription;

import java.util.Set;

public interface MemberUseCase {

    Set<Subscription> getSubscriptions(Long memberId);

}
