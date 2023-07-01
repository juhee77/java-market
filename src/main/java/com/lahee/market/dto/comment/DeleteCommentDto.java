package com.lahee.market.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteCommentDto {
    String writer;
    String password;
}
