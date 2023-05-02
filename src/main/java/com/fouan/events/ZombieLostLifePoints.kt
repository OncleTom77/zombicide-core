package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.weapons.Weapon

class ZombieLostLifePoints(
    turn: Int,
    val zombieId: ActorId,
    val attackerId: ActorId,
    val weaponUsed: Weapon,
    val damage: Int
) : Event<ZombieLostLifePoints>(turn), ActorEvent
