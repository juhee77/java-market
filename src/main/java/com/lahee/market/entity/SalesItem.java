package com.lahee.market.entity;

import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.exception.ItemNotMatchUserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "sales_item")
public class SalesItem {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = LAZY, mappedBy = "salesItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "salesItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();


    public static SalesItem getEntityInstance(RequestSalesItemDto requestSalesItemDto, User user) {
        SalesItem salesItem = new SalesItem();
        salesItem.description = requestSalesItemDto.getDescription();
        salesItem.title = requestSalesItemDto.getTitle();
        salesItem.minPriceWanted = requestSalesItemDto.getMinPriceWanted();
        salesItem.status = ItemStatus.SELL;
        salesItem.addUser(user);
        return salesItem;
    }

    public void update(RequestSalesItemDto requestDto) {
        this.description = requestDto.getDescription();
        this.title = requestDto.getTitle();
        this.minPriceWanted = requestDto.getMinPriceWanted();
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //연관관계 편의 메소드
    private void addUser(User user) {
        this.user = user;
        user.addItem(this);
    }

    public void addComment(Comment comment) {
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
    }

    public void addNegotiation(Negotiation negotiation) {
        if (!negotiations.contains(negotiation)) {
            negotiations.add(negotiation);
        }
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public void deleteNegotiation(Negotiation negotiation) {
        negotiations.remove(negotiation);
    }

    public void updateSoldOutStatus() {
        this.status = ItemStatus.SOLD;
    }

    //해당 유저가 등록한 아이템이 아니다.
    public void validItemIdInURL(User user) {
        if (!this.user.equals(user)) {
            throw new ItemNotMatchUserException();
        }
    }
}
