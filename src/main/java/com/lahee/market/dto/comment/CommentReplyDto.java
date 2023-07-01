package com.lahee.market.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentReplyDto {
    String writer;
    String password;
    String reply;
}
