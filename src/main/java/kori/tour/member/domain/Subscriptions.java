package kori.tour.member.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscriptions {

    @ElementCollection
    @CollectionTable(
            name = "member_subscription",
            joinColumns = @JoinColumn(name = "member_id"))
    private Set<Subscription> subscriptions = new HashSet<>();

    public Set<Subscription> getSubscriptions() {
        return Collections.unmodifiableSet(subscriptions);
    }

}
