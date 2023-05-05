package com.fouan.spawn

import com.fouan.actors.DangerLevel
import com.fouan.actors.zombies.Walker
import com.fouan.actors.zombies.Zombie
import kotlin.reflect.KClass

class ZombieSpawnCard private constructor(
    private val name: String,
    private val spawnInfoPerDangerLevel: Map<DangerLevel, SpawnInfo>
) {
    fun getSpawnInfo(level: DangerLevel): SpawnInfo? = spawnInfoPerDangerLevel[level]

    companion object {
        @JvmField
        val WalkerSpawnCard = ZombieSpawnCard(
            "Walker", mapOf(
                Pair(DangerLevel.BLUE, SpawnInfo(1, Walker::class)),
                Pair(DangerLevel.YELLOW, SpawnInfo(2, Walker::class)),
                Pair(DangerLevel.ORANGE, SpawnInfo(3, Walker::class)),
                Pair(DangerLevel.RED, SpawnInfo(4, Walker::class)),
            )
        )
    }

    class SpawnInfo(val quantity: Int, val type: KClass<out Zombie>)
}
