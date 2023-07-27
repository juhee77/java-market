package com.lahee.market.exception;

public class UserNotFoundException extends Status404Exception {
    public UserNotFoundException() {
        super("찾을 수 없는 유저 입니다.");
    }
}