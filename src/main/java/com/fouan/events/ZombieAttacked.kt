package com.fouan.events

import com.fouan.actors.ActorId

class ZombieAttacked(turn: Int, override val actorId: ActorId) :
    Event<ZombieAttacked>(turn), ActorEvent, ActionEvent