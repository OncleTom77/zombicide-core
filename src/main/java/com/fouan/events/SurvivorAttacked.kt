package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.weapons.AttackResult

class SurvivorAttacked(turn: Int, override val actorId: ActorId, val attackResult: AttackResult) :
    Event<SurvivorAttacked>(turn), ActorEvent, ActionEvent