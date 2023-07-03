package com.lahee.market.service;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.*;
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
        Comment comment = Comment.postNewComment(salesItem, requestCommentDto);
        salesItem.addComment(comment);
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
        if (comment.getSalesItem().getId() != itemId) {
            throw new CommentNotMatchItemException();
        }
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateComment(Long itemId, Long commentId, RequestCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (comment.getSalesItem().getId() != itemId) {
            throw new CommentNotMatchItemException();
        }

        //아이디 비번 검증
        checkCommentWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), comment);
        comment.update(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateCommentReply(Long itemId, Long commentId, CommentReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (comment.getSalesItem().getId() != itemId) {
            throw new CommentNotMatchItemException();
        }
        SalesItem salesItem = comment.getSalesItem();

        //글을 올린 사람이 맞는지 아이디 비번 검증
        checkItemWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), salesItem);
        comment.updateReply(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long itemId, Long commentId, DeleteCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (comment.getSalesItem().getId() != itemId) {
            throw new CommentNotMatchItemException();
        }

        //댓글을 올린사람이 맞는지 아이디와 비번 검증후 틀리는 경우 에러를 던진다.
        checkCommentWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), comment);
        comment.getSalesItem().deleteComment(comment); //연관관계 메서드
        commentRepository.deleteById(commentId);
    }


    private static void checkCommentWriterAndPasswordAndThrowException(String writer, String password, Comment comment) {
        if (!comment.getWriter().equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!comment.getPassword().equals(password)) {
            throw new PasswordNotMatchException();
        }
    }

    private static void checkItemWriterAndPasswordAndThrowException(String writer, String password, SalesItem item) {
        if (!item.getWriter().equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!item.getPassword().equals(password)) {
            throw new PasswordNotMatchException();
        }
    }
}
