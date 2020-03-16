package me.nickcruz.intcode

/** Computer object used to for [me.nickcruz.Day2] and [me.nickcruz.Day4] solutions. */
class IntCodeComputer {

    companion object {
        private const val OPCODE_ADD = 1
        private const val OPCODE_MULTIPLY = 2
        private const val OPCODE_TERMINATE = 99
    }

    private val program = mutableListOf<Int>()

    fun run(input: List<Int>): List<Int> {
        program.clear()
        program.addAll(input)
        var i = 0
        while (i < program.size) {
            i = when (program[i]) {
                OPCODE_ADD -> add(i)
                OPCODE_MULTIPLY -> multiply(i)
                OPCODE_TERMINATE -> terminate()
                else -> throw IllegalStateException("Unknown opcode: $i")
            }
        }
        return program.toList()
    }

    /**
     * Adds the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun add(currentIndex: Int) = runBiconsumerOpcode(currentIndex) { a, b ->
        a + b
    }

    /**
     * Multiples the values at the next two positions and puts it in the positions specified by the 3rd position.
     *
     * @param currentIndex The index of the [OPCODE_ADD] that was seen.
     * @return The new index to continue running the program on. Will be currentIndex + 4.
     */
    private fun multiply(currentIndex: Int) = runBiconsumerOpcode(currentIndex) { a, b ->
        a * b
    }

    private fun runBiconsumerOpcode(currentIndex: Int, operation: (Int, Int) -> Int): Int {
        val result = operation(program[program[currentIndex + 1]], program[program[currentIndex + 2]])
        program[program[currentIndex + 3]] = result
        return currentIndex + 4
    }

    private fun terminate() = Int.MAX_VALUE

}