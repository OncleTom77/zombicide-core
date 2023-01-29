package com.fouan.actors.view;

import com.fouan.actors.Actor;
import com.fouan.actors.ActorId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class Actors {

    private final Map<ActorId, Actor> actors;

    public Actors() {
        this.actors = new HashMap<>();
    }

    public void add(Actor actor) {
        actors.put(actor.getId(), actor);
    }

    public void remove(Actor actor) {
        removeById(actor.getId());
    }

    public void removeById(ActorId actorId) {
        actors.remove(actorId);
    }

    public Optional<Actor> findById(ActorId actorId) {
        return Optional.ofNullable(actors.get(actorId));
    }

    public Stream<Actor> all() {
        return actors.values().stream();
    }


    /*public void update(ActorId actorId, UnaryOperator<Actor> updater) {
        findById(actorId)
            .map(computedActor -> updater.apply(computedActor.actor))
            .ifPresent(actor -> actors.put(actorId, new ComputedActor(actor)));
    }*/
}
