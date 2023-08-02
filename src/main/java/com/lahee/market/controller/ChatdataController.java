package com.lahee.market.controller;


import com.lahee.market.dto.chat.ChatDataRequestDto;
import com.lahee.market.dto.chat.ChatDataResponseDto;
import com.lahee.market.entity.Chatdata;
import com.lahee.market.entity.Chatroom;
import com.lahee.market.entity.MessageType;
import com.lahee.market.entity.User;
import com.lahee.market.service.ChatdataService;
import com.lahee.market.service.ChatroomService;
import com.lahee.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatdataController {
    private final SimpMessageSendingOperations sendingOperations;
    private final UserService memberService;
    private final ChatdataService chatDataService;
    private final ChatroomService chatRoomService;

    //구독하는 시점에 동작
    @Transactional
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatDataRequestDto message) {
        log.info("enter: {}", message);
        User user = memberService.getUser(message.getWriter());
        Chatroom room = chatRoomService.getChatroom(message.getRoomId());

        //판매자의 수락이 되지 않은 경우
        if (room.getSeller().equals(user) && !room.isActive()) {
            //수락이 되지 않은 경우(지금 입장함) // 판매자만 입장 메세지 추가(제안자는 생성시점에 입장)
            chatDataService.save(message, MessageType.ENTER);
            chatRoomService.updateActivate(room);
        }
        // 해당 방의 이전 채팅 기록 가져오기
        List<ChatDataResponseDto> chatList = new ArrayList<>();
        List<Chatdata> allChatList = chatDataService.findAllChatByRoomIdAndDate(room, room.getCreatedAt());
        if (allChatList != null) {
            chatList = allChatList.stream().map(ChatDataResponseDto::fromEntity).collect(Collectors.toList());
        }

        // 해당 사용자에게만 메시지 전송
        sendingOperations.convertAndSend("/user/" + message.getWriter() + "/sub/chat/enter/" + message.getRoomId(), chatList);
        log.info("지난 기록 전송 완료: {}", chatList);
    }

    @MessageMapping(value = "/chat/send")
    public void message(ChatDataRequestDto message) {
        log.info("out: {}", message);
        Chatdata save = chatDataService.save(message, MessageType.BASIC);
        ChatDataResponseDto chatDataResponseDto = ChatDataResponseDto.fromEntity(save);

        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
    }

}
