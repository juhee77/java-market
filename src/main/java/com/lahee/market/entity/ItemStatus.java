package com.lahee.market.entity;

public enum ItemStatus {
    SALE("판매중"), SOLD_OUT("판매 완료");
    final String name;

    ItemStatus(String name) {
        this.name = name;
    }
}
