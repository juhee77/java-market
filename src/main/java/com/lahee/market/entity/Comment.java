package com.lahee.market.entity;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.exception.CommentNotMatchItemException;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private String reply;

    public static Comment getEntityInstance(RequestCommentDto dto) {
        Comment comment = new Comment();
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


    //아이템에 속한 코멘트가 맞는지 확인한다.
    public void validItemIdInURL(Long itemId) {
        if (salesItem.getId() != itemId) {
            throw new CommentNotMatchItemException();
        }
    }
}
