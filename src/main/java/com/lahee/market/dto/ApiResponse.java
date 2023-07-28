package com.lahee.market.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> extends ResponseEntity<T> {
    public ApiResponse(HttpStatus httpStatus, T data) {
        super(data, httpStatus);
    }
}