package me.nickcruz.intcode

import java.util.LinkedList
import java.util.Queue

/** Computer object used to for [me.nickcruz.Day2] and [me.nickcruz.Day4] solutions. */
class IntCodeComputer(startingProgram: List<Int>) {

    companion object {
        private const val OPCODE_ADD = 1
        private const val OPCODE_MULTIPLY = 2
        private const val OPCODE_STORE = 3
        private const val OPCODE_PRINT = 4
        private const val OPCODE_TERMINATE = 99
    }

    /**
     * The resulting program in the computer after [run].
     */
    val program: List<Int>
        get() = runningProgram.toList()

    private val runningProgram = startingProgram.toMutableList()

    private val remainingInputs: Queue<Int> = LinkedList()

    private val outputs = LinkedList<Int>()

    /**
     * @param inputs A list of inputs that will be input into the program whenever the program is asked for input. These
     *               will be input in the order that they are requested. (Day5 only asks for a single input for now, and
     *               it's difficult to engineer a solution that will work for multiple different inputs without knowing
     *               other scenarios of how that would even work.)
     *
     * If there are no inputs left to "input" for [OPCODE_STORE], this will input [OPCODE_TERMINATE].
     *
     * @return A list of [Int]s that were printed by any [OPCODE_PRINT] instructions.
     */
    fun run(inputs: List<Int> = emptyList()): List<Int> {

        // TODO(nick): Implement immediate mode, since everything is parameter mode right now

        remainingInputs.clear()
        remainingInputs.addAll(inputs)
        var i = 0
        while (i < runningProgram.size) {
            i = when (runningProgram[i]) {
                OPCODE_ADD -> add(i)
                OPCODE_MULTIPLY -> multiply(i)
                OPCODE_TERMINATE -> terminate()
                OPCODE_STORE -> store(i)
                OPCODE_PRINT -> print(i)
                else -> throw IllegalStateException("Unknown opcode: $i")
            }
        }
        return outputs
    }

    /**
     * Adds the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun add(currentIndex: Int) = runBiConsumerOpcode(currentIndex) { a, b ->
        a + b
    }

    /**
     * Multiples the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun multiply(currentIndex: Int) = runBiConsumerOpcode(currentIndex) { a, b ->
        a * b
    }

    /**
     * Stores the value received from a prepared input and puts it in the position specified by the next position.
     */
    private fun store(currentIndex: Int): Int {
        var i = currentIndex
        val input = remainingInputs.poll() ?: 0
        runningProgram[runningProgram[++i]] = input
        return ++i
    }

    /**
     * Prints the value at a position specified by the next position.
     */
    private fun print(currentIndex: Int): Int {
        var i = currentIndex
        outputs.push(runningProgram[runningProgram[++i]])
        return ++i
    }

    private fun runBiConsumerOpcode(currentIndex: Int, operation: (Int, Int) -> Int): Int {
        var i = currentIndex
        val result = operation(runningProgram[runningProgram[++i]], runningProgram[runningProgram[++i]])
        runningProgram[runningProgram[++i]] = result
        return ++i
    }

    private fun terminate() = Int.MAX_VALUE

}