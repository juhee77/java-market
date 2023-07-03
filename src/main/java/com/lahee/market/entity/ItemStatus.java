package com.lahee.market.entity;

public enum ItemStatus {
    SELL("판매중"), SOLD("판매 완료");
    final String name;

    ItemStatus(String name) {
        this.name = name;
    }
}
