package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.DangerLevel
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie

interface ActorsQueries {
    fun findLivingSurvivorBy(id: ActorId): Survivor?
    fun allLivingSurvivors(): List<Survivor>
    fun findZombieBy(id: ActorId): Zombie?
    fun getRemainingActionsCountForActor(actorId: ActorId, turn: Int): Int
    fun findAllZombiesOnSameZoneAsSurvivor(survivorId: ActorId): Set<Zombie>
    fun findCurrentSurvivorIdForTurn(turn: Int): ActorId?
    fun findZombieIdsWithRemainingActions(turn: Int): List<ActorId>
    fun findAttackingZombies(): List<Zombie>
    fun findActorBy(id: ActorId): Actor
    fun getCurrentDangerLevel(): DangerLevel
}