package me.nickcruz

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * --- Day 3: Crossed Wires ---
 * The gravity assist was successful, and you're well on your way to the Venus refuelling station. During the rush back
 * on Earth, the fuel management system wasn't completely installed, so that's next on the priority list.
 *
 * Opening the front panel reveals a jumble of wires. Specifically, two wires are connected to a central port and extend
 * outward on a grid. You trace the path each wire takes as it leaves the central port, one wire per line of text (your
 * puzzle input).
 *
 * The wires twist and turn, but the two wires occasionally cross paths. To fix the circuit, you need to find the
 * intersection point closest to the central port. Because the wires are on a grid, use the Manhattan distance for this
 * measurement. While the wires do technically cross right at the central port where they both start, this point does
 * not count, nor does a wire count as crossing with itself.
 *
 * For example, if the first wire's path is R8,U5,L5,D3, then starting from the central port (o), it goes right 8, up 5,
 * left 5, and finally down 3:
 *
 * ...........
 * ...........
 * ...........
 * ....+----+.
 * ....|....|.
 * ....|....|.
 * ....|....|.
 * .........|.
 * .o-------+.
 * ...........
 * Then, if the second wire's path is U7,R6,D4,L4, it goes up 7, right 6, down 4, and left 4:
 *
 * ...........
 * .+-----+...
 * .|.....|...
 * .|..+--X-+.
 * .|..|..|.|.
 * .|.-X--+.|.
 * .|..|....|.
 * .|.......|.
 * .o-------+.
 * ...........
 * These wires cross at two locations (marked X), but the lower-left one is closer to the central port: its distance is
 * 3 + 3 = 6.
 *
 * Here are a few more examples:
 *
 * R75,D30,R83,U83,L12,D49,R71,U7,L72
 * U62,R66,U55,R34,D71,R55,D58,R83 = distance 159
 * R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
 * U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = distance 135
 *
 * What is the Manhattan distance from the central port to the closest intersection?
 *
 * --- Part Two ---
 * It turns out that this circuit is very timing-sensitive; you actually need to minimize the signal delay.
 *
 * To do this, calculate the number of steps each wire takes to reach each intersection; choose the intersection where
 * the sum of both wires' steps is lowest. If a wire visits a position on the grid multiple times, use the steps value
 * from the first time it visits that position when calculating the total value of a specific intersection.
 *
 * The number of steps a wire takes is the total number of grid squares the wire has entered to get to that location,
 * including the intersection being considered. Again consider the example from above:
 *
 * ...........
 * .+-----+...
 * .|.....|...
 * .|..+--X-+.
 * .|..|..|.|.
 * .|.-X--+.|.
 * .|..|....|.
 * .|.......|.
 * .o-------+.
 * ...........
 * In the above example, the intersection closest to the central port is reached after 8+5+5+2 = 20 steps by the first
 * wire and 7+6+4+3 = 20 steps by the second wire for a total of 20+20 = 40 steps.
 *
 * However, the top-right intersection is better: the first wire takes only 8+5+2 = 15 and the second wire takes only
 * 7+6+2 = 15, a total of 15+15 = 30 steps.
 *
 * Here are the best steps for the extra examples from above:
 *
 * R75,D30,R83,U83,L12,D49,R71,U7,L72
 * U62,R66,U55,R34,D71,R55,D58,R83 = 610 steps
 * R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
 * U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = 410 steps
 *
 * What is the fewest combined steps the wires must take to reach an intersection?
 */
class Day3 {

    fun run(schema1: String, schema2: String) = FuelManagementSystem().findManhattanDistance(schema1, schema2)

    class FuelManagementSystem {

        companion object {
            private const val MAX_WIRE_LENGTH = 20000 // Arbitrary num so far
            private const val CENTRAL_PORT_X = MAX_WIRE_LENGTH / 2
            private const val CENTRAL_PORT_Y = CENTRAL_PORT_X
        }

        private val circuit = MutableList(MAX_WIRE_LENGTH) {
            MutableList(MAX_WIRE_LENGTH) {
                Wire.EMPTY
            }
        }.apply {
            this[CENTRAL_PORT_X][CENTRAL_PORT_Y] = Wire.CENTRAL_PORT
        }

        /**
         * CENTRAL_PORT is in fact a CROSS, but will be its own enum to distinguish itself. (Edge case: crossing at central
         * port again?)
         *
         * EMPTY is the initial state of everything except for the CENTRAL_PORT.
         *
         * WIRE1 is everything after the first wire's schema is drawn.
         *
         * CROSS is any WIRE1's that we run into while "drawing" wire 2. For this reason, we don't need WIRE2, as we are
         * only looking for CROSSes. (In fact, we may not even need this one.)
         */
        enum class Wire {
            CENTRAL_PORT,
            EMPTY,
            WIRE1,
            CROSS
        }

        /**
         * Creates a 2D [MutableList] of [Int]s. Each value represents how that wire is.
         *
         * This function will "draw" the wires based on the string counted. This way, while wire 2 is getting drawn, we can
         * see where the wires cross.
         *
         * Complexity will be O(DI) where D is the distance marked by each instruction and I is the total number of
         * instructions.
         */
        fun findManhattanDistance(schema1: String, schema2: String): Int {
            // Draw wire 1 based on schema1
            drawWire(schema1) { _, _, wire ->
                when (wire) {
                    Wire.EMPTY -> Wire.WIRE1
                    else -> wire
                }
            }

            var minimumManhattanDistance = Int.MAX_VALUE

            // Draw wire 2 based on schema2
            drawWire(schema2) { x, y, wire ->
                when (wire) {
                    Wire.WIRE1 -> {
                        minimumManhattanDistance = minOf(minimumManhattanDistance, findManhattanDistance(x, y))
                        Wire.CROSS
                    }
                    else -> wire
                }
            }
            return minimumManhattanDistance
        }

        private fun findManhattanDistance(x: Int, y: Int) =
            (CENTRAL_PORT_X - x).absoluteValue + (CENTRAL_PORT_Y - y).absoluteValue

        /**
         * Draws a wire based on a given schema.
         *
         * @param schema The wire to draw. Draws it one wire at a time.
         * @param onWireDrawn Callback for when each point in the circuit is hit.
         */
        private fun drawWire(schema: String, onWireDrawn: (x: Int, y: Int, Wire) -> Wire) {
            // Start at central port
            var currentX = CENTRAL_PORT_X
            var currentY = CENTRAL_PORT_Y

            // Draw wire based on schema, delimited by ','
            for (vector in schema.split(',')) {
                val direction = vector[0]
                val magnitude = vector.substring(1).toInt()

                val prevX = currentX
                val prevY = currentY
                when (direction) {
                    'L' -> currentX -= magnitude
                    'R' -> currentX += magnitude
                    'D' -> currentY -= magnitude
                    'U' -> currentY += magnitude
                }

                val fromX = min(prevX, currentX)
                val toX = max(prevX, currentX)
                val fromY = min(prevY, currentY)
                val toY = max(prevY, currentY)

                for (x in fromX..toX) {
                    for (y in fromY..toY) {
                        circuit[x][y] = onWireDrawn(x, y, circuit[x][y])
                    }
                }
            }
        }
    }
}