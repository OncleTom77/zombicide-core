package com.fouan.actors.view;

import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import com.fouan.actors.zombies.Zombie;
import com.fouan.events.*;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesQueries;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.fouan.actors.view.ComputedActors.ComputedActor.Type.SURVIVOR;
import static com.fouan.actors.view.ComputedActors.ComputedActor.Type.ZOMBIE;
import static com.fouan.actors.view.LifeStatus.ALIVE;

@Named
@AllArgsConstructor
final class ActorsView implements ActorsCommands, ActorsQueries {

    private final List<ActorEvent> history = new LinkedList<>();
    private final ComputedActors actors = new ComputedActors();

    private final ZonesQueries zonesQueries;
    private final EventsPublisher eventsPublisher;

    @EventListener
    public void handleActorEvents(ActorEvent event) {
        history.add(event);
    }

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        actors.add(new ComputedActors.ComputedActor(event.getSurvivor()));
    }

    @EventListener
    public void handleZombieSpawn(ZombieSpawned event) {
        actors.add(new ComputedActors.ComputedActor(event.getZombie()));
    }

    @EventListener
    public void handleZombieDied(ZombieDied event) {
        var zombie = findZombieBy(event.getZombieId())
            .orElseThrow(() -> new IllegalStateException("Zombie (id: " + event.getZombieId().value() + ") not found"));

        eventsPublisher.fire(
            new SurvivorGainedExperience(event.getTurn(), event.getAttackerId(), zombie.getExperienceProvided())
        );
    }

    @Override
    public void clear() {
        actors.clear();
        history.clear();
    }

    @Override
    public Optional<Survivor> findSurvivorBy(ActorId id) {
        return actors.findById(id)
            .filter(actor -> SURVIVOR.equals(actor.getType()))
            .filter(actor -> ALIVE.equals(actor.getLifeStatus()))
            .map(actor -> (Survivor) actor.getActor());
    }

    @Override
    public Stream<Survivor> allLivingSurvivors() {
        return actors.all()
            .filter(actor -> SURVIVOR.equals(actor.getType()))
            .filter(actor -> ALIVE.equals(actor.getLifeStatus()))
            .map(actor -> (Survivor) actor.getActor());
    }

    @Override
    public Optional<SurvivorWithZone> findSurvivorWithZoneBy(ActorId id) {
        return findSurvivorBy(id)
            .map(survivor -> new SurvivorWithZone(survivor, zonesQueries.findByActorId(id).orElse(null)));
    }

    @Override
    public Optional<Zombie> findZombieBy(ActorId id) {
        return actors.findById(id)
                .filter(actor -> ZOMBIE.equals(actor.getType()))
                .filter(actor -> ALIVE.equals(actor.getLifeStatus()))
                .map(actor -> (Zombie) actor.getActor());
    }

    @Override
    public Optional<ZombieWithZone> findZombieWithZoneBy(ActorId id) {
        return findZombieBy(id)
            .map(zombie -> new ZombieWithZone(zombie, zonesQueries.findByActorId(id).orElse(null)));
    }

    @Override
    public List<ZombieWithZone> findZombiesWithZoneNearTo(Zone zone) {
        return Collections.emptyList();
    }
}
