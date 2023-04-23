package com.fouan.events

import com.fouan.actors.zombies.Zombie
import com.fouan.zones.Zone

class ZombieSpawned(turn: Int, val zombie: Zombie, val zone: Zone) : Event<ZombieSpawned>(turn),
    ActorEvent, ZoneEvent
