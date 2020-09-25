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

- check victory of defeat after each action spent

### 6. Zombie invasion phase

- ask survivor which zombie to hit when there is more than one zombie is his Zone

### 7. Zombies move to the survivor (no more random)

### 8. Introduce buildings (without closed doors for now)

- wall
- new Search Action for survivors

### 9. Add doors to buildings

- new action to open doors
- when the first building door is open, generate Zombies in each building's Zones

### Other

- new Zombie types: Fatty, Runner, Abomination
- new weapons types: ranged, magic, enchantments
- new Actions: make noise, do nothing, reorganize inventor
- new objectives: retrieve specific items on the board
- new Zombie Invasion card (additional Zombie Activation)
- line of sight
- Survivor skills
