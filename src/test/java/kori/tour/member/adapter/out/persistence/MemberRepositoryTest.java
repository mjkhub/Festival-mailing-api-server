package kori.tour.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import kori.tour.member.domain.Member;
import kori.tour.member.domain.Subscription;

@DataJpaTest
class ryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager em;

    private final String TARGET_AREA_CODE = "1";
    private final String TARGET_SIGUNGU_CODE = "10";

    @BeforeEach
    void setUp() {
        Subscription targetSubscription = Subscription.builder()
                .areaCode(TARGET_AREA_CODE)
                .sigunGuCode(TARGET_SIGUNGU_CODE)
                .build();

        Subscription otherSubscription = Subscription.builder()
                .areaCode("2")
                .sigunGuCode("20")
                .build();
        // 5명의 회원을 생성하여 지역을 두개씩 구독
        IntStream.range(0, 5).forEach(i -> {
            Member member = Member.builder().build();
            member.addSubscription(targetSubscription);
            member.addSubscription(otherSubscription);
            memberRepository.save(member);
        });

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("특정 ID로 회원과 구독 정보를 조회하면 해당 회원과 구독 정보를 반환한다")
    void findByIdWithSubscriptions_shouldReturnMemberWithSubscriptions() {
        // given
        Member newMember = Member.builder().build();
        Subscription subscription = Subscription.builder()
                .areaCode(TARGET_AREA_CODE)
                .sigunGuCode(TARGET_SIGUNGU_CODE)
                .build();
        newMember.addSubscription(subscription);
        Member persistedMember = memberRepository.save(newMember);
        em.flush();
        em.clear();

        // when
        Optional<Member> result = memberRepository.findByIdWithSubscriptions(persistedMember.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getSubscriptions()).hasSize(1);
        assertThat(result.get().getSubscriptions())
                .anyMatch(s -> s.getAreaCode().equals(TARGET_AREA_CODE) && s.getSigunGuCode().equals(TARGET_SIGUNGU_CODE));
    }

    @Test
    @DisplayName("특정 지역을 구독하는 회원을 페이지네이션하여 첫 페이지를 조회한다")
    void findBySubscriptionArea_shouldReturnFirstPageOfSubscribedMembers() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 3);

        // when
        Slice<Member> result = memberRepository.findBySubscriptionArea(TARGET_AREA_CODE, TARGET_SIGUNGU_CODE, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getContent()).isSortedAccordingTo(Comparator.comparing(Member::getId));
    }

    @Test
    @DisplayName("특정 지역을 구독하는 회원을 페이지네이션하여 마지막 페이지를 조회한다")
    void findBySubscriptionArea_shouldReturnLastPageOfSubscribedMembers() {
        // given
        PageRequest pageRequest = PageRequest.of(1, 3);

        // when
        Slice<Member> result = memberRepository.findBySubscriptionArea(TARGET_AREA_CODE, TARGET_SIGUNGU_CODE, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2); // 5명 중 2번째 페이지 (크기: 3) -> 2명
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.hasNext()).isFalse();
        assertThat(result.isLast()).isTrue();
        assertThat(result.getContent()).isSortedAccordingTo(Comparator.comparing(Member::getId));
    }

    @Test
    @DisplayName("구독자가 없는 지역을 조회하면 빈 Slice를 반환한다")
    void findBySubscriptionArea_shouldReturnEmptySliceForUnsubscribedArea() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 3);
        String nonExistentAreaCode = "99";
        String nonExistentSigunguCode = "999";

        // when
        Slice<Member> result = memberRepository.findBySubscriptionArea(nonExistentAreaCode, nonExistentSigunguCode, pageRequest);

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }
}
