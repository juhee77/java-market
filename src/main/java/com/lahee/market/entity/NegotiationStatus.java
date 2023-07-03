package com.lahee.market.entity;

public enum NegotiationStatus {
    SUGGEST("제안"), REJECT("거절");

    private String name;

    NegotiationStatus(String name) {
        this.name = name;
    }
}
