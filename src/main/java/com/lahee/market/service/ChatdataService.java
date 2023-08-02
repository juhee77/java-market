package com.lahee.market.service;


import com.lahee.market.dto.chat.ChatDataRequestDto;
import com.lahee.market.entity.Chatdata;
import com.lahee.market.entity.Chatroom;
import com.lahee.market.entity.MessageType;
import com.lahee.market.repository.ChatdataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.lahee.market.entity.Chatdata.getInstance;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatdataService {
    private final ChatdataRepository chatDataRepository;
    private final ChatroomService chatRoomService;

    //메세지를 저장
    @Transactional
    public Chatdata save(ChatDataRequestDto message, MessageType messageType) {
        return chatDataRepository.save(getInstance(message, chatRoomService.getChatroom(message.getRoomId()), messageType));
    }

    public List<Chatdata> findAllChatByRoomIdAndDate(Chatroom chatRoom, Instant subscribeDateTime) {
        return chatDataRepository.findChatAllChatByCreatedAtAfterAndChatroom(subscribeDateTime, chatRoom);
    }

    public List<Chatdata> findAllChat(Chatroom chatroom) {
        return chatDataRepository.findAllById(chatroom.getId());
    }

}
