package com.lahee.market.exception;

public class StatusException extends Status400Exception {
    public StatusException() {
        super("지원하지 않는 STATUS 입니다. (수락, 거절, 확정)");
    }
}