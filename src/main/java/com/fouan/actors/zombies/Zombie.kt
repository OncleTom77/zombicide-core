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

    override fun toString(): String {
        return "Zombie(name=$name, damage=$damage, minDamageToDestroy=$minDamageToDestroy, experienceProvided=$experienceProvided)"
    }
}
