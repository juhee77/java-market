package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.*;

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

    @GetMapping
    public ResponseEntity<Page<ResponseCommentDto>> findAllCommentsByItem(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(commentService.findAllEntityByItem(itemId, page, limit));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> findOneComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.findOneById(itemId, commentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto> updateComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        commentService.updateComment(itemId, commentId, requestCommentDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_COMMENT_MESSAGE));
    }

    @PutMapping("/{commentId}/reply")
    public ResponseEntity<ResponseDto> updateComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentReplyDto commentReplyDto) {
        commentService.updateCommentReply(itemId, commentId, commentReplyDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_COMMENT_REPLY_MESSAGE));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId,
            @RequestBody DeleteCommentDto deleteCommentDto) {
        commentService.deleteComment(itemId, commentId, deleteCommentDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(DELETE_COMMENT_MESSAGE));
    }
}
