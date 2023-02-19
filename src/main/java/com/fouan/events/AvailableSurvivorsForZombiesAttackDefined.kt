package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.zones.Position

class AvailableSurvivorsForZombiesAttackDefined(
    turn: Int,
    val position: Position,
    val zombieIds: List<ActorId>,
    val survivorIds: List<ActorId>
) :
    Event<AvailableSurvivorsForZombiesAttackDefined>(turn), ActorEvent