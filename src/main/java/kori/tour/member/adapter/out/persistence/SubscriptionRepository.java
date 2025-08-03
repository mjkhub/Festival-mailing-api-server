package kori.tour.member.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kori.tour.member.domain.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s join fetch s.member m " +
            "where s.subscriptionInfo.areaCode = :areaCode and s.subscriptionInfo.sigunGuCode = :sigunGuCode")
    List<Subscription> findSubscriptionByAreaWithMember(String areaCode, String sigunGuCode);


}
