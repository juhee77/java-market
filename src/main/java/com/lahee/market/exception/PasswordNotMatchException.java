package com.lahee.market.exception;

public class PasswordNotMatchException extends Status403Exception {
    public PasswordNotMatchException() {
        super("비밀번호가 잘못 되었습니다.");
    }
}