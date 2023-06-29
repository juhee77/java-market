package com.lahee.market.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    Long id;
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
}
