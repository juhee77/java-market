package com.lahee.market.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestSalesItemDto {
    @NotEmpty
    String title;
    @NotEmpty
    String description;
    @NotNull
    Integer minPriceWanted;
    @NotEmpty
    String writer;
    @NotEmpty
    String password;

    public RequestSalesItemDto(String title, String description, Integer minPriceWanted, String writer, String password) {
        this.title = title;
        this.description = description;
        this.minPriceWanted = minPriceWanted;
        this.writer = writer;
        this.password = password;
    }
}
