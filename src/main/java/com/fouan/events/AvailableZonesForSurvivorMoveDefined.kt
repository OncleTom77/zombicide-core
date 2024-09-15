package com.fouan.events

import com.fouan.zones.Zone

class AvailableZonesForSurvivorMoveDefined(turn: Int, val zones: List<Zone>) :
    Event<AvailableZonesForSurvivorMoveDefined>(turn),
    GameEvent
