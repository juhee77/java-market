package com.lahee.market.controller;

import com.lahee.market.dto.ApiResponse;
import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.service.CommentService;
import com.lahee.market.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.*;
import static com.lahee.market.dto.ResponseDto.getInstance;
import static com.lahee.market.util.SecurityUtil.getCurrentUsername;

@RestController
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ApiResponse<ResponseDto> saveComment(@PathVariable("itemId") Long itemId,
                                                @Valid @RequestBody RequestCommentDto requestCommentDto) {
        commentService.save(itemId, requestCommentDto, SecurityUtil.getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK, getInstance(SAVE_COMMENT_MESSAGE));
    }

    @GetMapping
    public ApiResponse<Page<ResponseCommentDto>> findAllCommentsByItem(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return new ApiResponse<>(HttpStatus.OK, commentService.findAllEntityByItem(itemId, page, limit));
    }

    @GetMapping("/{commentId}")
    public ApiResponse<ResponseCommentDto> findOneComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId) {
        return new ApiResponse<>(HttpStatus.OK, commentService.findOneById(itemId, commentId));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<ResponseDto> updateComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        commentService.updateComment(itemId, commentId, requestCommentDto, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK, getInstance(UPDATE_COMMENT_MESSAGE));
    }

    @PutMapping("/{commentId}/reply")
    public ApiResponse<ResponseDto> replyComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentReplyDto commentReplyDto) {
        commentService.updateCommentReply(itemId, commentId, commentReplyDto, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK, getInstance(UPDATE_COMMENT_REPLY_MESSAGE));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<ResponseDto> deleteComment(
            @PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(itemId, commentId, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK, getInstance(DELETE_COMMENT_MESSAGE));
    }
}
