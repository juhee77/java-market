package com.lahee.market.exception;

public class ItemNotFoundException extends Status404Exception {
    public ItemNotFoundException() {
        super("찾을 수 없는 아이템 입니다.");
    }
}