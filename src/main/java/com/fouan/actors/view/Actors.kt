package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import java.util.*
import java.util.stream.Stream
import kotlin.collections.HashMap

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

/*public void update(ActorId actorId, UnaryOperator<Actor> updater) {
        findById(actorId)
            .map(computedActor -> updater.apply(computedActor.actor))
            .ifPresent(actor -> actors.put(actorId, new ComputedActor(actor)));
    }*/
}