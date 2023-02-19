package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorDied(turn: Int, val survivorId: ActorId) : Event<SurvivorDied>(turn), ActorEvent