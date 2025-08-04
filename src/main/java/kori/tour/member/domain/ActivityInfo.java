package kori.tour.member.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityInfo {

    private LocalDateTime signUpDate;
    private LocalDate lastSignInDate;

    /**
     * Updates the last sign-in date with the specified date.
     *
     * @param localDate the new last sign-in date to set
     */
    public void updateLastSignInDate(LocalDate localDate){
        this.lastSignInDate = localDate;
    }

}
