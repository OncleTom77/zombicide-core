package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.zones.Position

class ZombieMoved(
    turn: Int,
    override val actorId: ActorId,
    val position: Position
) : Event<ZombieMoved>(turn),
    ActorEvent,
    ActionEvent,
    ZoneEvent {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ZombieMoved

        if (actorId != other.actorId) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + actorId.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }

    override fun toString(): String {
        return "ZombieMoved(turn=$turn, actorId=$actorId, position=$position)"
    }
}
