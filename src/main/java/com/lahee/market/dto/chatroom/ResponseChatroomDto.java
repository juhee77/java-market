package com.lahee.market.dto.chatroom;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatroomDto {
    private Long id;
    private String roomName;
    private Long itemId;
    private String itemName;
    private String itemSeller;

    public static ResponseChatroomDto fromEntity(Chatroom chatroom) {
        ResponseChatroomDto dto = new ResponseChatroomDto();
        dto.itemName = chatroom.getItem().getTitle();
        dto.itemId = chatroom.getItem().getId();
        dto.id = chatroom.getId();
        dto.itemSeller = chatroom.getSeller().getNickname();
        dto.roomName = chatroom.getRoomName();
        return dto;
    }
}
