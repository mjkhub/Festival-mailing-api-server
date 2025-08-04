package kori.tour.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class PlatformProfile {

    @Setter
    private PlatformType platformType;
    private String platformPk;
    private String platformEmail;

    /**
     * Constructs a PlatformProfile with the specified platform primary key and email.
     *
     * @param platformPk    the unique identifier for the user on the platform
     * @param platformEmail the email address associated with the user's platform account
     */
    public PlatformProfile(String platformPk, String platformEmail) { //oauth
        this.platformPk = platformPk;
        this.platformEmail = platformEmail;
    }


}
