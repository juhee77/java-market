package com.lahee.market.exception;

public class WriterNameNotMatchException extends Status403Exception {
    public WriterNameNotMatchException() {
        super("아이디가 잘못 되었습니다.");
    }
}