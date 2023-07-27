package com.lahee.market.entity;

import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.exception.NegotiationNotMatchItemException;
import com.lahee.market.exception.PasswordNotMatchException;
import com.lahee.market.exception.WriterNameNotMatchException;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "negotiation")
public class Negotiation {
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

    public static Negotiation getEntityInstance(RequestNegotiationDto dto) {
        Negotiation negotiation = new Negotiation();
        negotiation.suggestedPrice = dto.getSuggestedPrice();
        negotiation.status = NegotiationStatus.SUGGEST;
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

    //아이템에 속한 제안이 맞는지 확인한다.
    public void validItemIdInURL(Long itemId) {
        if (itemId != this.salesItem.getId()) {
            throw new NegotiationNotMatchItemException();
        }
    }
}

