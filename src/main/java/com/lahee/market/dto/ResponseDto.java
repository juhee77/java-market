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

    public static ResponseDto getInstance(Object object) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(object.toString());
        return responseDto;
    }
}
