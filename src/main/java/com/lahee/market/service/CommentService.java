package com.lahee.market.service;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.CommentNotFoundException;
import com.lahee.market.exception.CommentNotMatchItemException;
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
        commentRepository.save(comment);
        return ResponseCommentDto.fromEntity(comment);
    }

    public Page<ResponseCommentDto> findAllEntityByItem(Long itemId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        return commentRepository.findBySalesItem(salesItem, pageable).map(ResponseCommentDto::fromEntity);
    }

    public ResponseCommentDto findOneById(Long itemId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validItemComment(itemId, comment);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateComment(Long itemId, Long commentId, RequestCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validItemComment(itemId, comment);

        //아이디 비번 검증
        comment.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());
        comment.update(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateCommentReply(Long itemId, Long commentId, CommentReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validItemComment(itemId, comment);

        //판매자의 아이디 비번 검증
        SalesItem salesItem = comment.getSalesItem();
        salesItem.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());

        comment.updateReply(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long itemId, Long commentId, DeleteCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validItemComment(itemId, comment);

        //제안 작석장의 아이디 비번검증
        comment.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());
        comment.getSalesItem().deleteComment(comment); //객체 사이에서도 제거한다.
        commentRepository.deleteById(commentId);
    }

    private static void validItemComment(Long itemId, Comment comment) {
        if (comment.getSalesItem().getId() != itemId) {
            throw new CommentNotMatchItemException();
        }
    }
}
