package kori.tour.email.application.updater;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.email.application.updater.dto.EmailSendRequest;
import kori.tour.email.domain.Email;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final EmailSendUseCase emailSendUseCase;
	private final EmailRepository emailRepository;
	private final TourRepository tourRepository;
	private final MemberRepository memberRepository;


	public Slice<Member> findMembersBySubscription(String areaCode, String sigunGucCode, Pageable pageRequest){
		return memberRepository.findBySubscriptionArea(areaCode, sigunGucCode, pageRequest);
	}

	public String sendEmailToMembers(EmailSendRequest requestDto) {
		List<String> emails = requestDto.members().stream()
				.map(Member::getPlatformInfo)
				.map(PlatformInfo::getPlatformEmail)
				.toList();

		String messageId = emailSendUseCase.sendEmail(emails, requestDto.emailTitle(), requestDto.emailContent());
		log.info("Email sent to {} with messageId {}", emails, messageId);

		return messageId;
	}

	@Transactional
	public void saveEmails(List<Member> members, String messageId, Tour tour, String emailTitle, String emailBody) {
		LocalDateTime sendTime = LocalDateTime.now();
		Tour savedTour = tourRepository.save(tour);
		List<Member> savedMembers = memberRepository.saveAll(members);
		for (Member member : savedMembers) {
			Email email = Email.builder()
					.member(member)
					.tour(savedTour)
					.sendTime(sendTime)
					.title(emailTitle)
					.body(emailBody)
					.messageId(messageId)
					.build();
			emailRepository.save(email);
		}
	}

}
