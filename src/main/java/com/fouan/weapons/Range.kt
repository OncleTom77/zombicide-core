package com.fouan.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Range {

    private final int min;

    @Getter
    private final int max;

    public boolean contains(int value) {
        return min <= value && value <= max;
    }
}
