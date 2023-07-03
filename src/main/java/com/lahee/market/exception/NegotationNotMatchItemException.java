package com.lahee.market.exception;

public class NegotationNotMatchItemException extends Status400Exception {
    public NegotationNotMatchItemException() {
        super("제안 번호와 아이템의 번호가 잘못 요청 되었습니다.");
    }
}