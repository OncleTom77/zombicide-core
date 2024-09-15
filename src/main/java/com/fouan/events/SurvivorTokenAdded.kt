package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.actors.view.SurvivorToken

class SurvivorTokenAdded(turn: Int, val survivorId: ActorId, val token: SurvivorToken) :
    Event<SurvivorTokenAdded>(turn),
    ActorEvent
