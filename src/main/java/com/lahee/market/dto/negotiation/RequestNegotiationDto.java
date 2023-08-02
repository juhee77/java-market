package com.lahee.market.dto.negotiation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestNegotiationDto {
    @NotNull @PositiveOrZero
    Integer suggestedPrice;
}
