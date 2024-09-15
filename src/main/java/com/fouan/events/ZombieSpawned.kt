package com.fouan.events

import com.fouan.actors.zombies.Zombie
import com.fouan.zones.Position

class ZombieSpawned(turn: Int, val zombie: Zombie, val position: Position) : Event<ZombieSpawned>(turn),
    ActorEvent, ZoneEvent {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ZombieSpawned

        if (zombie != other.zombie) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + zombie.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }

    override fun toString(): String {
        return "ZombieSpawned(turn=$turn, zombie=$zombie, position=$position)"
    }
}

