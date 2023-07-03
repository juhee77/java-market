package com.lahee.market.repository;

import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findBySalesItem(SalesItem item, Pageable pageable);

    Page<Negotiation> findBySalesItemAndWriterAndPassword(SalesItem item, String writer, String password, Pageable pageable);

}
