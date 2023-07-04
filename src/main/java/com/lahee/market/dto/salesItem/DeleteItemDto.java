package com.lahee.market.dto.salesItem;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteItemDto {
    @NotBlank
    String writer;
    @NotBlank
    String password;
}
