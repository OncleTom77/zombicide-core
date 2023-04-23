package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.zones.Position

class ZombieMoved(
    turn: Int,
    override val actorId: ActorId,
    val position: Position
) : Event<ZombieMoved>(turn),
    ActorEvent,
    ActionEvent,
    ZoneEvent
