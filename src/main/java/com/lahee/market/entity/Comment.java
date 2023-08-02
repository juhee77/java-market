package com.lahee.market.entity;

import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Comment extends BaseEntity {
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

    public static Comment getEntityInstance(RequestCommentDto dto, User user, SalesItem salesItem) {
        Comment comment = new Comment();
        comment.content = dto.getContent();
        comment.setUser(user);
        comment.setSalesItem(salesItem);
        return comment;
    }

    public void update(RequestCommentDto dto) {
        this.content = dto.getContent();
    }

    public void updateReply(CommentReplyDto dto) {
        this.reply = dto.getReply();
    }

    //연관관계 편의 메서드
    private void setUser(User user) {
        if (this.user != null) {
            this.user.getComments().remove(this);
        }
        this.user = user;
        user.addComment(this);
    }

    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) {
            this.salesItem.getComments().remove(this); //이전에 관계가 매핑 되어있다면 제거한다.
        }
        this.salesItem = item;
        item.addComment(this);
    }

    public void delete() {
        user.deleteComment(this);
        salesItem.deleteComment(this);
    }

    //인증메서드
    //아이템에 속한 코멘트가 맞는지 확인한다.
    public void validItemIdInURL(Long itemId) {
        if (salesItem.getId() != itemId) {
            throw new CustomException(ErrorCode.COMMENT_NOT_IN_ITEM_EXCEPTION);
        }
    }

    //아이템에 속한 유저와 로그인한 유저가 맞는지 확인한다.
    public void validCommentUser(User user) {
        if (this.user != user) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_USER);
        }
    }

    public void validItemUser(User user) {
        if (this.salesItem.getUser() != user) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_ITEM_USER);
        }
    }
}
