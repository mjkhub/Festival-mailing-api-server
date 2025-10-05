package kori.tour.email.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kori.tour.member.domain.Member;
import kori.tour.tour.domain.Tour;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "tour_id", unique = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tour tour;

	// 이메일이 실제로 보내진 시간과 오차가 있을 수 있음
	private LocalDateTime sendTime;

	@JoinColumn(name = "email_content_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmailContent emailContent;

}
