package com.lahee.market.exception;

public class ItemNotMatchUserException extends Status403Exception {
    public ItemNotMatchUserException() {
        super("제안 번호와 아이템의 유저가 잘못 요청 되었습니다. - 해당 유저가 진행할 수 있는 요청이 아닙니다.");
    }
}