package com.lahee.market.dto.negotiation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestNegotiationDto {
    @NotBlank
    String writer;
    @NotBlank
    String password;
    @NotNull @PositiveOrZero
    Integer suggestedPrice;
}
