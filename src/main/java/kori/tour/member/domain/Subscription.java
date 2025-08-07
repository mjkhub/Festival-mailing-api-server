package kori.tour.member.domain;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {

	private String areaCode;

	private String sigunGuCode;

	private String sigunGuName;

	//Todo maybe need all-Args-Constructor

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
