package com.lahee.market.dto.negotiation;


import com.lahee.market.annotation.NegoStatusWhiteList;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNegotiationDto {
    @PositiveOrZero
    Integer suggestedPrice;
    @NegoStatusWhiteList
    String status;
}
