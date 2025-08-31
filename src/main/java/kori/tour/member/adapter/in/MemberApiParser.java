package kori.tour.member.adapter.in;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kori.tour.global.data.area_code.Area;
import kori.tour.member.adapter.in.api.out.AreaDto;
import kori.tour.member.adapter.in.api.out.MyPage;
import kori.tour.member.adapter.in.api.out.SubAreaDto;
import kori.tour.member.adapter.in.api.out.SubscriptionsResponse;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.Subscription;

@Component
class MemberApiParser {

    public MyPage mapToMyPageResponse(Member member) {
        return new MyPage(member.getPlatformInfo().getPlatformEmail(), member.getEmailSubscribe());
    }

    public SubscriptionsResponse mapToSubscriptionResponse(List<Area> areaCodeList, Set<Subscription> subscriptions) {

        // 1. 빠른 조회를 위해 (areaCode, sigunGuCode) -> boolean 맵 구성
        Set<String> subscribedKeys = subscriptions.stream()
                .map(s -> s.getAreaCode() + ":" + s.getSigunGuCode())
                .collect(Collectors.toSet());

        // 2. 전체 지역 + 구독 여부 붙이기
        List<AreaDto> areaDtos = areaCodeList.stream()
                .map(area -> new AreaDto(
                        area.areaCode(),
                        area.name(),
                        area.subRegions().stream()
                                .map(sub -> new SubAreaDto(
                                        sub.sigunGuCode(),
                                        sub.name(),
                                        subscribedKeys.contains(area.areaCode() + ":" + sub.sigunGuCode())
                                ))
                                .toList()
                ))
                .toList();

        // 3. 최종 응답
        return new SubscriptionsResponse(areaDtos);
    }


}
