package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorsTurnEnded(turn: Int, val actorId: ActorId) : Event<SurvivorsTurnEnded>(turn), ActorEvent, GameEvent