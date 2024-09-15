package com.fouan.actors.zombies

import com.fouan.actors.Actor
import com.fouan.actors.ActorId

abstract class Zombie protected constructor(
    id: ActorId,
    val name: String,
    val damage: Int,
    val minDamageToDestroy: Int,
    val experienceProvided: Int,
    actionsCount: Int
) : Actor(
    id,
    actionsCount
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Zombie

        if (name != other.name) return false
        if (damage != other.damage) return false
        if (minDamageToDestroy != other.minDamageToDestroy) return false
        if (experienceProvided != other.experienceProvided) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + damage
        result = 31 * result + minDamageToDestroy
        result = 31 * result + experienceProvided
        return result
    }

    override fun toString(): String {
        return "Zombie(id=$id, name='$name', damage=$damage, minDamageToDestroy=$minDamageToDestroy, experienceProvided=$experienceProvided, actionsCount=$actionsCount)"
    }
}
