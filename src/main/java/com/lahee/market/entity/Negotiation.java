package com.lahee.market.entity;

import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "negotiation")
@SQLDelete(sql = "UPDATE negotiation SET deleted_at = datetime('now')  WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Negotiation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;
    private Integer suggestedPrice;

    @Enumerated(EnumType.STRING)
    private NegotiationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Negotiation getEntityInstance(RequestNegotiationDto dto, SalesItem item, User user) {
        Negotiation negotiation = new Negotiation();
        negotiation.suggestedPrice = dto.getSuggestedPrice();
        negotiation.status = NegotiationStatus.SUGGEST;
        negotiation.setSalesItem(item); //연관관계 매핑
        negotiation.setUser(user);
        return negotiation;
    }

    public void update(UpdateNegotiationDto dto) {
        this.suggestedPrice = dto.getSuggestedPrice();
    }

    public void updateStatus(NegotiationStatus status) {
        this.status = status;
    }

    public void acceptStatus() {
        this.status = NegotiationStatus.CONFIRMATION;
    }

    //연관관계 편의 메서드
    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) {
            this.salesItem.getNegotiations().remove(this); //이전에 관계가 매핑 되어있다면 제거한다.
        }
        this.salesItem = item;
        item.addNegotiation(this);
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getNegotiations().remove(this);
        }
        this.user = user;
        user.addNegotiation(this);
    }

    //아이템에 속한 제안이 맞는지 확인한다.
    public void validItemIdInURL(Long itemId) {
        if (!itemId.equals(this.salesItem.getId())) {
            throw new CustomException(ErrorCode.NEGOTIATION_NOT_ITEM_EXCEPTION);
        }
    }

    public void validNegotiationUser(User user) {
        if (this.user != user) {
            throw new CustomException(ErrorCode.INVALID_NEGOTIATION_EXCEPTION);
        }
    }

    public void validItemUser(User user) {
        if (this.salesItem.getUser() != user) {
            throw new CustomException(ErrorCode.INVALID_NEGOTIATION_USER_EXCEPTION);
        }
    }

    public void delete() {
        salesItem.deleteNegotiation(this);
        user.deleteNegotiation(this);
    }
}

