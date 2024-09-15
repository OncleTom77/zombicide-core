package com.fouan.actors

abstract class Actor(val id: ActorId, val actionsCount: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Actor

        if (id != other.id) return false
        if (actionsCount != other.actionsCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + actionsCount
        return result
    }
}