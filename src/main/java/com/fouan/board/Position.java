package com.fouan.board;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Position {

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
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
