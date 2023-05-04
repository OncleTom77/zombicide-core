package com.fouan.zones

import kotlin.math.abs

class Position(val x: Int, private val y: Int) {
    fun plusX(value: Int): Position = Position(x + value, y)

    fun minusX(value: Int): Position = plusX(-value)

    fun plusY(value: Int): Position = Position(x, y + value)

    fun minusY(value: Int): Position = plusY(-value)

    fun computeDistance(other: Position): Int = abs(x - other.x) + abs(y - other.y)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        return y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

    companion object {
        val BOARD_COMPARATOR: Comparator<Position> = Comparator.comparing(Position::y)
            .thenComparing(Position::x)
    }
}
