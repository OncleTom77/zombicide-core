package com.fouan.board;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Position {

    private final int x;
    private final int y;

    public Position plusX(int value) {
        return Position.builder()
                .x(x + value)
                .y(y)
                .build();
    }

    public Position minusX(int value) {
        return plusX(-value);
    }

    public Position plusY(int value) {
        return Position.builder()
                .x(x)
                .y(y + value)
                .build();
    }

    public Position minusY(int value) {
        return plusY(-value);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
