package com.lahee.market.dto.salesItem;

import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseSalesItemEachDto {
    Long id;
    String seller;
    String title;
    String description;
    Integer minPriceWanted;
    String status;
    List<ResponseCommentDto> comments;
    String imageUrl;

    public static ResponseSalesItemEachDto fromEntity(SalesItem item) {
        ResponseSalesItemEachDto dto = new ResponseSalesItemEachDto();
        dto.setSeller(item.getUser().getNickname());
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setTitle(item.getTitle());
        dto.setMinPriceWanted(item.getMinPriceWanted());
        dto.setStatus(item.getStatus().toString());
        dto.setImageUrl(item.getImageUrl());

        dto.comments = new ArrayList<>();
        for (Comment comment : item.getComments()) {
            dto.comments.add(ResponseCommentDto.fromEntity(comment));
        }
        return dto;
    }
}
