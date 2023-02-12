package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import java.util.*
import java.util.function.UnaryOperator
import java.util.stream.Stream

class Actors {
    private val actors: MutableMap<ActorId, Actor> = HashMap()

    fun add(actor: Actor) {
        actors[actor.id] = actor
    }

    fun remove(actor: Actor) {
        removeById(actor.id)
    }

    fun removeById(actorId: ActorId) {
        actors.remove(actorId)
    }

    fun findById(actorId: ActorId): Optional<Actor> {
        return Optional.ofNullable(actors[actorId])
    }

    fun all(): Stream<Actor> {
        return actors.values.stream()
    }

    fun update(actorId: ActorId, updater: UnaryOperator<Actor>) {
        val updatedActor = findById(actorId)
            .map { updater.apply(it) }
            .orElseThrow()
        actors[actorId] = updatedActor
    }
}