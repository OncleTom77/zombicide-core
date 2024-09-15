package com.fouan.events

import com.fouan.actors.ActorId

class SurvivorGainedExperience(turn: Int, val survivorId: ActorId, val gainedExperiences: Int) :
    Event<SurvivorGainedExperience>(turn), ActorEvent
