package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.weapons.Weapon

class ZombieDied(
    turn: Int,
    val zombieId: ActorId,
    val actorId: ActorId,
    val weaponUsed: Weapon
) : Event<ZombieDied>(turn), ActorEvent, ZoneEvent