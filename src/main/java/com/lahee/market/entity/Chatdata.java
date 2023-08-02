package com.lahee.market.entity;


import com.lahee.market.dto.chat.ChatDataRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Chatdata extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Chatroom chatroom;

    private String writer;
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    //생성자
    public static Chatdata getInstance(ChatDataRequestDto chatDataDto, Chatroom chatRoom, MessageType messageType) {
        Chatdata chatData = new Chatdata();
        chatData.messageType = messageType;
        chatData.chatroom = chatRoom;
        chatData.message = chatDataDto.getMessage();
        chatData.writer = chatDataDto.getWriter();
        return chatData;
    }
}
