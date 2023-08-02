package com.lahee.market.dto.salesItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSalesItemDto {
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotNull @PositiveOrZero
    Integer minPriceWanted;
}
