package com.lahee.market.dto.negotiation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestNegotiationDto {
    String writer;
    String password;
    Integer suggestedPrice;
}
