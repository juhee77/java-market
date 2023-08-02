package com.lahee.market.repository;


import com.lahee.market.entity.Chatroom;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    @Query("SELECT c FROM Chatroom c WHERE c.seller = :user OR c.suggester = :user")
    List<Chatroom> findAllBySellerAndSuggester(@Param("user") User user);

    Optional<Chatroom> findByItemAndSuggester(SalesItem item, User user);
}
