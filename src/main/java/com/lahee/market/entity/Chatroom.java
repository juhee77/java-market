package com.lahee.market.entity;

import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE chatroom SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Chatroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;

    @ManyToOne
    private SalesItem item;

    @ManyToOne
    private User seller;

    @ManyToOne
    private User suggester;

    private boolean isActive; //판매자가 수락을 했는지 여부

    public static Chatroom getEntityInstance(String roomName, SalesItem item, User seller, User suggester) {
        Chatroom chatroom = new Chatroom();
        chatroom.item = item;
        chatroom.suggester = suggester;
        chatroom.seller = seller;
        chatroom.roomName = roomName;
        chatroom.isActive = false;
        return chatroom;
    }

    //인증 메서드
    public void validateUser(User user) {
        if (!seller.equals(user) && !suggester.equals(user)) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ROOM_USER);
        }
    }

    public void updateActivate() {
        isActive = true;
    }
}
