package kori.tour.member.application.service;

import kori.tour.global.annotation.UseCase;
import kori.tour.global.data.area_code.Area;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.application.port.MemberUseCase;
import kori.tour.member.domain.Subscription;
import lombok.RequiredArgsConstructor;
import java.util.Set;


@UseCase
@RequiredArgsConstructor
class MemberService implements MemberUseCase {

    private final MemberRepository memberRepository;
    @Override
    public Set<Subscription> getSubscriptions(Long memberId) {

        return memberRepository.findByIdWithSubscriptions(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND))
                .getSubscriptions();
    }
}
