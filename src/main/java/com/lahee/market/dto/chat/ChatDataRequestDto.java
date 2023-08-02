package com.lahee.market.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDataRequestDto {
    private Long roomId;// 방 번호
    private String writer; // 채팅 송신자
    private String message;// 메세지
}
