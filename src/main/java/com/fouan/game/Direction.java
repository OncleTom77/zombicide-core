package com.fouan.game;

import com.fouan.board.Position;

import java.util.function.Function;

public enum Direction {
    UP(position -> position.plusY(1)),
    DOWN(position -> position.minusY(1)),
    LEFT(position -> position.minusX(1)),
    RIGHT(position -> position.plusX(1));

    private final Function<Position, Position> compute;

    Direction(Function<Position, Position> compute) {
        this.compute = compute;
    }

    public Position apply(Position position) {
        return compute.apply(position);
    }
}
