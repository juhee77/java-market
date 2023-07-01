package com.lahee.market.entity;

import com.lahee.market.dto.RequestSalesItemDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "sales_item")
public class SalesItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;
    private String writer;
    private String password;


    public static SalesItem postNewItem(RequestSalesItemDto requestSalesItemDto) {
        SalesItem salesItem = new SalesItem();
        salesItem.description = requestSalesItemDto.getDescription();
        salesItem.title = requestSalesItemDto.getTitle();
        salesItem.minPriceWanted = requestSalesItemDto.getMinPriceWanted();
        salesItem.status = "판매중";
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
}
