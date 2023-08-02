package com.lahee.market.dto.chat;

import com.lahee.market.entity.Chatdata;
import com.lahee.market.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.lahee.market.constants.constant.ASIA_SEOUL;
import static com.lahee.market.constants.constant.CHAT_DATA_FORMATTER;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDataResponseDto {
    private MessageType messageType;
    private Long roomId;
    private String sender;
    private String message;
    private String time;

    public static ChatDataResponseDto fromEntity(Chatdata chatdata) {
        ZonedDateTime zonedDateTime = chatdata.getCreatedAt().atZone(ZoneId.of(ASIA_SEOUL));
        String format = zonedDateTime.format(CHAT_DATA_FORMATTER);
        return new ChatDataResponseDto(
                chatdata.getMessageType(),
                chatdata.getChatroom().getId(),
                chatdata.getWriter(),
                chatdata.getMessage(),
                format
        );
    }

}
