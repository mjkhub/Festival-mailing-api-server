package kori.tour.member.application.service;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import kori.tour.global.annotation.UseCase;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.member.adapter.in.api.in.SubscriptionUpdate;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.application.port.MemberUseCase;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.Subscription;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
class MemberService implements MemberUseCase {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberWithSubscriptions(Long memberId) {
        return memberRepository.findByIdWithSubscriptions(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    @Override
    public void updateSubscription(Long memberId, SubscriptionUpdate subscriptionUpdate) {
        Member member = getMemberWithSubscriptions(memberId);
        Subscription subscription = Subscription.builder()
                .areaCode(subscriptionUpdate.areaCode())
                .sigunGuCode(subscriptionUpdate.sigunGuCode())
                .subscribeDate(LocalDateTime.now())
                .build();
        if(subscriptionUpdate.subscribe()) member.addSubscription(subscription);
        else member.removeSubscription(subscription);
    }
}
