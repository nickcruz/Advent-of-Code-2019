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
        private const val OPCODE_JUMP_IF_TRUE = 5
        private const val OPCODE_JUMP_IF_FALSE = 6
        private const val OPCODE_LESS_THAN = 7
        private const val OPCODE_EQUALS = 8
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
        remainingInputs.clear()
        remainingInputs.addAll(inputs)
        var i = 0
        loop@ while (true) {
            val instruction = runningProgram[i]
            i = when (instruction.opcode()) {
                OPCODE_ADD -> add(instruction, i)
                OPCODE_MULTIPLY -> multiply(instruction, i)
                OPCODE_TERMINATE -> {
                    break@loop
                }
                OPCODE_STORE -> storeInput(i)
                OPCODE_PRINT -> print(i)
                OPCODE_JUMP_IF_TRUE -> jump(instruction, i, true)
                OPCODE_JUMP_IF_FALSE -> jump(instruction, i, false)
                OPCODE_LESS_THAN -> lessThan(instruction, i)
                OPCODE_EQUALS -> equals(instruction, i)
                else -> throw IllegalStateException("Unknown opcode: $instruction")
            }
            if (i > runningProgram.size) {
                throw IllegalStateException("Instruction pointer past end of program. i: $i, size: ${runningProgram.size}")
            }
        }
        return outputs
    }

    /**
     * Adds the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param instruction The full instruction value received at [currentIndex].
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun add(instruction: Int, currentIndex: Int) = runBiConsumerOpcode(instruction, currentIndex) { a, b ->
        a + b
    }

    /**
     * Multiples the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param instruction The full instruction value received at [currentIndex].
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun multiply(instruction: Int, currentIndex: Int) = runBiConsumerOpcode(instruction, currentIndex) { a, b ->
        a * b
    }

    /**
     * Stores the value received from a prepared input and puts it in the position specified by the next position.
     */
    private fun storeInput(currentIndex: Int): Int {
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

    /**
     * Checks if the first parameter is non-zero, then sets the instruction pointer to the value from the second
     * parameter. Otherwise, it does nothing.
     *
     * @param jumpIfTrue Jumps if the first param is non-zero. Otherwise, jumps if zero.
     */
    private fun jump(instruction: Int, currentIndex: Int, jumpIfTrue: Boolean): Int {
        var i = currentIndex
        val arg1 = retrieveValue(instruction.isArg1Immediate(), ++i)

        val isArg1NonZero = arg1 != 0
        ++i
        return if ((isArg1NonZero && jumpIfTrue) || (!isArg1NonZero && !jumpIfTrue)) {
            retrieveValue(instruction.isArg2Immediate(), i)
        } else {
            i
        }
    }

    /**
     * If the first parameter is less than the second parameter, it stores 1 in the position given by the third
     * parameter. Otherwise, it stores 0.
     */
    private fun lessThan(instruction: Int, currentIndex: Int) = runBiConsumerOpcode(instruction, currentIndex) { a, b ->
        if (a < b) 1 else 0
    }

    /**
     * If the first parameter is equal to the second parameter, it stores 1 in the position given by the third
     * parameter. Otherwise, it stores 0.
     */
    private fun equals(instruction: Int, currentIndex: Int) = runBiConsumerOpcode(instruction, currentIndex) { a, b ->
        if (a == b) 1 else 0
    }

    private fun runBiConsumerOpcode(
        instruction: Int,
        currentIndex: Int,
        operation: (Int, Int) -> Int
    ): Int {
        var i = currentIndex
        val arg1 = retrieveValue(instruction.isArg1Immediate(), ++i)
        val arg2 = retrieveValue(instruction.isArg2Immediate(), ++i)
        val result = operation(arg1, arg2)
        storeResult(instruction.isArg3Immediate(), ++i, result)
        return ++i
    }

    private fun retrieveValue(isImmediateMode: Boolean, index: Int): Int = if (isImmediateMode) {
        runningProgram[index]
    } else {
        runningProgram[runningProgram[index]]
    }

    private fun storeResult(isImmediateMode: Boolean, index: Int, value: Int) {
        if (isImmediateMode) {
            runningProgram[index] = value
        } else {
            runningProgram[runningProgram[index]] = value
        }
    }

    private fun Int.opcode() = this % 100

    private fun Int.isArg1Immediate() = (this % 1000) / 100 == 1

    private fun Int.isArg2Immediate() = (this % 10000) / 1000 == 1

    private fun Int.isArg3Immediate() = (this % 100000) / 10000 == 1

}