package com.fouan.events

import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie

class ZombieAttacksDistributed(
    turn: Int,
    val distribution: Map<Survivor, List<Zombie>>
) :
    Event<ZombieAttacksDistributed>(turn), ActorEvent