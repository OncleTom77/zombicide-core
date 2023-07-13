package com.fouan.events

import com.fouan.actors.DangerLevel
import com.fouan.spawn.ZombieSpawnCard
import com.fouan.zones.Zone

class ZombieSpawnZoneAdded(
    turn: Int,
    val zone: Zone,
    val dangerLevel: DangerLevel,
    val spawnInfo: ZombieSpawnCard.SpawnInfo
) : Event<ZombieSpawnZoneAdded>(turn), ZoneEvent
