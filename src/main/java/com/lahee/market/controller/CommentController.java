package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.SAVE_COMMENT_MESSAGE;

@RestController
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveComment(@PathVariable("itemId") Long itemId,
                                                   @Valid @RequestBody RequestCommentDto requestCommentDto) {
        commentService.save(itemId, requestCommentDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(SAVE_COMMENT_MESSAGE));
    }

}
