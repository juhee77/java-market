package com.lahee.market.dto.comment;

import com.lahee.market.entity.Comment;
import lombok.Data;

@Data
public class ResponseCommentDto {
    Long id;
    String content;
    String reply;

    public static ResponseCommentDto fromEntity(Comment comment) {
        ResponseCommentDto dto = new ResponseCommentDto();
        dto.id = comment.getId();
        dto.content = comment.getContent();
        dto.reply = comment.getReply();
        return dto;
    }

}
