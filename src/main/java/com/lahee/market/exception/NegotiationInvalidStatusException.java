package com.lahee.market.exception;

public class NegotiationInvalidStatusException extends Status400Exception {
    public NegotiationInvalidStatusException() {
        super("판매자가 제안을 수락하지 않은 상태 입니다.");
    }
}