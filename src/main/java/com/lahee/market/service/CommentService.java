package com.lahee.market.service;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.CommentNotFoundException;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.repository.CommentRepository;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {
    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseCommentDto save(Long itemId, RequestCommentDto requestCommentDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Comment comment = Comment.getEntityInstance(requestCommentDto);
        comment.setSalesItem(salesItem);
        commentRepository.save(comment); //연관관계 매핑
        return ResponseCommentDto.fromEntity(comment);
    }

    public Page<ResponseCommentDto> findAllEntityByItem(Long itemId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        return commentRepository.findBySalesItem(salesItem, pageable).map(ResponseCommentDto::fromEntity);
    }

    public ResponseCommentDto findOneById(Long itemId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.validItemIdInURL(itemId);

        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateComment(Long itemId, Long commentId, RequestCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.validItemIdInURL(itemId);

//        comment.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());//아이디 비번 검증
        comment.update(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateCommentReply(Long itemId, Long commentId, CommentReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.validItemIdInURL(itemId);

//        comment.getSalesItem().checkAuthAndThrowException(dto.getWriter(), dto.getPassword());//판매자의 아이디 비번 검증
        comment.updateReply(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long itemId, Long commentId, DeleteCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.validItemIdInURL(itemId);//아이템에 속한 코멘트가 맞는지 확인한다.

//        comment.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());//제안 작석장의 아이디 비번을 검증한다.
        comment.getSalesItem().deleteComment(comment); //객체 사이에서도 제거한다.
        commentRepository.deleteById(commentId);
    }
}
