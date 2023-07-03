package com.lahee.market.repository;

import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findBySalesItem(SalesItem item, Pageable pageable);

    Page<Negotiation> findBySalesItemAndWriterAndPassword(SalesItem item, String writer, String password, Pageable pageable);

    List<Negotiation> findBySalesItem(SalesItem item);

    @Modifying
    @Query("UPDATE Negotiation o Set o.status='REJECT' WHERE o.salesItem = :item and o.status = 'SUGGEST'")
    void updateItemStatusToReject(@Param("item") SalesItem item);
}
