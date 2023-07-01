package com.lahee.market.entity;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sales_item")
    private SalesItem salesItem;
    private String writer;
    private String password;
    private String content;
    private String reply;


    public static Comment postNewComment(SalesItem item, RequestCommentDto dto) {
        Comment comment = new Comment();
        comment.writer = dto.getWriter();
        comment.password = dto.getPassword();
        comment.salesItem = item;
        comment.content = dto.getContent();
        return comment;
    }

    public void update(RequestCommentDto dto) {
        this.content = dto.getContent();
    }

    public void updateReply(CommentReplyDto dto) {
        this.reply = dto.getReply();
    }
}
