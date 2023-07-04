package com.lahee.market.exception;

public class InvalidRequestException extends Status400Exception {
    public InvalidRequestException() {
        super("잘못된 요청 입니다.");
    }
}