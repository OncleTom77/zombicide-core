# Zombicide core

Zombicide core is a project that implements the main mechanisms of the board game Zombicide.

## Step by step implementation

### 1. Basic game

#### a. Initialize game

- load map with only outside Zone (no moving restrictions yet)
- place one Survivor on the starting Zone
- place the Exit objective on a Zone

#### b. Move Survivor

- ask for an adjacent Zone to move the Survivor to
- move the Survivor accordingly

#### c. Determine victory

- if the Survivor is on the Exit Zone, the Survivor wins, end of the game. Otherwise, continue

### 2. Add first Zombie

- place one Zombie on a Zone when initialize the map
- the Zombie has one action to spend to move to one random Zone after the Survivor's turn

### 3. Add weapon

- add a weapon to the survivor at the game initialization phase
- at his Turn, the survivor can now choose between attacking a zombie in the same Zone or moving to one adjacent Zone
- if the survivor hits a zombie (roll dice to know either the weapon hit the zombie or not), the zombie is dead and never appears again in the game

### 4. Introduce life points

- if a zombie hits the survivor, the survivor increases his Wound counter by one. 
- the survivor dies as soon as his Wound counter reaches 3
 
### 5. Give the survivor 3 Actions per Turn

- check victory or defeat after each action spent

### 6. Zombie invasion phase

First:
- adapt game to have multiple Zombies
- ask survivor which zombie to hit when there is more than one zombie is his Zone
- play all Zombies when playing Zombies' phase
- resolve Zombie attacks before Zombie moves

Then
- generate Zombies for each Zombie Spawn on the map

### 7. Multiple survivors

- make all survivors play
- compute the end of the game according to all the survivors
- when a zombie attacks a zone where there are multiple survivors, ask players which survivor gets injured
- when there are multiple zombies in the same zone, make them attack together against the same survivor

### 7.5 Bonus

- implements design pattern
  - command pattern
- refactor game to handle interactions with event-based system
  - have a centralized event collector
  - when zombie needs an input from user, send an input request event, wait for the response and continue

### 8. Zombies move to the survivors (no more random)

### 9. Introduce buildings (without closed doors for now)

- wall
- new Search Action for survivors

### 10. Add doors to buildings

- new action to open doors
- when the first building door is open, generate Zombies in each building's Zones

### Other

- new Zombie types: Fatty, Runner, Abomination
- new weapons types: ranged, magic, enchantments
- new Actions: make noise, reorganize inventor
- introduction of scenario with new objectives: retrieve specific items on the board, etc.
- new Zombie Invasion card (additional Zombie Activation)
- line of sight
- Survivor skills, experience, danger level (modify zombie spawn phase)
- multiple weapons, body equipments
- import map from file
