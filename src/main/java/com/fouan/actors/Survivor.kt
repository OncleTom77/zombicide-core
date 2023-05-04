package com.fouan.actors

import com.fouan.actors.view.LifeStatus
import com.fouan.weapons.Weapon

class Survivor(
    id: ActorId,
    val lifePoints: Int,
    val name: String,
    val weapon: Weapon,
    val experience: Int,
    actionsCount: Int,
) : Actor(
    id, actionsCount
) {
    val dangerLevel: DangerLevel = DangerLevel.fromExperience(experience)

    val lifeStatus: LifeStatus
        get() = if (lifePoints > 0) LifeStatus.ALIVE else LifeStatus.DEAD

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Survivor

        if (lifePoints != other.lifePoints) return false
        if (name != other.name) return false
        if (weapon != other.weapon) return false
        if (experience != other.experience) return false
        return dangerLevel == other.dangerLevel
    }

    override fun hashCode(): Int {
        var result = lifePoints
        result = 31 * result + name.hashCode()
        result = 31 * result + weapon.hashCode()
        result = 31 * result + experience
        result = 31 * result + dangerLevel.hashCode()
        return result
    }

    override fun toString(): String {
        return "Survivor(name='$name', lifePoints=$lifePoints)"
    }
}
