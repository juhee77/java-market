package com.lahee.market.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String message;

    public static ResponseDto getSuccessInstance(){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("success");
        return responseDto;
    }
}
