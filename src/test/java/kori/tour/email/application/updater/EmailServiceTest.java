package kori.tour.email.application.updater;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.tour.adapter.out.persistence.TourRepository;

@SpringBootTest
class EmailServiceTest {


    @MockBean
    private EmailRepository emailRepository;

    @MockBean
    private TourRepository tourRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송: 정상 케이스")
    void sendEmailToMembers_success() {


    }
}
