package com.lahee.market.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String message;

    public static ResponseDto getInstance(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }

}
