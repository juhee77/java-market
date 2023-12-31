package com.lahee.market.repository;

import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findBySalesItem(SalesItem item, Pageable pageable);

    Page<Negotiation> findByUserAndSalesItem(User user, SalesItem item, Pageable pageable);

    List<Negotiation> findBySalesItem(SalesItem item);

    @Modifying
    @Query("UPDATE Negotiation o Set o.status='REJECT' WHERE o.salesItem = :item and o.id != :nId")
    void updateItemStatusToReject(@Param("item") SalesItem item, @Param("nId") Long negotiationId);
}
