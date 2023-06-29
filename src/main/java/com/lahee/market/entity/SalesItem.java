package com.lahee.market.entity;

import com.lahee.market.dto.RequsetSalesItemDto;
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


    public static SalesItem postNewItem(RequsetSalesItemDto requsetSalesItemDto) {
        SalesItem salesItem = new SalesItem();
        salesItem.description = requsetSalesItemDto.getDescription();
        salesItem.title = requsetSalesItemDto.getTitle();
        salesItem.minPriceWanted = requsetSalesItemDto.getMinPriceWanted();
        salesItem.status = "판매중";
        salesItem.writer = requsetSalesItemDto.getWriter();
        salesItem.password = requsetSalesItemDto.getPassword();
        return salesItem;
    }
}
