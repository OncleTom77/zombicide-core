package com.fouan.zones;

import lombok.EqualsAndHashCode;

import java.util.Comparator;

@EqualsAndHashCode
public final class Position {
    public static final Comparator<Position> BOARD_COMPARATOR = (o1, o2) -> {
        if (o1.y == o2.y) {
            return o1.x - o2.x;
        }
        return o1.y - o2.y;
    };

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position plusX(int value) {
        return new Position(x + value, y);
    }

    public Position minusX(int value) {
        return plusX(-value);
    }

    public Position plusY(int value) {
        return new Position(x, y + value);
    }

    public Position minusY(int value) {
        return plusY(-value);
    }

    public int computeDistance(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
