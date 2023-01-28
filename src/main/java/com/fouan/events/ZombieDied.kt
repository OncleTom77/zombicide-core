package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.weapons.Weapon
import lombok.Getter

class ZombieDied(
    turn: Int,
    val zombieId: ActorId,
    val attackerId: ActorId,
    val weaponUsed: Weapon
) : Event<ZombieDied>(turn), ActorEvent, ZoneEvent