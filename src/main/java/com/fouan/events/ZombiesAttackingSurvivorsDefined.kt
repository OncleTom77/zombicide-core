package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.zones.Position

class ZombiesAttackingSurvivorsDefined(
    turn: Int,
    val position: Position,
    val zombieIds: List<ActorId>,
    val survivorIds: List<ActorId>
) :
    Event<ZombiesAttackingSurvivorsDefined>(turn), ActorEvent