package com.nowcoder.wenda.sync;

public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    FOLLOW(4),
    UNFOLLOW(5),
    MAIL(3),
    ADD_QUESTION(6);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
