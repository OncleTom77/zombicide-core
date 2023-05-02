package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorHealed(turn: Int, val survivorId: ActorId, val gainedLifePoints: Int) :
    Event<SurvivorHealed>(turn), ActorEvent
