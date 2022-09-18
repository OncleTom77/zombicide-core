package com.fouan.actors.view;

import com.fouan.actors.Actor;
import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class ComputedActors {

    private final Map<ActorId, ComputedActor> actors;

    public ComputedActors() {
        this.actors = new HashMap<>();
    }

    public Stream<ComputedActor> all() {
        return actors.values().stream();
    }

    public void add(ComputedActor actor) {
        if (actors.containsKey(actor.getId())) {
            throw new IllegalStateException("Actor already exists");
        }

        actors.put(actor.getId(), actor);
    }

    public void clear() {
        actors.clear();
    }

    public Optional<ComputedActor> findById(ActorId actorId) {
        return Optional.ofNullable(actors.get(actorId));
    }

    public void update(ActorId actorId, UnaryOperator<Actor> updater) {
        findById(actorId)
            .map(computedActor -> updater.apply(computedActor.actor))
            .ifPresent(actor -> actors.put(actorId, new ComputedActor(actor)));
    }

    @Getter
    public static class ComputedActor {

        private final Actor actor;

        // computed values
        private final Type type;
        private final LifeStatus lifeStatus;

        public ComputedActor(Actor actor) {
            this.actor = actor;
            this.type = (actor instanceof Survivor) ? Type.SURVIVOR : Type.ZOMBIE;
            this.lifeStatus = actor.getLifePoints() > 0 ? LifeStatus.ALIVE : LifeStatus.DEAD;
        }

        public ActorId getId() {
            return actor.getId();
        }

        public int getLifePoints() {
            return actor.getLifePoints();
        }

        public enum Type {
            SURVIVOR, ZOMBIE
        }
    }
}
