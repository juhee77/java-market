package com.lahee.market.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentReplyDto {
    @NotBlank
    String writer;
    @NotBlank
    String password;
    @NotBlank
    String reply;
}
