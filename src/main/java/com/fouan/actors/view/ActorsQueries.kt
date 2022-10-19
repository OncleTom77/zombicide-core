package com.fouan.actors.view

import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie
import com.fouan.zones.Zone
import java.util.*
import java.util.stream.Stream

interface ActorsQueries {
    fun findSurvivorBy(id: ActorId): Optional<Survivor>
    fun allLivingSurvivors(): Stream<Survivor>
    fun findSurvivorWithZoneBy(id: ActorId): Optional<SurvivorWithZone>
    fun findZombieBy(id: ActorId): Optional<Zombie>
    fun findZombieWithZoneBy(id: ActorId): Optional<ZombieWithZone>
    fun findZombiesWithZoneNearTo(zone: Zone): List<ZombieWithZone>
    fun findRemainingActionsForSurvivor(survivor: Survivor, turn: Int): Int
}