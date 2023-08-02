package com.lahee.market.dto.salesItem;

import com.lahee.market.entity.SalesItem;
import lombok.Data;

@Data
public class ResponseSalesItemDto {
    Long id;
    String seller;
    String title;
    String description;
    Integer minPriceWanted;
    String status;
    String imageUrl;

    public static ResponseSalesItemDto fromEntity(SalesItem item) {
        ResponseSalesItemDto dto = new ResponseSalesItemDto();
        dto.setSeller(item.getUser().getNickname());
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setTitle(item.getTitle());
        dto.setMinPriceWanted(item.getMinPriceWanted());
        dto.setImageUrl(item.getImageUrl());
        dto.setStatus(item.getStatus().toString());
        return dto;
    }
}
