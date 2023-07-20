package com.prgms.springbootrestapi.domain;

import java.util.Arrays;

public enum Category {

    FAMILY("family", "가족"),
    ABSTRACT_STRATEGY("abstract strategy", "전략"),
    PARTY("party", "파티"),
    QUICKNESS("quickness", "순발력"),

    MAFIA("mafia", "마피아"),

    BLUFFING("bluffing", "속임"),
    NONE("none", "없음");

    private final String name;
    private final String displayName;

    Category(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public static Category of(String name) {
        return Arrays.stream(Category.values())
                    .filter(c -> c.name.equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 카테고리가 없습니다."));
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

}
