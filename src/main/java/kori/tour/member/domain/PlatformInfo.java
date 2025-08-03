package kori.tour.member.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformInfo {

    @Enumerated(EnumType.STRING) @Column(length = 20)
    private PlatformType platformType;
    private String platformPk;
    private String platformEmail;

}
