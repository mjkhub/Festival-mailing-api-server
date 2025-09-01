package kori.tour.member.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.global.annotation.UseCase;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.member.adapter.in.api.in.SubscriptionUpdate;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.application.port.MemberUseCase;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.Subscription;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.RegionCode;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
class MemberService implements MemberUseCase {

    private final MemberRepository memberRepository;

    private final TourRepository tourRepository;

    private final int PAGE_SIZE = 15;

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

    @Override
    public Slice<Tour> getSubscribingRegionTours(Long memberId, int page) {
        Member member = getMemberWithSubscriptions(memberId);
        List<RegionCode> subscriptions = member.getSubscriptions().stream()
                .map(Subscription::mapToRegionCode)
                .toList();
        Pageable pageRequest = PageRequest.of(Math.max(0,page), PAGE_SIZE);
        if (subscriptions.isEmpty())
            return new SliceImpl<>(List.of(), pageRequest, false);
        return tourRepository.findByMemberSubscriptions(subscriptions, LocalDate.now(), pageRequest);
    }
}
