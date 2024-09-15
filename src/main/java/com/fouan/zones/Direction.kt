package com.fouan.zones

enum class Direction(private val compute: (Position) -> Position) {
    UP({ position -> position.plusY(1) }),
    DOWN({ position -> position.minusY(1) }),
    LEFT({ position -> position.minusX(1) }),
    RIGHT({ position -> position.plusX(1) });

    fun apply(position: Position): Position {
        return compute.invoke(position)
    }
}
