package kori.tour.member.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kori.tour.member.domain.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Retrieves a list of subscriptions for the specified area and sigunGu codes, eagerly loading the associated member for each subscription.
     *
     * @param areaCode the area code to filter subscriptions by
     * @param sigunGuCode the sigunGu code to filter subscriptions by
     * @return a list of subscriptions matching the given area and sigunGu codes, each with its associated member loaded
     */
    @Query("select s from Subscription s join fetch s.member m " +
            "where s.subscriptionInfo.areaCode = :areaCode and s.subscriptionInfo.sigunGuCode = :sigunGuCode")
    List<Subscription> findSubscriptionByAreaWithMember(String areaCode, String sigunGuCode);


}
