package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie
import java.util.*

interface ActorsQueries {
    fun findSurvivorBy(id: ActorId): Optional<Survivor>
    fun allLivingSurvivors(): MutableList<Survivor>
    fun findZombieBy(id: ActorId): Optional<Zombie>
    fun findZombieWithZoneBy(id: ActorId): Optional<ZombieWithZone>
    fun findRemainingActionsForSurvivor(survivor: Survivor, turn: Int): Int
    fun findAllZombiesOnSameZoneAsSurvivor(survivorId: ActorId): Set<Zombie>
    fun findCurrentSurvivorIdForTurn(turn: Int): Optional<ActorId>
    fun allZombieActionsSpent(): Boolean
    fun findAttackingZombies(): MutableList<Zombie>
    fun findActorBy(id: ActorId): Actor
}