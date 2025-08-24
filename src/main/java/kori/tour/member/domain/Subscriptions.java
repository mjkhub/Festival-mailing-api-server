package kori.tour.member.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscriptions {

    @ElementCollection
    @CollectionTable(
            name = "member_subscription",
            joinColumns = @JoinColumn(name = "member_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "area_code", "sigun_gu_code"}),
            indexes = @Index(name = "idx_region", columnList = "area_code, sigun_gu_code"))
    private Set<Subscription> subscriptions = new HashSet<>();

    public Set<Subscription> getSubscriptions() {
        return Collections.unmodifiableSet(subscriptions);
    }

    public void add(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void remove(Subscription subscription) {
        this.subscriptions.remove(subscription);
    }

}
