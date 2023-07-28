package com.lahee.market.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> extends ResponseEntity<T> {
    private T data;

    public ApiResponse(HttpStatusCode status) {
        super(status);
    }

    public ApiResponse(HttpStatus httpStatus, T data) {
        super(data, httpStatus);
        this.data = data;
    }

    public void setData(T data) {
        this.data = data;
    }
}