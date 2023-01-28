package com.fouan.weapons;

public record AttackResult(Weapon weapon, java.util.List<Integer> rolls, int hitCount) { }
