package com.fouan.actors.view;

import com.fouan.actors.zombies.Zombie;
import com.fouan.zones.Zone;

public record ZombieWithZone(Zombie zombie, Zone zone) {}
