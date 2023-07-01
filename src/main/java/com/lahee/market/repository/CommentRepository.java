package com.lahee.market.repository;

import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findBySalesItem(SalesItem item, Pageable pageable);
}
