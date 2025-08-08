package kori.tour.email.application.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.email.application.updater.dto.EmailSendRequestDto;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.member.domain.PlatformType;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@SpringBootTest
class EmailServiceTest {

    @MockitoBean
    private SesV2Client sesClient;

    @MockitoBean
    private EmailRepository emailRepository;

    @MockitoBean
    private TourRepository tourRepository;

    @MockitoBean
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송: 정상 케이스")
    void sendEmailToMembers_success() {
        // given
        Member member = Member.builder()
                .platformInfo(new PlatformInfo(PlatformType.KAKAO, "1234", "testEmail@test.com"))
                .build();

        Tour tour = Tour.builder()
                .id(1L)
                .areaCode("01")
                .sigunGuCode("10")
                .build();

        EmailSendRequestDto requestDto = new EmailSendRequestDto(
                List.of(member),
                "테스트 제목",
                "테스트 내용",
                String.valueOf(tour.getId()),
                tour.getAreaCode(),
                tour.getSigunGuCode()
        );

        SendEmailResponse mockResponse = SendEmailResponse.builder()
                .messageId("abc123")
                .build();

        doReturn(mockResponse).when(sesClient).sendEmail(any(SendEmailRequest.class));

        // when
        String messageId = emailService.sendEmailToMembers(requestDto);

        // then
        assertEquals("abc123", messageId);
        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
    }
}
