package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import java.util.function.UnaryOperator

class Actors {
    private val actors: MutableMap<ActorId, Actor> = mutableMapOf()

    fun add(actor: Actor) {
        actors[actor.id] = actor
    }

    fun remove(actor: Actor) {
        removeById(actor.id)
    }

    fun removeById(actorId: ActorId) {
        actors.remove(actorId)
    }

    fun findById(actorId: ActorId): Actor? {
        return actors[actorId]
    }

    fun all(): List<Actor> {
        return actors.values.toList()
    }

    fun update(actorId: ActorId, updater: UnaryOperator<Actor>) {
        val actor = findById(actorId)!!
        actors[actorId] = updater.apply(actor)
    }
}