package com.lahee.market.service;

import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.repository.CommentRepository;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        salesItem.setItem(comment);
        commentRepository.save(comment);
        return ResponseCommentDto.fromEntity(comment);
    }
}
