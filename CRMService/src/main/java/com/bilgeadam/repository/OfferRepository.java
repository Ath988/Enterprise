package com.bilgeadam.repository;

import com.bilgeadam.entity.Offer;
import com.bilgeadam.views.VwOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
	
}