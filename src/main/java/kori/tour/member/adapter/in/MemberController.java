package kori.tour.member.adapter.in;


import java.util.List;
import java.util.Set;

import kori.tour.member.adapter.in.api.out.SubscribingRegionTours;
import kori.tour.tour.domain.Tour;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import kori.tour.global.data.area_code.Area;
import kori.tour.global.data.area_code.AreaCodeRegistry;
import kori.tour.member.adapter.in.api.in.SubscriptionUpdate;
import kori.tour.member.adapter.in.api.out.MyPage;
import kori.tour.member.adapter.in.api.out.SubscriptionsResponse;
import kori.tour.member.application.port.MemberUseCase;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Parameter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value= "/api/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberUseCase memberUseCase;
    private final AreaCodeRegistry areaCodeRegistry;
    private final MemberApiParser memberApiParser;

    @Operation(summary = "마이페이지 조회", description = "마이페이지에서 회원 정보를 조회합니다 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyPage.class))),
    })
    @GetMapping("/")
    public ResponseEntity<MyPage> memberLoginHome(){
        Member member = memberUseCase.getMemberWithSubscriptions(1L);
        return ResponseEntity.ok().body(memberApiParser.mapToMyPageResponse(member));
    }

    @Operation(summary = "전체 지역 목록 조회", description = "회원이 구독하는 지역을 표시하여 전체 지역 목록을 반환합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionsResponse.class))),
    })
    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionsResponse> getSubscriptions(HttpServletRequest servletRequest){
        List<Area> areaCodeList = areaCodeRegistry.getAreaCodeList();
        Set<Subscription> subscriptions = memberUseCase.getMemberWithSubscriptions(1L).getSubscriptions();
        SubscriptionsResponse subscriptionsResponse = memberApiParser.mapToSubscriptionResponse(areaCodeList, subscriptions);
        return ResponseEntity.ok().body(subscriptionsResponse);
    }

    @Operation(summary = "지역 구독 상태 변경", description = "특정 지역에 대한 구독 상태를 변경합니다. `subscribe`가 `true`이면 구독하고, `false`이면 구독을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "구독 상태 변경 성공")
    })
    @PutMapping("/subscriptions")
    public ResponseEntity<Void> updateSubscription(@RequestBody SubscriptionUpdate subscriptionUpdate){
        memberUseCase.updateSubscription(1L, subscriptionUpdate);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/")
//    public ResponseEntity<Void> memberSubscriptions(HttpServletRequest servletRequest){
//
//
//        return null;
//    }

    @Operation(summary = "구독 지역 축제/행사 조회", description = "회원이 구독하는 지역의 축제/행사 정보를 페이지별로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscribingRegionTours.class)))
    })
    @GetMapping("/subscriptions/tours")
    public ResponseEntity<SubscribingRegionTours> getSubscribingTours(@Parameter(description = "페이지 번호 (0부터 시작)", required = true, example = "0") @RequestParam int page){
        Slice<Tour> subscribingRegionTours = memberUseCase.getSubscribingRegionTours(1L, page);
        SubscribingRegionTours response = memberApiParser.mapToSubscribingRegionTours(subscribingRegionTours);
        return ResponseEntity.ok().body(response);
    }


}