package com.lahee.market.service;


import com.lahee.market.dto.chatroom.RequestChatroomDto;
import com.lahee.market.dto.chatroom.ResponseChatroomDto;
import com.lahee.market.dto.chatroom.ResponseChatroomEachDto;
import com.lahee.market.entity.Chatroom;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.entity.User;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import com.lahee.market.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatroomService {
    private final ChatroomRepository chatRoomRepository;
    private final UserService userService;
    private final SalesItemService itemService;

    @Transactional
    public ResponseChatroomDto createChatRoom(RequestChatroomDto dto, String username) {
        User suggester = userService.getUser(username);//제안하는 사람
        SalesItem salesItem = itemService.getSalesItem(dto.getItemId()); //지금 사려고 하는 아이템
        User seller = salesItem.getUser();//지금 사려고 하는 아이템의 판매자

        if (!chatRoomRepository.findByItemAndSuggester(salesItem, suggester).isEmpty()) {
            throw new CustomException(ErrorCode.DUPLICATE_CHAT_ROOM);
        }

        //채팅방 이름 확인
//        if (!dto.getRoomName().equals(suggester.getNickname())) {
//            throw new CustomException("유저 이름과 현재 로그인된 유저이름이 다릅니다.");
//        }

        Chatroom chatroom = Chatroom.getEntityInstance(dto.getRoomName(), salesItem, seller, suggester);

        return ResponseChatroomDto.fromEntity(chatRoomRepository.save(chatroom));
    }

    //내가 들어갈 수 있는 방들
    public List<ResponseChatroomDto> findSuggestedRoom(String username) {
        User user = userService.getUser(username);//현재 내가 누구인가

        //chat room 중에 내가 속한 방이 있다면 모두 보여준다.
        List<Chatroom> chatrooms = chatRoomRepository.findAllBySellerAndSuggester(user);
        return chatrooms.stream().map(ResponseChatroomDto::fromEntity).collect(Collectors.toList());
    }

    public ResponseChatroomEachDto findRoomById(Long roomId, String username) {
        //해당 방에 들어올 수 있는 사람인지 확인
        User user = userService.getUser(username);
        Chatroom chatroom = getChatroom(roomId);
        if (!chatroom.getSeller().equals(user) && !chatroom.getSuggester().equals(user)) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ROOM_USER);
        }
        return ResponseChatroomEachDto.fromEntity(chatroom);
    }

    @Transactional
    public void deleteRoom(Long roomId, String username) {
        Chatroom chatroom = getChatroom(roomId);
        User user = userService.getUser(username);

        chatroom.validateUser(user);//해당 유저가 지울 수 있는 권한이 있는지 확인한다.
        chatRoomRepository.deleteById(roomId);
    }

    public Chatroom getChatroom(Long roomId) {
        Optional<Chatroom> item = chatRoomRepository.findById(roomId);
        if (item.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ROOM_ID);
        }
        return item.get();
    }

    @Transactional
    public void updateActivate(Chatroom room) {
        room.updateActivate();
    }
}
