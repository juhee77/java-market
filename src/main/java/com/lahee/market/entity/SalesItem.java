package com.lahee.market.entity;

import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.exception.PasswordNotMatchException;
import com.lahee.market.exception.WriterNameNotMatchException;
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
    private String writer;
    private String password;

    @OneToMany(fetch = LAZY, mappedBy = "salesItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "salesItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();


    public static SalesItem getEntityInstance(RequestSalesItemDto requestSalesItemDto) {
        SalesItem salesItem = new SalesItem();
        salesItem.description = requestSalesItemDto.getDescription();
        salesItem.title = requestSalesItemDto.getTitle();
        salesItem.minPriceWanted = requestSalesItemDto.getMinPriceWanted();
        salesItem.status = ItemStatus.SELL;
        salesItem.writer = requestSalesItemDto.getWriter();
        salesItem.password = requestSalesItemDto.getPassword();
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

    //인증 메서드
    public void checkAuthAndThrowException(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!this.password.equals(password)) {
            throw new PasswordNotMatchException();
        }
    }

    public void updateSoldOutStatus() {
        this.status = ItemStatus.SOLD;
    }
}
