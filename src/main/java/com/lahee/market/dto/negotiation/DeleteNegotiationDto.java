package com.lahee.market.dto.negotiation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteNegotiationDto {
    @NotBlank
    String writer;
    @NotBlank
    String password;
}
