package com.lahee.market.repository;

import com.lahee.market.entity.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation,Long> {
}
