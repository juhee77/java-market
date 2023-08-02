package com.lahee.market.controller;


import com.lahee.market.dto.ApiResponse;
import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.chatroom.RequestChatroomDto;
import com.lahee.market.dto.chatroom.ResponseChatroomDto;
import com.lahee.market.dto.chatroom.ResponseChatroomEachDto;
import com.lahee.market.service.ChatroomService;
import com.lahee.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lahee.market.constants.ControllerMessage.DELETE_CHAT_ROOM;
import static com.lahee.market.util.SecurityUtil.getCurrentUsername;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatroomController {
    private final ChatroomService chatService;

    //필요 메서드
    //내가 방을 만들어서 들어가는 경우(제안을 하는 경우)
    @PostMapping("rooms")
    public ResponseEntity<ResponseChatroomDto> createRoom(@RequestBody RequestChatroomDto requestChatroomDto){
        return ResponseEntity.ok(chatService.createChatRoom(requestChatroomDto, getCurrentUsername()));
    }

    //내가 들어갈 수 있는방
    @GetMapping("rooms")
    public ApiResponse<List<ResponseChatroomDto>> findAllRooms() {
        return new ApiResponse<>(HttpStatus.OK, chatService.findSuggestedRoom(getCurrentUsername()));
    }

    //방을 입장하는 경우,해당 방의 데이터를 구체적으로 다시 보내준다.
    @GetMapping("/rooms/{roomId}")
    public ApiResponse<ResponseChatroomEachDto>  roomDetail(@PathVariable("roomId") Long roomId) {
        return new ApiResponse<>(HttpStatus.OK, chatService.findRoomById(roomId, getCurrentUsername()));
    }

    //방을 나오는 경우
    @DeleteMapping("/rooms/{roomId}")
    public ApiResponse<ResponseDto> deleteRoom(@PathVariable("roomId") Long roomId) {
        chatService.deleteRoom(roomId,getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK, ResponseDto.getInstance(DELETE_CHAT_ROOM));
    }

}
