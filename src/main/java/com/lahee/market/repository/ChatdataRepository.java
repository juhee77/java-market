package com.lahee.market.repository;


import com.lahee.market.entity.Chatdata;
import com.lahee.market.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ChatdataRepository extends JpaRepository<Chatdata, Long> {
    List<Chatdata> findChatAllChatByCreatedAtAfterAndChatroom(Instant date, Chatroom chatroom);

    List<Chatdata> findAllById(Long id);
}
