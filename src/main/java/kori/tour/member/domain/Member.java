package kori.tour.member.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Embedded
	private PlatformInfo platformInfo;

	@Embedded
	private ActivityInfo activityInfo;

	@Embedded
	private Subscriptions subscriptions = new Subscriptions();

	// Todo 비즈니스 로직은 데이터 흐름을 파악하고 작성하도록하자

}
