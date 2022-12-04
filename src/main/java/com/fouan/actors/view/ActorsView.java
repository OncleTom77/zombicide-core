package com.fouan.actors.view;

import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import com.fouan.actors.zombies.Zombie;
import com.fouan.events.*;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesQueries;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Named
@AllArgsConstructor
public final class ActorsView implements ActorsCommands, ActorsQueries {

    private final List<ActorEvent> history = new LinkedList<>();
    private final Actors actors = new Actors();

    private final ZonesQueries zonesQueries;
    private final EventsPublisher eventsPublisher;

    @EventListener
    public void handleActorEvents(ActorEvent event) {
        log.info(event.getClass().getName());
        history.add(event);
    }

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        actors.add(event.getSurvivor());
    }

    @EventListener
    public void handleZombieSpawn(ZombieSpawned event) {
        actors.add(event.getZombie());
    }

    @EventListener
    public void handleZombieDied(ZombieDied event) {
        var zombie = findZombieBy(event.getZombieId())
                .orElseThrow(() -> new IllegalStateException("Zombie (id: " + event.getZombieId().value() + ") not found"));

        log.info("{} is dead", zombie);

        eventsPublisher.fire(
                new SurvivorGainedExperience(event.getTurn(), event.getAttackerId(), zombie.getExperienceProvided())
        );
    }

    @Override
    public void clear() {
        history.clear();
    }

    @Override
    public Optional<Survivor> findSurvivorBy(ActorId id) {
        return actors.findById(id)
                .filter(actor -> actor instanceof Survivor)
                .map(actor -> (Survivor) actor)
                .filter(survivor -> survivor.getLifeStatus() == Survivor.LifeStatus.ALIVE);
    }

    @Override
    public Stream<Survivor> allLivingSurvivors() {
        return actors.all()
                .filter(actor -> actor instanceof Survivor)
                .map(actor -> (Survivor) actor)
                .filter(survivor -> survivor.getLifeStatus() == Survivor.LifeStatus.ALIVE);
    }

    @Override
    public Optional<SurvivorWithZone> findSurvivorWithZoneBy(ActorId id) {
        return findSurvivorBy(id)
                .map(survivor -> new SurvivorWithZone(survivor, zonesQueries.findByActorId(id).orElse(null)));
    }

    @Override
    public Optional<Zombie> findZombieBy(ActorId id) {
        return actors.findById(id)
                .filter(actor -> actor instanceof Zombie)
                .map(actor -> (Zombie) actor);
    }

    @Override
    public Optional<ZombieWithZone> findZombieWithZoneBy(ActorId id) {
        return findZombieBy(id)
                .map(zombie -> new ZombieWithZone(zombie, zonesQueries.findByActorId(id).orElse(null)));
    }

    @Override
    public Set<Zombie> findAllZombiesOnSameZoneAsSurvivor(ActorId survivorId) {
        SurvivorWithZone survivorWithZone = findSurvivorWithZoneBy(survivorId)
                .orElseThrow();
        return zonesQueries.findActorIdsOn(survivorWithZone.zone())
                .stream()
                .map(this::findZombieBy)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ZombieWithZone> findZombiesWithZoneNearTo(Zone zone) {
        return Collections.emptyList();
    }

    @Override
    public int findRemainingActionsForSurvivor(@NotNull Survivor survivor, int turn) {
        return survivor.getActionsCount() - findEventForActorIdAndTurn(survivor.getId(), turn).size();
    }

    private List<ActionEvent> findEventForActorIdAndTurn(ActorId actorId, int turn) {
        return history.stream()
                .filter(event -> event instanceof ActionEvent)
                .map(event -> (ActionEvent) event)
                .filter(actionEvent -> actionEvent.getActorId().equals(actorId) && actionEvent.getTurn() == turn)
                .toList();
    }

    public Optional<ActorId> findCurrentSurvivorIdForTurn(int turn) {
        return history.stream()
                .filter(actorEvent -> actorEvent instanceof SurvivorsTurnStarted)
                .map(actorEvent -> (SurvivorsTurnStarted) actorEvent)
                .filter(survivorsTurnStarted -> survivorsTurnStarted.getTurn() == turn)
                .map(SurvivorsTurnStarted::getSurvivorId)
                .reduce((first, second) -> second);
    }
}
