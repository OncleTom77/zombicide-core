package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.DangerLevel
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie
import java.util.*

interface ActorsQueries {
    fun findLivingSurvivorBy(id: ActorId): Optional<Survivor>
    fun allLivingSurvivors(): List<Survivor>
    fun findZombieBy(id: ActorId): Optional<Zombie>
    fun findZombieWithZoneBy(id: ActorId): Optional<ZombieWithZone>
    fun getRemainingActionsCountForActor(actorId: ActorId, turn: Int): Int
    fun findAllZombiesOnSameZoneAsSurvivor(survivorId: ActorId): Set<Zombie>
    fun findCurrentSurvivorIdForTurn(turn: Int): Optional<ActorId>
    fun findZombieIdsWithRemainingActions(turn: Int): List<ActorId>
    fun findAttackingZombies(): List<Zombie>
    fun findActorBy(id: ActorId): Actor
    fun getCurrentDangerLevel(): DangerLevel
}