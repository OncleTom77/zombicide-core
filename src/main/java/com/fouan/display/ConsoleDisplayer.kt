package com.fouan.display

import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Zombie
import com.fouan.events.*
import com.fouan.game.view.GameView
import com.fouan.util.mergeLists
import com.fouan.zones.Position
import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import javax.inject.Named

@Named
class ConsoleDisplayer(
    private val output: Output,
    private val zonesQueries: ZonesQueries,
    private val actorsQueries: ActorsQueries,
    private val gameView: GameView,
    private val choiceMaker: ChoiceMaker,
    private val actorSelection: ActorSelection
) {
    private val log = KotlinLogging.logger { }

    @EventListener
    fun handleActorEvents(event: ActorEvent) {
        log.info("{}(turn: {})", event.javaClass.simpleName, event.turn)
    }

    @EventListener
    fun handleSurvivorAdded(event: SurvivorAdded) {
        output.display("Survivor ${event.survivor.name} added at ${event.zone.position}")
    }

    @EventListener
    fun handleZombieSpawned(event: ZombieSpawned) {
        output.display("Zombie ${event.zombie.name} spawned at ${event.zone.position}")
    }

    @EventListener
    fun handleZombieDied(event: ZombieDied) {
        output.display("Zombie ${event.zombie.name} is dead")
    }

    @EventListener
    fun handleAvailableZonesForSurvivorMoveDefined(event: AvailableZonesForSurvivorMoveDefined) {
        val availableZones = event.zones
        var choice = 0
        if (availableZones.size > 1) {
            output.display("Choose your action:")
            for (i in availableZones.indices) {
                output.display("$i: ${availableZones[i]}")
            }
            choice = choiceMaker.getChoice(0, availableZones.size - 1)
        }
        val chosenZone = availableZones[choice]
        gameView.fireEvent(ZoneChosen(event.turn, chosenZone.position))
    }

    @EventListener
    fun handleAvailableActionsDefined(event: AvailableActionsDefined) {
        displayBoard()
        val playingSurvivor = actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
            ?.let { id -> actorsQueries.findLivingSurvivorBy(id) }!!
        val remainingActions = actorsQueries.getRemainingActionsCountForActor(playingSurvivor.id, event.turn)
        output.display("$playingSurvivor's turn. Remaining actions: $remainingActions")

        val availableActions = event.actions
        var choice = 0
        if (availableActions.size > 1) {
            output.display("Choose your action:")
            for (i in availableActions.indices) {
                output.display("$i: ${availableActions[i]}")
            }
            choice = choiceMaker.getChoice(0, availableActions.size - 1)
        }
        val chosenAction = availableActions[choice]
        gameView.fireEvent(ActionChosen(event.turn, chosenAction))
    }

    @EventListener
    fun handleAvailableZombiesForSurvivorAttackDefined(event: AvailableZombiesForSurvivorAttackDefined) {
        val availableZombies = event.zombies.toList()
        output.display("Choose your target!")
        val chosenZombies = actorSelection.select(availableZombies, event.numberOfZombiesToChoose)
        gameView.fireEvent(ZombiesChosen(event.turn, chosenZombies))
    }

    @EventListener
    fun handleSurvivorAttacked(event: SurvivorAttacked) {
        val survivor = actorsQueries.findLivingSurvivorBy(event.actorId)!!
        output.display("Dice roll: ${event.attackResult.rolls}")
        output.display("${survivor.name} attacked with ${event.attackResult.weapon} and hits ${event.attackResult.hitCount} target(s)")
    }

    @EventListener
    fun handleZombiesAttackingSurvivorsDefined(event: AvailableSurvivorsForZombiesAttackDefined) {
        val zombies = event.zombieIds
            .map { actorsQueries.findZombieBy(it)!! }
        val survivors = event.survivorIds
            .map { actorsQueries.findLivingSurvivorBy(it)!! }
        val survivorNames = survivors
            .map(Survivor::name)
            .joinToString { ", " }
        output.display("${zombies.size} zombie(s) are attacking $survivorNames on zone at position ${event.position}")

        val attackDistribution = mutableMapOf<Survivor, List<Zombie>>()
        zombies.forEach { zombie: Zombie ->
            output.display("Choose a target for the attack of $zombie")
            output.display("Actual attack distribution: $attackDistribution")

            val selectedSurvivor = actorSelection.select(survivors)
            attackDistribution.merge(selectedSurvivor, listOf(zombie)) { a, b -> mergeLists(a, b) }
        }
        gameView.fireEvent(ZombieAttacksDistributed(event.turn, attackDistribution))
    }

    private fun displayBoard() {
        // Display board
        val zones = zonesQueries.findAll()
            .sortedWith(Zone.ZONE_POSITION_COMPARATOR)
        val horizontalLine = StringBuilder("-")
        val width = getWidth(zones)
        horizontalLine.append("-----".repeat(width))
        val line = StringBuilder()

        output.display(horizontalLine.toString())
        for (i in zones.indices) {
            val zone = zones[i]
            line.append("| ")
                .append(getStringRepresentation(zone))
                .append(" ")
            val endOfLine = i % width == width - 1
            if (endOfLine) {
                line.append("|")
                output.display(line.toString())
                line.deleteRange(0, line.length)
                output.display(horizontalLine.toString())
            }
        }
    }

    private fun getWidth(zones: List<Zone>): Int {
        return zones.map(Zone::position)
            .maxOfOrNull(Position::x)
            ?.let { it + 1 }!!
    }

    private fun getStringRepresentation(zone: Zone): String {
        val actorIds = zonesQueries.findActorIdsOn(zone.position)
        val containsSurvivors = actorIds.any { id -> actorsQueries.findLivingSurvivorBy(id) != null }
        val containsZombies = actorIds.any { id -> actorsQueries.findZombieBy(id) != null }

        return if (containsSurvivors) {
            "Su"
        } else if (containsZombies) {
            "Zo"
        } else if (zone.markers.contains(Zone.ZoneMarker.STARTING_ZONE)) {
            "St"
        } else if (zone.markers.contains(Zone.ZoneMarker.EXIT_ZONE)) {
            "Ex"
        } else if (zone.markers.contains(Zone.ZoneMarker.ZOMBIE_SPAWN)) {
            "Sp"
        } else {
            "  "
        }
    }
}
