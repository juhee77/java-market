package com.lahee.market.dto.negotiation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateNegotiationDto {
    String writer;
    String password;
    Integer suggestedPrice;
    String status;
}
