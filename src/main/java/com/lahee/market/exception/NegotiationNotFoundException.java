package com.lahee.market.exception;

public class NegotiationNotFoundException extends Status404Exception {
    public NegotiationNotFoundException() {
        super("찾을 수 없는 제안 입니다.");
    }
}