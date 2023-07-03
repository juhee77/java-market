package com.lahee.market.entity;

import java.util.Arrays;

public enum NegotiationStatus {
    SUGGEST("제안"), REJECT("거절"), ACCEPT("수락"), CONFIRMATION("확정");

    private final String name;

    NegotiationStatus(String name) {
        this.name = name;
    }

    public static NegotiationStatus findNegotiationStatus(String name) {
        return Arrays.stream(values()).filter(o -> o.name.equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("정의 되지 않은 상태"));
    }

    public String getName() {
        return name;
    }
}
