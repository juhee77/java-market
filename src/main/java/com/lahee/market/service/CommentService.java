package com.lahee.market.service;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.entity.User;
import com.lahee.market.exception.CommentNotFoundException;
import com.lahee.market.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final SalesItemService salesItemService;

    @Transactional
    public ResponseCommentDto save(Long itemId, RequestCommentDto requestCommentDto, String username) {
        User user = userService.getUser(username);
        SalesItem salesItem = salesItemService.getSalesItem(itemId);
        Comment comment = Comment.getEntityInstance(requestCommentDto, user, salesItem);
        return ResponseCommentDto.fromEntity(commentRepository.save(comment));
    }

    public Page<ResponseCommentDto> findAllEntityByItem(Long itemId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        SalesItem salesItem = salesItemService.getSalesItem(itemId);
        return commentRepository.findBySalesItem(salesItem, pageable).map(ResponseCommentDto::fromEntity);
    }

    public ResponseCommentDto findOneById(Long itemId, Long commentId) {
        Comment comment = getComment(commentId);
        comment.validItemIdInURL(itemId);

        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateComment(Long itemId, Long commentId, RequestCommentDto dto, String username) {
        User user = userService.getUser(username); //댓글 작성자가 자신이 작성한 댓글을 수정하려 하는 경우
        Comment comment = getComment(commentId);
        comment.validItemIdInURL(itemId);
        comment.validCommentUser(user);
        comment.update(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public ResponseCommentDto updateCommentReply(Long itemId, Long commentId, CommentReplyDto dto, String username) {
        User user = userService.getUser(username); //아이템 판매자가 자신의 글에 달린 댓글들에 대댓글을 단다
        Comment comment = getComment(commentId);
        comment.validItemIdInURL(itemId);
        comment.validItemUser(user);
        comment.updateReply(dto);
        return ResponseCommentDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long itemId, Long commentId, String username) {
        User user = userService.getUser(username);
        Comment comment = getComment(commentId);
        comment.validItemIdInURL(itemId);//아이템에 속한 코멘트가 맞는지 확인한다.
        comment.validCommentUser(user);

        comment.delete();//객체 사이에서도 제거한다.
        commentRepository.deleteById(commentId);
    }

    public Comment getComment(Long commendId) {
        Optional<Comment> comment = commentRepository.findById(commendId);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

}
