package com.lahee.market.dto.salesItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RequestSalesItemDto {
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotNull @PositiveOrZero
    Integer minPriceWanted;
    @NotBlank
    String writer;
    @NotBlank
    String password;

    public RequestSalesItemDto(String title, String description, Integer minPriceWanted, String writer, String password) {
        this.title = title;
        this.description = description;
        this.minPriceWanted = minPriceWanted;
        this.writer = writer;
        this.password = password;
    }
}
