package me.nickcruz

import java.util.LinkedList
import java.util.Queue
import kotlin.math.min

/**
 * --- Day 6: Universal Orbit Map ---
 *
 * You've landed at the Universal Orbit Map facility on Mercury. Because navigation in space often involves transferring
 * between orbits, the orbit maps here are useful for finding efficient routes between, for example, you and Santa. You
 * download a map of the local orbits (your puzzle input).
 *
 * Except for the universal Center of Mass (COM), every object in space is in orbit around exactly one other object. An
 * orbit looks roughly like this:
 *
 * (see website lol)
 * \
 * \
 * |
 * |
 * AAA--> o            o <--BBB
 * |
 * |
 * /
 * /
 *
 * In this diagram, the object BBB is in orbit around AAA. The path that BBB takes around AAA (drawn with lines) is only
 * partly shown. In the map data, this orbital relationship is written AAA)BBB, which means "BBB is in orbit around
 * AAA".
 *
 * Before you use your map data to plot a course, you need to make sure it wasn't corrupted during the download. To
 * verify maps, the Universal Orbit Map facility uses orbit count checksums - the total number of direct orbits (like
 * the one shown above) and indirect orbits.
 *
 * Whenever A orbits B and B orbits C, then A indirectly orbits C. This chain can be any number of objects long: if A
 * orbits B, B orbits C, and C orbits D, then A indirectly orbits D.
 *
 * For example, suppose you have the following map:
 *
 * COM)B
 * B)C
 * C)D
 * D)E
 * E)F
 * B)G
 * G)H
 * D)I
 * E)J
 * J)K
 * K)L
 * Visually, the above map of orbits looks like this:
 *
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *       \
 *        I
 *
 * In this visual representation, when two objects are connected by a line, the one on the right directly orbits the one
 * on the left.
 *
 * Here, we can count the total number of orbits as follows:
 *
 * D directly orbits C and indirectly orbits B and COM, a total of 3 orbits.
 * L directly orbits K and indirectly orbits J, E, D, C, B, and COM, a total of 7 orbits.
 * COM orbits nothing.
 * The total number of direct and indirect orbits in this example is 42.
 *
 * What is the total number of direct and indirect orbits in your map data?
 *
 * --- Part Two ---
 * Now, you just need to figure out how many orbital transfers you (YOU) need to take to get to Santa (SAN).
 *
 * You start at the object YOU are orbiting; your destination is the object SAN is orbiting. An orbital transfer lets
 * you move from any object to an object orbiting or orbited by that object.
 *
 * For example, suppose you have the following map:
 *
 * COM)B
 * B)C
 * C)D
 * D)E
 * E)F
 * B)G
 * G)H
 * D)I
 * E)J
 * J)K
 * K)L
 * K)YOU
 * I)SAN
 * Visually, the above map of orbits looks like this:
 *
 *                           YOU
 *                          /
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *               \
 *                I - SAN
 *
 * In this example, YOU are in orbit around K, and SAN is in orbit around I. To move from K to I, a minimum of 4 orbital
 * transfers are required:
 *
 * K to J
 * J to E
 * E to D
 * D to I
 * Afterward, the map of orbits looks like this:
 *
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *               \
 *                I - SAN
 *                \
 *                 YOU
 *
 * What is the minimum number of orbital transfers required to move from the object YOU are orbiting to the object SAN
 * is orbiting? (Between the objects they are orbiting - not between YOU and SAN.)
 */
class Day6 {

    fun countOrbits(orbitMap: List<String>): Int {
        var totalOrbits = 0
        assembleCelestialBodies(orbitMap) {
            totalOrbits += it.orbitDepth
        }
        return totalOrbits
    }

    fun countOrbitalTransfers(orbitMap: List<String>, start: String, end: String): Int {
        // Find start and start there. Starting point is actually start's center
        var startingBody: CelestialBody? = null
        var endingBody: CelestialBody? = null
        val celestialBodies = assembleCelestialBodies(orbitMap) {
            when (it.name) {
                start -> startingBody = it.center
                end -> endingBody = it.center
            }
        }
        val requireStart = startingBody ?: throw IllegalStateException("StartingBody is null: start: $start")
        val requireEnd = endingBody ?: throw IllegalStateException("EndingBody is null: end: $end")

        // BFS through graph until you find the ending body
        val traversed = mutableSetOf(start, requireStart.name)
        val pendingTransfers: Queue<Transfer> = LinkedList()
        pendingTransfers.add(Transfer(requireStart, 0))

        var minTransfers: Int? = null

        while (pendingTransfers.isNotEmpty()) {
            val currentTransfer = pendingTransfers.remove()

            // Min the completed transfer distances.
            if (currentTransfer.current == requireEnd) {
                val completedTransferDistance = currentTransfer.transfersSoFar
                minTransfers = min(minTransfers ?: Int.MAX_VALUE, completedTransferDistance)
                continue
            }

            // Add all remaining possible orbits (+ parent) to queue.
            val bodiesToCheck = currentTransfer.current.orbits
                .filter { !traversed.contains(it.name) }
                .toMutableList()

            currentTransfer.current.center?.let {
                bodiesToCheck.add(it)
            }

            pendingTransfers.addAll(bodiesToCheck
                .onEach { traversed.add(it.name) }
                .map { Transfer(it, currentTransfer.transfersSoFar + 1) })
        }

        // Return how many transfers you did
        return minTransfers ?: throw IllegalStateException("Couldn't find $end's center.")
    }

    private fun assembleCelestialBodies(
        orbitMap: List<String>,
        onCelestialBodyCreated: (CelestialBody) -> Unit = {}
    ): CelestialBody {
        // Assemble strings into pairs, where each center points to its orbits.
        val orbitPairs = orbitMap.map { orbitPair ->
            val objectPair = orbitPair.split(ORBIT_DELIMITER)
            if (objectPair.size != 2) {
                throw IllegalArgumentException("Unable to parse orbit pair string. Orbit Pair: $orbitPair")
            }
            val center = objectPair[0]
            val orbit = objectPair[1]
            center to orbit
        }

        // Assemble a map:
        // Keys = center
        // Values = set of its orbits
        val centerToOrbits = orbitPairs.groupBy(Pair<String, String>::first, Pair<String, String>::second)

        // Recursively assemble a tree:
        // Start at Center Of Mass, then add all orbits
        return createCelestialBody(
            centerToOrbits = centerToOrbits,
            name = CENTER_OF_MASS,
            orbitDepth = 0,
            center = null,
            onCelestialBodyCreated = onCelestialBodyCreated
        )
    }

    private fun createCelestialBody(
        centerToOrbits: Map<String, List<String>>,
        name: String,
        orbitDepth: Int,
        center: CelestialBody? = null,
        onCelestialBodyCreated: (CelestialBody) -> Unit = {}
    ): CelestialBody {
        val orbitNames = centerToOrbits[name] ?: emptyList()

        return CelestialBody(
            name = name,
            orbitDepth = orbitDepth,
            center = center
        )
            .apply {
                orbits = orbitNames.map { orbitName ->
                    createCelestialBody(
                        centerToOrbits = centerToOrbits,
                        name = orbitName,
                        orbitDepth = orbitDepth + 1,
                        center = this,
                        onCelestialBodyCreated = onCelestialBodyCreated
                    )
                }
            }
            .apply(onCelestialBodyCreated)
    }
}

private const val ORBIT_DELIMITER = ')'
private const val CENTER_OF_MASS = "COM"

data class Transfer(val current: CelestialBody, val transfersSoFar: Int = 0)

class CelestialBody(
    val name: String,
    var center: CelestialBody? = null,
    var orbits: List<CelestialBody> = emptyList(),

    // Number of orbits from center of mass, which is basically the depth of the celestial object in a tree.
    val orbitDepth: Int = 0


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CelestialBody

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}