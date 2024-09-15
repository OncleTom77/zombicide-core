package com.fouan.events

import com.fouan.actors.Survivor
import com.fouan.zones.Zone

class SurvivorAdded(turn: Int, val survivor: Survivor, val zone: Zone) : Event<SurvivorAdded>(turn),
    ActorEvent
