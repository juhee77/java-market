package com.lahee.market.repository;

import com.lahee.market.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository  extends JpaRepository<SalesItem,Long> {
}
