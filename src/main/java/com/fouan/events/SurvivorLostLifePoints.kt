package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorLostLifePoints(
    turn: Int,
    val survivorId: ActorId,
    val attackerId: ActorId,
    val damage: Int
) : Event<SurvivorLostLifePoints>(turn), ActorEvent