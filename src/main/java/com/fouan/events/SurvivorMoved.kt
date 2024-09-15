package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.zones.Position

class SurvivorMoved(turn: Int, override val actorId: ActorId, val position: Position) : Event<SurvivorMoved>(turn),
    ZoneEvent, ActorEvent, ActionEvent
