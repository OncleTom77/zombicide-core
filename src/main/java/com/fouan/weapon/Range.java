package com.fouan.weapon;

public class Range {

    private final int min;
    private final int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(int value) {
        return min <= value && value <= max;
    }

    public int getMax() {
        return max;
    }
}
