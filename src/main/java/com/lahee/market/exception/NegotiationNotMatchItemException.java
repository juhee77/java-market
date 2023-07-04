package com.lahee.market.exception;

public class NegotiationNotMatchItemException extends Status400Exception {
    public NegotiationNotMatchItemException() {
        super("제안 번호와 아이템의 번호가 잘못 요청 되었습니다.");
    }
}