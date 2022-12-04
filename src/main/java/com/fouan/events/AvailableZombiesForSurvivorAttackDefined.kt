package com.fouan.events

import com.fouan.actors.zombies.Zombie

class AvailableZombiesForSurvivorAttackDefined(turn: Int, val zombies: Set<Zombie>, val numberOfZombiesToChoose: Int) : Event<AvailableZombiesForSurvivorAttackDefined>(turn), GameEvent