package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.actors.zombies.Zombie
import com.fouan.weapons.Weapon

class ZombieDied(
    turn: Int,
    val zombie: Zombie,
    val actorId: ActorId,
    val weaponUsed: Weapon
) : Event<ZombieDied>(turn), ActorEvent, ZoneEvent