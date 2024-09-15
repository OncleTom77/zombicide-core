package com.fouan.events

import com.fouan.actors.ActorId

interface ActionEvent: GameEvent {

    val actorId: ActorId
}