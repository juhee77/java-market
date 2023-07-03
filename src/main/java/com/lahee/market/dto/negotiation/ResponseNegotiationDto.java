package com.lahee.market.dto.negotiation;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.NegotiationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseNegotiationDto {
    Long id;
    Integer suggestedPrice;
    NegotiationStatus status;


    public static ResponseNegotiationDto fromEntity(Negotiation negotiation) {
        return new ResponseNegotiationDto(negotiation.getId(), negotiation.getSuggestedPrice(), negotiation.getStatus());
    }
}
