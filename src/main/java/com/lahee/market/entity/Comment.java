package com.lahee.market.entity;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.exception.PasswordNotMatchException;
import com.lahee.market.exception.WriterNameNotMatchException;
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
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static Comment getEntityInstance(RequestCommentDto dto) {
        Comment comment = new Comment();
        comment.writer = dto.getWriter();
        comment.password = dto.getPassword();
        comment.content = dto.getContent();
        return comment;
    }

    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) {
            this.salesItem.getComments().remove(this); //이전에 관계가 매핑 되어있다면 제거한다.
        }
        this.salesItem = item;
        item.addComment(this);
    }

    public void update(RequestCommentDto dto) {
        this.content = dto.getContent();
    }

    public void updateReply(CommentReplyDto dto) {
        this.reply = dto.getReply();
    }

    //인증 메서드
    public void checkAuthAndThrowException(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!this.password.equals(password)) {
            throw new PasswordNotMatchException();
        }
    }
}
