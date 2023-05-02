package com.fouan.events

import com.fouan.actors.DangerLevel
import com.fouan.old.cards.SpawnInfo
import com.fouan.zones.Zone

class ZombieSpawnZoneAdded(
    turn: Int,
    val zone: Zone,
    val dangerLevel: DangerLevel,
    val spawnInfo: SpawnInfo
) : Event<ZombieSpawnZoneAdded>(turn), ZoneEvent
