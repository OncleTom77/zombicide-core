package com.fouan.display;

import com.fouan.events.SurvivorAdded;
import com.fouan.events.ZombieSpawned;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import javax.inject.Named;

@Named
@AllArgsConstructor
public final class ConsoleDisplayer {

    private final Output output;

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        output.display("Survivor " + event.getSurvivor().getName() + " added at " + event.getZone().getPosition().toString());
    }

    @EventListener
    public void handleZombieSpawned(ZombieSpawned event) {
        output.display("Zombie " + event.getZombie().getName() + " spawned at " + event.getZone().getPosition().toString());
    }
}
