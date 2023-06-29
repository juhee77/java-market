package com.lahee.market.dto;

import com.lahee.market.entity.SalesItem;
import lombok.Data;

@Data
public class ResponseSalesItemDto {
    Long id;
    String title;
    String description;
    Integer minPriceWanted;
    String status;

    public static ResponseSalesItemDto fromEntity(SalesItem item) {
        ResponseSalesItemDto dto = new ResponseSalesItemDto();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setTitle(item.getTitle());
        dto.setMinPriceWanted(item.getMinPriceWanted());
        dto.setStatus(item.getStatus());
        return dto;
    }
}
