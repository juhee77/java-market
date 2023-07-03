package com.lahee.market.entity;

public enum NegotiationStatus {
    SUGGEST("제안"), REJECT("거절");

    private final String name;

    NegotiationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
