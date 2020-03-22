package me.nickcruz

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
 */
class Day6 {

    fun countOrbits(orbitMap: List<String>): Int {
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
        var totalOrbits = 0
        createCelestialBody(centerToOrbits, CENTER_OF_MASS, 0) {
            totalOrbits += it.orbitDepth
        }

        return totalOrbits
    }

    private fun createCelestialBody(
        centerToOrbits: Map<String, List<String>>,
        name: String,
        orbitDepth: Int,
        onCelestialObjectCreated: (CelestialObject) -> Unit
    ): CelestialObject {
        val orbits = centerToOrbits[name] ?: emptyList()
        return CelestialObject(
            name,
            orbits.map { createCelestialBody(centerToOrbits, it, orbitDepth + 1, onCelestialObjectCreated) },
            orbitDepth
        ).apply(onCelestialObjectCreated)
    }
}

private const val ORBIT_DELIMITER = ')'
private const val CENTER_OF_MASS = "COM"

data class CelestialObject(
    val name: String,
    val orbits: List<CelestialObject> = emptyList(),

    // Number of orbits from center of mass, which is basically the depth of the celestial object in a tree.
    val orbitDepth: Int = 0
)