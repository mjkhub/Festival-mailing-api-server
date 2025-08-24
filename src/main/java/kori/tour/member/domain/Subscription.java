package kori.tour.member.domain;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {

	private String areaCode;

	private String sigunGuCode;

	private String sigunGuName;

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Subscription that = (Subscription) o;
		return Objects.equals(areaCode, that.areaCode) && Objects.equals(sigunGuCode, that.sigunGuCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(areaCode, sigunGuCode);
	}
}
