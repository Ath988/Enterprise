package com.bilgeadam.repository;

import com.bilgeadam.entity.Offer;
import com.bilgeadam.views.VwOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	@Query("""
        SELECT new com.bilgeadam.views.VwOffer(
            o.offerDetail.title,
            o.status,
            CONCAT(c.profile.firstName, ' ', c.profile.lastName),
            c.profile.email
        )
        FROM Offer o
        LEFT JOIN Customer c ON o.customerId = c.customerId
    """)
	List<VwOffer> findAllOffersWithCustomerInfo();
	
	@Query("""
    SELECT new com.bilgeadam.views.VwOffer(
        o.offerDetail.title,
        o.status,
        CONCAT(c.profile.firstName, ' ', c.profile.lastName),
        c.profile.email
    )
    FROM Offer o
    LEFT JOIN Customer c ON o.customerId = c.customerId
    WHERE o.id = :offerId
""")
	List<VwOffer> findOfferWithCustomerInfoById(Long offerId);
}