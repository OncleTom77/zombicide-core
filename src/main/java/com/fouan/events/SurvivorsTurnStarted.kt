package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorsTurnStarted(turn: Int, val survivorId: ActorId) : Event<SurvivorsTurnStarted>(turn), ActorEvent,
    GameEvent